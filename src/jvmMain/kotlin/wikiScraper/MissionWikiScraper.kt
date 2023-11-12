package wikiScraper

import MissionWikiData
import jsonMapper
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import org.jsoup.Jsoup
import toMissionType
import java.io.File

private const val onlyOne = false
private const val start = 0
private const val limit = 0
private const val chunkSize = 100

fun main() {
    val missionUrlFile = File("raw-data/mission-pages.txt")
    if (!missionUrlFile.exists()) missionUrlFile.writeText("")
    fetchMissionPagesIfEmpty(missionUrlFile)

    val output = File("src/jsMain/resources/mission-wiki-data.json")
    val existing = (if (output.exists()) {
        jsonMapper.decodeFromString<List<MissionWikiData>>(output.readText())
    } else listOf()).associateBy { it.id }.toMutableMap()

    println("Reading Missions")
    missionUrlFile.readLines()
        .also { println("Found a total of ${it.size} urls") }
        .let { if (onlyOne) it.take(1) else it.drop(start) }
        .let { if (limit > 0) it.take(limit) else it}
        .also { println("Crawling ${it.size} urls") }
        .chunked(chunkSize).flatMap { chunk ->
            println("Processing next $chunkSize, starting with ${chunk.first()}")
            chunk.mapNotNull { fetchAndParseMission(it) }
        }
        .forEach {mission ->
            existing[mission.id] = mission
        }

    output.writeText(jsonMapper.encodeToString(existing.values))
}

private fun fetchMissionPagesIfEmpty(missions: File) {
    if (missions.readLines().isEmpty()) {
        val urls = listOf(
            "https://starfieldwiki.net/wiki/Category:Starfield-Missions",
            "https://starfieldwiki.net/wiki/Starfield:Missions",
            "https://starfieldwiki.net/wiki/Starfield:Main_Mission",
            "https://starfieldwiki.net/wiki/Starfield:Crimson_Fleet_Missions",
            "https://starfieldwiki.net/wiki/Starfield:Freestar_Rangers_Missions",
            "https://starfieldwiki.net/wiki/Starfield:Ryujin_Industries_Missions",
            "https://starfieldwiki.net/wiki/Starfield:UC_Vanguard_Missions",
        ).flatMap { crawl(it, onlyOne) }.toSet()

        missions.writeText(urls.joinToString("\n"))
    }
}

private fun fetchAndParseMission(url: String): MissionWikiData? {
    val page = Jsoup.connect(url).get()
    val name = page.select("#firstHeading").firstOrNull()?.text()?.replace("Starfield:", "")
    val id = page.select(".missionHeader").firstOrNull()
        ?.select("tr")?.firstOrNull { row ->
            row.select("th").firstOrNull()?.text() == "ID:"
        }?.select("td")?.firstOrNull()?.text()
    return if (name != null && id != null) {
        val type = id.toMissionType()
        MissionWikiData(name, id, type)
    } else null.also { println("Did not find info for $url") }
}