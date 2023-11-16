package wikiScraper

import FaunaWikiData
import jsonMapper
import kotlinx.serialization.encodeToString
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import toTemperament
import java.io.File
import java.lang.IllegalStateException

fun main() {
    val options = ScraperOptions()
    val urlFile = File("raw-data/fauna-pages.txt")
    if (!urlFile.exists()) urlFile.writeText("")
    fetchPagesIfEmpty(
        urlFile,
        listOf("https://starfieldwiki.net/wiki/Category:Starfield-Creatures-All"),
        options.onlyOne
    )

    val output = File("src/jsMain/resources/fauna-wiki-data.json")

    println("Reading Fauna")
    readFromUrls(urlFile, output, ::parseFauna, options)
}

private fun parseFauna(page: Document): List<FaunaWikiData> {
    val allTables = page.select(".wikitable")
    val singleTable = allTables.firstOrNull { it.hasClass("infobox") }
    val variantTables = allTables.toMutableList().also { it.remove(singleTable) }

    return when {
        singleTable == null && variantTables.isEmpty() -> {
            println("Skipping ${page.baseUri()}")
            listOf()
        }

        variantTables.isEmpty() -> listOf(parseTable(singleTable!!))
        else -> variantTables.map { parseTable(it) }
    }
}

private fun parseTable(table: Element): FaunaWikiData {
    val name = parseName(table.select("th").first()!!)
    val planet = table.selectHeaderClean("Planet") ?: parsePlanet(table.select("th").first()!!)
    if (name == planet) throw IllegalStateException("Non-fauna table detected")
    val abilities = table.selectHeaderClean("Abilities")?.split(",") ?: emptyList()
    val temperament = table.selectHeaderClean("Temperament").toTemperament()
    val biomes = table.selectHeader("Biomes")?.select("li")?.map { it.text() } ?: listOf()
    val resource = table.selectHeaderClean("Resource") ?: "None"

    val other: Map<String, String> = listOfNotNull(
        table.tablePair("Harvestable"),
        table.tablePair("Domesticable"),
        table.tablePair("Predation"),
        table.tablePair("Weaknesses"),
        table.tablePair("Resistances"),
        table.tablePair("Behavior"),
        table.tablePair("Difficulty"),
        table.tablePair("Health Multiplier"),
        table.tablePair("Difficulty"),
        table.tablePair("Size"),
        table.tablePair("Diet"),
        table.tablePair("Schedule"),
        table.tablePair("Combat Style"),
    ).toMap()
    return FaunaWikiData(name, temperament, planet, biomes, resource, abilities, other)
}