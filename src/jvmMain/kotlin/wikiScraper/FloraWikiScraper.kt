package wikiScraper

import FloraWikiData
import org.jsoup.nodes.Document
import java.io.File

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
//    return try {
//        val allTables = page.select(".wikitable")
//        val singleTable = allTables.firstOrNull { it.hasClass("infobox") }
//        val variantTables = allTables.toMutableList().also { it.remove(singleTable) }
//
//        return when {
//            singleTable == null && variantTables.isEmpty() -> {
//                println("Skipping ${page.baseUri()}")
//                listOf()
//            }
//
//            variantTables.isEmpty() -> listOf(parseTable(singleTable!!))
//            else -> variantTables.map { parseTable(it) }
//        }
//    } catch (e: Exception) {
//        println("Failed to parse ${page.baseUri()}")
//        listOf()
//    }
    return listOf()
}
//
//private fun parseTable(table: Element): FaunaWikiData {
//    val name = parseName(table.select("th").first()!!)
//    val planet = table.selectHeaderClean("Planet") ?: parsePlanet(table.select("th").first()!!)
//    if (name == planet) throw IllegalStateException("Non-fauna table detected")
//    val abilities = table.selectHeaderClean("Abilities")?.split(",") ?: emptyList()
//    val temperament = table.selectHeaderClean("Temperament").toTemperament()
//    val biomes = table.selectHeader("Biomes")?.select("li")?.map { it.text() } ?: listOf()
//    val resource = table.selectHeaderClean("Resource") ?: "None"
//
//    val other: Map<String, String> = listOfNotNull(
//        table.tablePair("Harvestable"),
//        table.tablePair("Domesticable"),
//        table.tablePair("Predation"),
//        table.tablePair("Weaknesses"),
//        table.tablePair("Resistances"),
//        table.tablePair("Behavior"),
//        table.tablePair("Difficulty"),
//        table.tablePair("Health Multiplier"),
//        table.tablePair("Difficulty"),
//        table.tablePair("Size"),
//        table.tablePair("Diet"),
//        table.tablePair("Schedule"),
//        table.tablePair("Combat Style"),
//    ).toMap()
//    return FaunaWikiData(name, temperament, planet, biomes, resource, abilities, other)
//}

