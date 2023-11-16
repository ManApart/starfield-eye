package wikiScraper

import MissionWikiData
import jsonMapper
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import toMissionType
import java.io.File

fun main() {
    val options = ScraperOptions()
    val urlFile = File("raw-data/mission-pages.txt")
    if (!urlFile.exists()) urlFile.writeText("")
    val missionBaseUrls = listOf(
        "https://starfieldwiki.net/wiki/Category:Starfield-Missions",
        "https://starfieldwiki.net/wiki/Starfield:Missions",
        "https://starfieldwiki.net/wiki/Starfield:Main_Mission",
        "https://starfieldwiki.net/wiki/Starfield:Crimson_Fleet_Missions",
        "https://starfieldwiki.net/wiki/Starfield:Freestar_Rangers_Missions",
        "https://starfieldwiki.net/wiki/Starfield:Ryujin_Industries_Missions",
        "https://starfieldwiki.net/wiki/Starfield:UC_Vanguard_Missions")
    fetchPagesIfEmpty(urlFile, missionBaseUrls, options.onlyOne)

    val output = File("src/jsMain/resources/mission-wiki-data.json")

    println("Reading Missions")
    readFromUrls(urlFile, output, ::parseMission, options)
}
private fun parseMission(page: Document): List<MissionWikiData> {
    val name = page.select("#firstHeading").firstOrNull()?.text()?.replace("Starfield:", "")
    val id = page.select(".missionHeader").firstOrNull()
        ?.select("tr")?.firstOrNull { row ->
            row.select("th").firstOrNull()?.text() == "ID:"
        }?.select("td")?.firstOrNull()?.text()
    return if (name != null && id != null) {
        val type = id.toMissionType()
        listOf(MissionWikiData(name, id, type))
    } else emptyList<MissionWikiData>().also { println("Did not find info for ${page.baseUri()}") }
}