package wikiScraper

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
    val output = File("raw-data/star-wiki-data.json")
    val existing = (if (output.exists()) {
        jsonMapper.decodeFromString<Map<String, StarWikiData>>(output.readText()).toMutableMap()
    } else mapOf()).toMutableMap()

    runBlocking {
        val api = authedApi()
        getStarNames(api)
            .also { println("Reading ${it.size} Stars") }
            .chunked(chunkSize)
            .flatMap { chunk ->
                chunk.map { id ->
                    async {
                        try {
                            api.fetch(id, "stars").let { id to it }
                        } catch (e: Exception) {
                            null
                        }
                    }
                }.awaitAll().filterNotNull()
            }
            .mapNotNull { (id, data) -> parseWikiData(id, data) }
            .forEach { existing[it.id] = it }
        api.close()
    }

    output.writeText(jsonMapper.encodeToString(existing))
}

private suspend fun getStarNames(api: WikiApi): List<String> {
    val doc = api.fetch("Starfield:Star_Systems", "planets")
    return doc.select("table").first()!!.select("tr").drop(1).mapNotNull { row ->
        row.selectTd(0)?.getUrlId()
    }
}

private fun parseWikiData(id: String, document: Document): StarWikiData? {
    return try {
        attemptParseWikiData(id, document)
    } catch (e: Exception) {
        println("Unable to get data for $id")
        null
    }
}

private fun attemptParseWikiData(id: String, document: Document): StarWikiData {
    val data: Map<String, List<String>> = document.select(".infobox").first().rowsToMap()

    val planets = document.select("table").filter { !it.hasClass("infobox") && !it.hasClass("navbox") }.map { el -> el.selectColumn(1).mapNotNull { it.getUrlId() }}.flatten()

    val cleanId = id.urlIdToId().replace("_System", "").trim()
    return StarWikiData(
        id,
        cleanId,
        data["Catalogue ID"]?.first() ?: "",
        cleanId.urlIdToName(),
        data["Level"]?.first()?.toIntOrNull() ?: 0,
        data["Spectral Class"]?.first() ?: "",
        data["Temperature"]?.first() ?: "",
        data["Mass"]?.first() ?: "",
        data["Radius"]?.first()?.toFloatOrNull() ?: 0f,
        data["Magnitude"]?.first()?.toFloatOrNull() ?: 0f,
        planets,
    )
}
