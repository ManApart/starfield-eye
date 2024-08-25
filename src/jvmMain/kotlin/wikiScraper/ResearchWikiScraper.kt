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


    return page.select(".wikitable").flatMapIndexed { categoryIndex: Int, table: Element? ->
        val category = ResearchCategory.entries[categoryIndex]
        table!!.select("tr").drop(1).map { row ->
            val colCount = row.select("td").count()
            val rawName = row.selectTdClean(0)?.split(" ") ?: listOf()
            val name = rawName.dropLast(1).joinToString(" ")
            val rank = rawName.last().toIntOrNull() ?: 1
            val preReqs = if (colCount == 5) row.selectTd(1)?.text()?.toRankMap() ?: mapOf() else {
                if (rank > 1) {
                    mapOf(name to rank - 1)
                } else mapOf()
            }
            val perkRow = if (colCount == 5) 2 else 1
            val perks = row.selectTd(perkRow)?.text()?.toRankMap() ?: mapOf()
            val description = row.selectTdClean(4) ?: ""
            val matRow = if (colCount == 5) 3 else 2
            val materials = row.selectTd(matRow).parseMaterials()

            ResearchProject(name, category, rank, description, preReqs, perks, materials)
        }
    }
}

private fun String.toRankMap(): Map<String, Int> {
    val parts = split(Regex("(?<=\\D)(?=\\d)")).map {
        it.replace("Rank", "")
            .replace("None", "")
            .replace("and", "")
            .replace(",", "")
            .replace("Requires ", "")
            .replace("no trained skills", "")
            .replace(" at level ", "")
            .trim()
    }.flatMap { line ->
        val lineParts = line.split(" ")
        if (lineParts.first().toIntOrNull() != null) {
            listOf(lineParts.first(), lineParts.drop(1).joinToString(" "))
        } else listOf(line)
    }.filter { it.isNotEmpty() }

    return if (parts.size >= 2) {
        parts.chunked(2).associate { it[0] to (it[1].toIntOrNull() ?: 1) }
    } else mapOf()
}

private fun Element?.parseMaterials(): List<Material> {
    if (this == null) return listOf()

    val rawAmounts1 = this.html().split("<br>").map { it.substring(it.indexOf("x") + 1).toIntOrNull() ?: 1 }
    val rawAmounts2 = this.text().split(" ", ",").mapNotNull { it.toIntOrNull() }
    val amounts = if (rawAmounts1.size > rawAmounts2.size) rawAmounts1 else rawAmounts2

    return select("a").mapIndexed { i, a ->
        Material(a.text(), amounts[i], a.attr("href"))
    }
}
