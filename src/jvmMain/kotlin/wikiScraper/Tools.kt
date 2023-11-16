package wikiScraper

import WikiData
import jsonMapper
import kotlinx.serialization.encodeToString
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import java.io.File
import java.net.URL
import java.net.URLConnection
import java.nio.charset.StandardCharsets
import java.util.*

data class ScraperOptions(
    val onlyOne: Boolean = false,
    val useCache: Boolean = true,
    val start: Int = 0,
    val limit: Int = 0,
    val chunkSize: Int = 100
)

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

fun fetchPagesIfEmpty(urlFile: File, baseUrls: List<String>, onlyOne: Boolean) {
    if (urlFile.readLines().isEmpty()) {
        val urls = baseUrls.flatMap { crawl(it, onlyOne) }.toSet()
        urlFile.writeText(urls.joinToString("\n"))
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

inline fun <reified T : WikiData> readFromUrls(
    urlFile: File,
    output: File,
    parse: (Document) -> List<T>,
    options: ScraperOptions,
) {
    val existing = mutableMapOf<String, T>()
    urlFile.readLines()
        .also { println("Found a total of ${it.size} urls") }
//        .filter { it.contains("Seahag") }
        .let { if (options.onlyOne) it.take(1) else it.drop(options.start) }
        .let { if (options.limit > 0) it.take(options.limit) else it }
        .also { println("Crawling ${it.size} urls") }
        .chunked(options.chunkSize).flatMap { chunk ->
            println("Processing next ${options.chunkSize}, starting with ${chunk.first()}")
            chunk.flatMap {
                try {
                parse(fetch(it, options.useCache))
                } catch (e: Exception){
                    println("Unable to parse $it")
                    emptyList()
                }
            }
        }
        .forEach { existing[it.name] = it }

    output.writeText(jsonMapper.encodeToString(existing.values))
}

fun fetch(url: String, useCache: Boolean): Document {
    return if (useCache) {
        val file = File("raw-data/cache/${url.substring(url.lastIndexOf("/"))}.html").also { it.parentFile.mkdirs() }
        if (!file.exists()) {
            file.writeText(getPage(url)!!)
        }
        Jsoup.parse(file)
    } else Jsoup.connect(url).get()
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
        val i = rows.indexOf(row) + 1
        rows[i]
    }
}


fun parseName(box: Element): String {
    return if (box.text().contains(")")) {
        box.text().let { it.substring(0, it.indexOf(")") + 1) }.trim()
    } else {
        box.text()
    }
}

fun parsePlanet(box: Element): String {
    return if (box.select("a").isNotEmpty()) {
        box.select("a").first()!!.text().trim()
    } else {
        box.text()
    }
}