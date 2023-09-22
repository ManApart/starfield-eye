package wikiScraper

import org.jsoup.Jsoup

fun main(){
    val planets = listOf("Earth")
    planets
        .map { getPage("https://starfieldwiki.net/wiki/Starfield:$it") }
        .map { parseWikiData(it) }
}

fun parseWikiData(pageString: String): WikiData {
    val rows = Jsoup.parse(pageString).select(".infobox").select("tr")

    println(pageString)
    return WikiData("")
}
