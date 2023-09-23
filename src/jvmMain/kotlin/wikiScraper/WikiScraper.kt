package wikiScraper

import jsonMapper
import kotlinx.serialization.encodeToString
import org.jsoup.Jsoup
import java.io.File

fun main() {
    val planets = listOf("Mars")
        .map { getPage("https://starfieldwiki.net/wiki/Starfield:$it") }
        .map { parseWikiData(it) }
    File("raw-data/wiki-data.json").writeText(jsonMapper.encodeToString(planets))
}

fun parseWikiData(pageString: String): WikiData {
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