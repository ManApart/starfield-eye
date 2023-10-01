package wikiScraper

import jsonMapper
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import org.jsoup.Jsoup
import java.io.File

private const val onlyOne = true
private const val start = 0
private const val limit = 0
private const val chunkSize = 100

fun main() {
    val missionUrlFile = File("raw-data/mission-pages.txt")
    if (!missionUrlFile.exists()) missionUrlFile.writeText("")
    fetchMissionPagesIfEmpty(missionUrlFile)
    val missions = missionUrlFile.readLines()

    val output = File("raw-data/mission-wiki-data.json")
    val existing = (if (output.exists()) {
        jsonMapper.decodeFromString<Map<String, MissionWikiData>>(output.readText()).toMutableMap()
    } else mapOf()).toMutableMap()

    //For each mission url, create data

    output.writeText(jsonMapper.encodeToString(existing))
}

private fun fetchMissionPagesIfEmpty(missions: File) {
    if (missions.readLines().isEmpty()) {
        val urls = crawl("https://starfieldwiki.net/wiki/Category:Starfield-Missions")
        missions.writeText(urls.joinToString("\n"))
    }
    //https://starfieldwiki.net/w/index.php?title=Category:Starfield-Missions&pagefrom=Companion+-+Sam+Coe#mw-pages

    //https://starfieldwiki.net/wiki/Starfield:Alternating_Currents

    //https://starfieldwiki.net/w/index.php?title=Category:Starfield-Missions&pagefrom=Kill+the+PrimaryRef+on+TargetPlanetLocation+%282%29#mw-pages

    //https://starfieldwiki.net/wiki/Category:Starfield-Missions-Freestar_Rangers
}

private fun crawl(baseUrl: String): List<String> {
    val cleanBase = if (baseUrl.startsWith("/")) "https://starfieldwiki.net$baseUrl" else baseUrl
    println("Crawling $cleanBase")
    val page = Jsoup.connect(cleanBase).get()
    val urls = page.select("li")
        .flatMap { li -> li.select("a").mapNotNull { it.attr("href") } }
        .map { if (it.startsWith("/")) "https://starfieldwiki.net$it" else it }

    val nextUrl = page.select("a").firstOrNull { it.text() == "next page" }?.text()
    val nextUrls = if(onlyOne) listOf() else urls.filter { it.contains("Category") } + listOfNotNull(nextUrl)

    return urls + nextUrls.flatMap { crawl(it) }
}