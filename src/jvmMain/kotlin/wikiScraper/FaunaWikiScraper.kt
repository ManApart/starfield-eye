package wikiScraper

import FaunaWikiData
import jsonMapper
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import toTemperament
import java.io.File
import java.lang.IllegalStateException

private const val onlyOne = false
private const val useCache = true
private const val start = 0
private const val limit = 0
private const val chunkSize = 100

fun main() {
    val faunaUrlFile = File("raw-data/fauna-pages.txt")
    if (!faunaUrlFile.exists()) faunaUrlFile.writeText("")
    fetchFaunaPagesIfEmpty(faunaUrlFile)

    val output = File("src/jsMain/resources/fauna-wiki-data.json")
    val existing = mutableMapOf<String, FaunaWikiData>()

    println("Reading Fauna")
    faunaUrlFile.readLines()
        .also { println("Found a total of ${it.size} urls") }
//        .filter { it.contains("Beetle_Filterer") }
        .let { if (onlyOne) it.take(1) else it.drop(start) }
        .let { if (limit > 0) it.take(limit) else it }
        .also { println("Crawling ${it.size} urls") }
        .chunked(chunkSize).flatMap { chunk ->
            println("Processing next $chunkSize, starting with ${chunk.first()}")
            chunk.flatMap { parseFauna(fetch(it, useCache)) }
        }
        .forEach { fauna ->
            existing[fauna.name] = fauna
        }

    output.writeText(jsonMapper.encodeToString(existing.values))
}

private fun fetchFaunaPagesIfEmpty(fauna: File) {
    if (fauna.readLines().isEmpty()) {
        val urls = crawl("https://starfieldwiki.net/wiki/Category:Starfield-Creatures-All", onlyOne).toSet()
        fauna.writeText(urls.joinToString("\n"))
    }
}

private fun fetch(url: String, useCache: Boolean): Document {
    return if (useCache) {
        val file = File("raw-data/cache/${url.substring(url.lastIndexOf("/"))}.html").also { it.parentFile.mkdirs() }
        if (!file.exists()) {
            file.writeText(getPage(url)!!)
        }
        Jsoup.parse(file)
    } else Jsoup.connect(url).get()
}

private fun parseFauna(page: Document): List<FaunaWikiData> {
    return try {
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
    } catch (e: Exception) {
        println("Failed to parse ${page.baseUri()}")
        listOf()
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


private fun parseName(box: Element): String {
    return if (box.text().contains(")")) {
        box.text().let { it.substring(0, it.indexOf(")") + 1) }.trim()
    } else {
        box.text()
    }
}

private fun parsePlanet(box: Element): String {
    return if (box.select("a").isNotEmpty()) {
        box.select("a").first()!!.text().trim()
    } else {
        box.text()
    }
}