package wikiScraper

import PlanetWikiData
import jsonMapper
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import org.jsoup.nodes.Document
import java.io.File

private const val chunkSize = 5

fun main() {
    val output = File("raw-data/planet-wiki-data.json")
    val existing = (if (output.exists()) {
        jsonMapper.decodeFromString<Map<String, PlanetWikiData>>(output.readText()).toMutableMap()
    } else mapOf()).toMutableMap()

    runBlocking {
        val api = authedApi()
        getPlanetNames(api)
            .also { println("Reading ${it.size} Planets") }
            .chunked(chunkSize)
            .flatMap { chunk ->
                chunk.map { id ->
                    async {
                        try {
                            api.fetch(id, "planets").let { id to it }
                        } catch (e: Exception) {
                            null
                        }
                    }
                }.awaitAll().filterNotNull()
            }
            .mapNotNull { (id, data) -> parseWikiData(id, data) }
            .forEach { existing[it.name] = it }
        api.close()
    }

    output.writeText(jsonMapper.encodeToString(existing))
}

private suspend fun getPlanetNames(api: WikiApi): List<String> {
    val doc = api.fetch("Starfield:Star_Systems", "planets")
    return doc.select("table").first()!!.select("tr").drop(1).flatMap { row ->
        val planets = row.selectTd(2)?.getUrlIds() ?: emptyList()
        val moons = row.selectTd(3)?.getUrlIds() ?: emptyList()
        planets + moons
    }
}

private fun parseWikiData(id: String, document: Document): PlanetWikiData? {
    return try {
        attemptParseWikiData(id, document)
    } catch (e: Exception) {
        println("Unable to get data for $id")
        null
    }
}

private fun attemptParseWikiData(id: String, document: Document): PlanetWikiData {
    val data: Map<String, List<String>> = document.select(".infobox").first().rowsToMap()

    val traits = document.select(".infobox").select("tr")
        .firstOrNull { it.selectFirst("th")?.text()?.trim() == "Traits" }?.select("td")?.flatMap { td ->
            td.select("span").map { it.text() }
        } ?: listOf()


    val resources = data["Resources"]?.flatMap { it.replace("  ", " ").split(" ") } ?: listOf()
    val moons = document.select("h2").firstOrNull { it.text().contains("Moons") }?.nextElementSibling()?.selectColumn(1)?.mapNotNull { it.getUrlId() } ?: emptyList()

    return PlanetWikiData(
        id,
        id.urlIdToName(),
        data["Type"]?.first() ?: "",
        data["Temperature"]?.first() ?: "",
        data["Atmosphere"]?.first() ?: "",
        data["Magnetosphere"]?.first() ?: "",
        data["Fauna"]?.first() ?: "",
        data["Flora"]?.first() ?: "",
        data["Water"]?.first() ?: "",
        resources,
        traits,
        moons,
    )
}
