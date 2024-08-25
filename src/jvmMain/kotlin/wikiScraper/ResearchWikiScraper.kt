package wikiScraper

import Material
import ResearchCategory
import ResearchProject
import jsonMapper
import kotlinx.serialization.encodeToString
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import java.io.File


fun main() {
    val inputFile = File("raw-data/research.html")
    val output = File("src/jsMain/resources/research-wiki-data.json")

    println("Reading Research")
    parseResearch(Jsoup.parse(inputFile.readText())).let { output.writeText(jsonMapper.encodeToString(it)) }

}

private fun parseResearch(page: Document): List<ResearchProject> {
    return page.select(".wikitable").take(1).flatMap { table ->
        val category = table.select("caption").text().replace(" Research Tree", "").let { r -> ResearchCategory.entries.firstOrNull { it.name.lowercase() == r.lowercase() } } ?: ResearchCategory.OTHER
        table.select("tr").drop(1).map { row ->
            val rawName = row.selectTdClean(0)?.split(" ") ?: listOf()
            val name = rawName.dropLast(1).joinToString(" ")
            val rank = rawName.last().toIntOrNull() ?: 1
            val preReqs = row.selectTd(1)?.text()?.toRankMap() ?: mapOf()
            val perks = row.selectTd(2)?.text()?.toRankMap() ?: mapOf()
            val description = row.selectTdClean(4) ?: ""
            val materials = row.selectTd(3).parseMaterials()

            ResearchProject(name, category, rank, description, preReqs, perks, materials)
        }
    }
}

private fun String.toRankMap(): Map<String, Int> {
    val parts = split(Regex("(?<=\\D)(?=\\d)")).flatMap { line ->
        val lineParts = line.replace("Rank", "").split(" ")
        if (lineParts.first().toIntOrNull() != null) {
            listOf(lineParts.first(), lineParts.drop(1).joinToString(" "))
        } else listOf(line.replace("Rank", "").trim())
    }.filter { it.isNotEmpty() }

    return if (parts.size >= 2) {
        parts.chunked(2).associate { it[0] to (it[1].toIntOrNull() ?: 1) }
    } else mapOf()
}

private fun Element?.parseMaterials() : List<Material>{
    if (this == null) return listOf()

    val amounts = this.html().split("<br>").map { it.substring(it.indexOf("x")+1).toIntOrNull() ?: 1 }
    return select("a").mapIndexed { i, a ->
        Material(a.text(), amounts[i], a.attr("href"))
    }
}
