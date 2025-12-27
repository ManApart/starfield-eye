package wikiScraper

import PlanetWikiData
import StarWikiData
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
    val starWikiData = jsonMapper.decodeFromString<Map<String, StarWikiData>>(File("raw-data/star-wiki-data.json").readText()).values.toList()

    getAllPlanets(starWikiData)
        .mapNotNull { (id, data) -> parseWikiData(id, data) }
        .forEach { existing[it.id] = it }

    output.writeText(jsonMapper.encodeToString(existing))
}

fun getAllPlanets(starWikiData: List<StarWikiData>): Map<String, Document> {
    return runBlocking {
        val api = authedApi()
        starWikiData.flatMap { it.planetIds }
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
            }.toMap().also { api.close() }
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

private fun attemptParseWikiData(urlId: String, document: Document): PlanetWikiData {
    val data: Map<String, List<String>> = document.select(".infobox").first().rowsToMap()

    val traits = document.select(".infobox").select("tr")
        .firstOrNull { it.selectFirst("th")?.text()?.trim() == "Traits" }?.select("td")?.flatMap { td ->
            td.select("span").map { it.text() }
        } ?: listOf()


    val resources = data["Resources"]?.flatMap { it.replace("  ", " ").split(" ") } ?: listOf()
    val moons = document.select("h2").firstOrNull { it.text().contains("Moons") }?.nextElementSibling()?.selectColumn(1)?.mapNotNull { it.getUrlId() }?.map { it.urlIdToId() } ?: emptyList()

    return PlanetWikiData(
        urlId.urlIdToId(),
        urlId.urlIdToName(),
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
