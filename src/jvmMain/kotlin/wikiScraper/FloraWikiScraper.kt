package wikiScraper

import FloraWikiData
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import java.io.File
import java.lang.IllegalStateException

fun main() {
    val options = ScraperOptions()
    val urlFile = File("raw-data/flora-pages.txt")
    if (!urlFile.exists()) urlFile.writeText("")
    fetchPagesIfEmpty(urlFile, listOf("https://starfieldwiki.net/wiki/Starfield:Flora"), options.onlyOne)
    val output = File("src/jsMain/resources/flora-wiki-data.json")

    println("Reading Flora")
    readFromUrls(urlFile, output, ::parseFlora, options)
}

private fun parseFlora(page: Document): List<FloraWikiData> {
    val allTables = page.select(".wikitable")
    val singleTable = allTables.firstOrNull { it.hasClass("infobox") }!!
    val variantTables = allTables.toMutableList().also { it.remove(singleTable) }
    val species = page.baseUri().let { it.substring(it.lastIndexOf(":")+1) }.replace("_", " ").replace("%27s", "'").replace(".html", "")
    return variantTables.flatMap { parseTable(it, species) }
}

private fun parseTable(table: Element, species: String): List<FloraWikiData> {
    return table.select("tr").drop(1).map { row ->

        val planet = row.selectTdClean(0)
        val biomes = row.selectTdClean(1)?.split(",")?.map { it.trim() } ?: listOf()
        val resource = row.selectTdClean(2) ?: "None"
        val production = row.selectTdClean(3)

        val other: Map<String, String> = listOfNotNull(
            production?.let { "Production" to it },
        ).toMap()

        val name = "$species ($planet)"

        FloraWikiData(name, planet, biomes, resource, other)
    }
}

