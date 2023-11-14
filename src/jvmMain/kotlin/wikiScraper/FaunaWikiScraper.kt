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

private const val onlyOne = true
private const val useCache = true
private const val start = 0
private const val limit = 0
private const val chunkSize = 100

fun main() {
    val faunaUrlFile = File("raw-data/fauna-pages.txt")
    if (!faunaUrlFile.exists()) faunaUrlFile.writeText("")
    fetchFaunaPagesIfEmpty(faunaUrlFile)

    val output = File("src/jsMain/resources/fauna-wiki-data.json")
    val existing = (if (output.exists()) {
        jsonMapper.decodeFromString<List<FaunaWikiData>>(output.readText())
    } else listOf()).associateBy { it.name }.toMutableMap()

    println("Reading Fauna")
    faunaUrlFile.readLines()
        .also { println("Found a total of ${it.size} urls") }
        .filter { it.contains("Fingerface_Geophage") }
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
    val allTables = page.select(".wikitable")
    val singleTable = allTables.first { it.hasClass("infobox") }
    val variantTables = allTables.toMutableList().also { it.remove(singleTable) }

    return if (variantTables.isEmpty()) {
        parseSingleTable(singleTable)
    } else parseVariants(singleTable, variantTables)
}

private fun parseSingleTable(singleTable: Element): List<FaunaWikiData> {
    return emptyList()
}

private fun parseVariants(singleTable: Element, variantTables: MutableList<Element>): List<FaunaWikiData> {
    val generalDifficulty = singleTable.select(3, 0)?.cleanText()
    val generalHealth = singleTable.select(5, 0)?.cleanText()
    val generalTemperament = singleTable.select(5, 1)?.cleanText()

    return variantTables.map {
        parseVariant(it, generalDifficulty, generalHealth, generalTemperament)
    }
}

private fun parseVariant(
    table: Element,
    generalDifficulty: String?,
    generalHealth: String?,
    generalTemperament: String?
): FaunaWikiData {
    val titleBox = table.select("th").first()!!
    val name = parseName(titleBox)
    val planet = parsePlanet(titleBox)
    val biomes = table.select(1, 0)?.select("li")?.map { it.text() } ?: listOf()
    val resource = table.select(2, 0)?.cleanText() ?: ""
    val temperament = (table.select(3, 0)?.text() ?: generalTemperament).toTemperament()
    val abilities = table.select(4, 0)?.cleanText()?.split(",") ?: emptyList()
    val harvestable = table.select(5, 0).cleanText()
    val domesticable = table.select(5, 1).cleanText()
    val resistances = table.select(6, 0).cleanText()
    val weaknesses = table.select(6, 1).cleanText()
    val behavior = table.select(7, 0).cleanText()
    val difficulty = table.select(8, 0).cleanText() ?: generalDifficulty
    val healthMultiplier = table.select(8, 1).cleanText() ?: generalHealth
    val size = table.select(9, 0).cleanText()
    val diet = table.select(9, 1).cleanText()
    val schedule = table.select(10, 0).cleanText()
    val combatStyle = table.select(10, 1).cleanText()
    val other: Map<String, String> = listOfNotNull(
        harvestable?.let { "Harvestable" to it },
        domesticable?.let { "Domesticable" to it },
        resistances?.let { "Resistances" to it },
        weaknesses?.let { "Weaknesses" to it },
        behavior?.let { "Behavior" to it },
        difficulty?.let { "Difficulty" to it },
        healthMultiplier?.let { "Health Multiplier" to it },
        size?.let { "Size" to it },
        diet?.let { "Diet" to it },
        schedule?.let { "Schedule" to it },
        combatStyle?.let { "Combat Style" to it },
    ).toMap()
    return FaunaWikiData(name, temperament, planet, biomes, resource, abilities, other)
}

private fun parseName(box: Element): String {
    return box.text().let { it.substring(0, it.indexOf(")")+1) }.trim()
}

private fun parsePlanet(box: Element): String {
    return box.select("a").first()!!.text().trim()
}