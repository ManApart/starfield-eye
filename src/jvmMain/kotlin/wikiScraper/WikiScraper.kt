package wikiScraper

import jsonMapper
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import org.jsoup.Jsoup
import toPlanet
import java.io.File

private const val onlyOne = false
private const val start = 0
private const val limit = 0
private const val chunkSize = 100

fun main() {
    val output = File("raw-data/wiki-data.json")
    val existing = (if (output.exists()) {
        jsonMapper.decodeFromString<Map<String, WikiData>>(output.readText()).toMutableMap()
    } else mapOf()).toMutableMap()

    getPlanetNames()
        .chunked(chunkSize)
        .flatMap { chunk ->
            chunk.mapNotNull { name -> getPage("https://starfieldwiki.net/wiki/Starfield:$name")?.let { name to it } }.also {
                println("Downloaded ${it.size}")
            }
        }
        .mapNotNull { (name, data) -> parseWikiData(name, data) }
        .forEach { existing[it.name] = it }

    output.writeText(jsonMapper.encodeToString(existing))
}

private fun getPlanetNames(): List<String> {
    return if (onlyOne) listOf("Earth") else {
        File("./raw-data/galaxy.csv")
            .readLines().drop(2).map { it.toPlanet() }
            .map { it.name.replace(" ", "_") }
            .drop(start)
            .let { if (limit == 0) it else it.take(limit) }
    }
}

private fun parseWikiData(name: String, pageString: String): WikiData? {
    return try {
        attemptParseWikiData(name, pageString)
    } catch (e: Exception) {
        println("Unable to get data for $name")
        null
    }
}

private fun attemptParseWikiData(name: String, pageString: String): WikiData {
    val data: Map<String, List<String>> = Jsoup.parse(pageString).select(".infobox").select("tr").mapNotNull { row ->
        val title = row.selectFirst("th")?.text()?.trim()
        val cols = row.select("td")
        if (title == null || cols.isEmpty()) null else {
            val data = cols.map { it.text().replace("◆", "").trim() }
            title to data
        }
    }.toMap()
    val resources = data["Resources"]?.flatMap { it.replace("  ", " ").split(" ") } ?: listOf()
    return WikiData(
        name.replace("_", " "),
        data["Type"]?.first() ?: "",
        data["Temperature"]?.first() ?: "",
        data["Atmosphere"]?.first() ?: "",
        data["Magnetosphere"]?.first() ?: "",
        data["Fauna"]?.first() ?: "",
        data["Flora"]?.first() ?: "",
        data["Water"]?.first() ?: "",
        resources,
        data["Traits"] ?: listOf(),
    )
}