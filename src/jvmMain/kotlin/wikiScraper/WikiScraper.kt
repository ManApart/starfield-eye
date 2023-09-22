package wikiScraper

import org.jsoup.Jsoup

fun main() {
    val planets = listOf("Earth")
    planets
        .map { getPage("https://starfieldwiki.net/wiki/Starfield:$it") }
        .map { parseWikiData(it) }
}

fun parseWikiData(pageString: String): WikiData {
    val rows = Jsoup.parse(pageString).select(".infobox").select("tr").map { row ->
        val cols = row.select("td")
        if (cols.size != 2) null else {
            val title = cols.first()!!.text()
            val data = cols.last()!!.text()
            println("title: $title, $data")
            title to data
        }
    }
    println(rows)
    return WikiData("")
}