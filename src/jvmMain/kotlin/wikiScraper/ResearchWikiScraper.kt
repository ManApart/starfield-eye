package wikiScraper

import ResearchCategory
import ResearchProject
import jsonMapper
import kotlinx.serialization.encodeToString
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import java.io.File


fun main() {
    val inputFile = File("raw-data/research.html")
    val output = File("src/jsMain/resources/research-wiki-data.json")

    println("Reading Research")
    parseResearch(Jsoup.parse(inputFile.readText())).let { output.writeText(jsonMapper.encodeToString(it)) }

}

private fun parseResearch(page: Document): List<ResearchProject> {
    page.select(".wikitable").take(1).flatMap { table ->
        val category = table.select("caption").text().replace("Research Tree", "").let { r -> ResearchCategory.entries.firstOrNull { it.name.lowercase() == r.lowercase() } } ?: ResearchCategory.OTHER
        table.select("tr").drop(1).map { row ->
            val rawName = row.selectTdClean(0)?.split(" ") ?: listOf()
            val name = rawName.dropLast(1).joinToString(" ")
            val rank = rawName.last().toIntOrNull() ?: 1

            val preReqs = row.selectTd(1)?.text()?.toRankMap() ?: mapOf()

            val skills = row.selectTd(2)?.text()?.toRankMap() ?: mapOf()
            val description = row.selectTdClean(4) ?: ""


//            ResearchProject(name, category, rank, description)
            null
        }
    }
    return listOf()
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
