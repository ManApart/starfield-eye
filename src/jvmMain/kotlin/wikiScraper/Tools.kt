package wikiScraper

import org.jsoup.Jsoup
import org.jsoup.nodes.Element
import java.net.URL
import java.net.URLConnection
import java.nio.charset.StandardCharsets
import java.util.*

fun getPage(url: String, headers: Map<String, String> = mapOf()): String? {
    val connection: URLConnection = URL(url).openConnection()
    with(connection) {
        //fake we're a browser for https
        setRequestProperty(
            "User-Agent",
            "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/106.0.0.0 Safari/537.36"
        )
        headers.entries.forEach { (key, value) ->
            setRequestProperty(key, value)
        }
        connect()
    }
    Scanner(
        connection.getInputStream(),
        StandardCharsets.UTF_8.toString()
    ).use { scanner ->
        scanner.useDelimiter("\\A")
        return if (scanner.hasNext()) scanner.next() else null.also { println("Unable to fetch $url") }
    }
}

fun crawl(baseUrl: String, onlyOne: Boolean): List<String> {
    val cleanBase = if (baseUrl.startsWith("/")) "https://starfieldwiki.net$baseUrl" else baseUrl
    println("Crawling $cleanBase")
    val page = Jsoup.connect(cleanBase).get()
    val urls = page.select("li")
        .flatMap { li ->
            li.select("a").mapNotNull { it.attr("href") }
        }
        .map { if (it.startsWith("/")) "https://starfieldwiki.net$it" else it }
        .filter { it.startsWith("https://starfieldwiki.net/wiki/Starfield:") }

    val nextUrl =
        page.select("a").firstOrNull { it.text() == "next page" }?.attr("href")?.let { "https://starfieldwiki.net$it" }
    val nextUrls = if (onlyOne) listOf() else urls.filter { it.contains("Category") } + listOfNotNull(nextUrl)

    return urls + nextUrls.flatMap { crawl(it, onlyOne) }
}

fun Element?.cleanText(): String? {
    return this?.text()?.replace("(?)", "")?.ifBlank { null }
}

fun Element.select(row: Int, cell: Int): Element? {
    return select("tr")[row].select("td")[cell]
}

fun Element.tablePair(headerText: String): Pair<String, String>? {
    return selectHeaderClean(headerText)?.let { headerText to it }
}

fun Element.selectHeaderClean(headerText: String): String? {
    return selectHeader(headerText).cleanText()
}
fun Element.selectHeader(headerText: String): Element? {
    return selectRight(headerText) ?: selectBelow(headerText)
}

fun Element.selectRightClean(headerText: String): String? {
    return selectRight(headerText).cleanText()
}

fun Element.selectRight(headerText: String): Element? {
    return select("tr").firstOrNull { row -> row.select("th").any { it.text() == headerText } }?.let { row ->
        val headers = row.select("th")
        val right = headers.first { it.text() == headerText }.let { headers.indexOf(it) }
        select("td")[right]
    }
}

fun Element.selectBelowClean(headerText: String): String? {
    return selectBelow(headerText).cleanText()
}

fun Element.selectBelow(headerText: String): Element? {
    val rows = select("tr")
    return rows.firstOrNull { row -> row.select("th").any { it.text() == headerText } }?.let { row ->
        val i = rows.indexOf(row)+1
        rows[i]
    }
}