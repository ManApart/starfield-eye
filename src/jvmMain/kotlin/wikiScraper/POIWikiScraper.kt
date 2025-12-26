package wikiScraper

import Galaxy
import PlanetWikiData
import PointOfInterest
import jsonMapper
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import toPOIType
import java.io.File

fun main() {
    val output = File("src/jsMain/resources/poi-wiki-data.json")

    val systems = jsonMapper.decodeFromString<Galaxy>(File("src/jsMain/resources/data.json").readText()).systems.values

    val docs = getAllPlanets()
    val poi = systems.flatMap { sys ->
        sys.planets.values.mapNotNull { planet ->
            docs["Starfield:" + planet.id]?.let { doc ->
                parsePOI(sys.star.id, planet.id, doc)
            } ?: null.also { println("Unable to find doc for ${sys.star.id}: ${planet.id}") }
        }.flatten()
    }.sortedBy { it.id }

    output.writeText(jsonMapper.encodeToString(poi))
}

private fun parsePOI(starId: String, planetId: String, page: Document): List<PointOfInterest> {
    return page.tablesWithHeaders("Place", "Type", "Description").flatMap { tbl ->
        tbl.tableWithHeaderRowToMap().map { parsePOIRow(starId, planetId, it) }
    }
}

private fun parsePOIRow(starId: String, planetId: String, row: Map<String, Element>): PointOfInterest{
    return PointOfInterest(
        row["Place"]?.select("a")?.lastOrNull()?.attr("href")?.replace("/wiki/", "")?.urlIdToId() ?: "",
        row["Place"]?.text()?.replace(" ", "") ?: "",
        row["Description"]?.text() ?: "",
        row["Type"]?.text()?.toPOIType() ?: POIType.OTHER,
        starId,
        planetId
    )
}
