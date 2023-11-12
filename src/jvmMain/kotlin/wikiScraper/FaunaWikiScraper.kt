package wikiScraper

import FaunaWikiData
import MissionWikiData
import jsonMapper
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import org.jsoup.Jsoup
import toMissionType
import java.io.File

private const val onlyOne = true
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
        .let { if (onlyOne) it.take(1) else it.drop(start) }
        .let { if (limit > 0) it.take(limit) else it}
        .also { println("Crawling ${it.size} urls") }
        .chunked(chunkSize).flatMap { chunk ->
            println("Processing next $chunkSize, starting with ${chunk.first()}")
            chunk.flatMap { fetchAndParseFauna(it) }
        }
        .forEach {fauna ->
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

private fun fetchAndParseFauna(url: String): List<FaunaWikiData> {
    val page = Jsoup.connect(url).get()
    val variantTables = page.select(".wikitable")
    val singleTable = page.select(".wikitable")
    return listOf()
}