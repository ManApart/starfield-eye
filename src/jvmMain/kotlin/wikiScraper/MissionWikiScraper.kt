package wikiScraper

import MissionWikiData
import kotlinx.coroutines.runBlocking
import org.jsoup.nodes.Document
import toMissionType
import java.io.File

fun main() {
    val options = ScraperOptions("missions")
    val pageFile = File("raw-data/mission-pages.txt")
    if (!pageFile.exists()) pageFile.writeText("")
    val missionBaseUrls = listOf(
        "https://starfieldwiki.net/wiki/Category:Starfield-Missions",
        "https://starfieldwiki.net/wiki/Starfield:Missions",
        "https://starfieldwiki.net/wiki/Starfield:Main_Mission",
        "https://starfieldwiki.net/wiki/Starfield:Crimson_Fleet_Missions",
        "https://starfieldwiki.net/wiki/Starfield:Freestar_Rangers_Missions",
        "https://starfieldwiki.net/wiki/Starfield:Ryujin_Industries_Missions",
        "https://starfieldwiki.net/wiki/Starfield:UC_Vanguard_Missions"
    )
    runBlocking {
        val api = authedApi()
        api.fetchPagesIfEmpty(pageFile, missionBaseUrls, options.onlyOne)

        val output = File("src/jsMain/resources/mission-wiki-data.json")

        println("Reading Missions")
        api.readFromUrls(pageFile, output, ::parseMission, options)
    }
}

private fun parseMission(url: String, page: Document): List<MissionWikiData> {
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
