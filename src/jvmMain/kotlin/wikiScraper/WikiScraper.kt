package wikiScraper

import WikiData
import jsonMapper
import kotlinx.serialization.encodeToString
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import java.io.File

const val wikiUrl = "https://starfieldwiki.net/wiki/"
const val wikiBase = "https://starfieldwiki.net"

data class ScraperOptions(
    val cacheDir: String,
    val onlyOne: Boolean = false,
    val start: Int = 0,
    val limit: Int = 0,
    val chunkSize: Int = 100,
)

suspend fun WikiApi.fetch(pageId: String, cacheDir: String, cacheOnly: Boolean = false): Document {
    return Jsoup.parse(fetchPage(pageId, cacheDir, cacheOnly))
}

suspend fun WikiApi.fetchPage(pageId: String, cacheDir: String, cacheOnly: Boolean = false): String {
    val file = File("raw-data/cache/$cacheDir/${pageId}.html").also { it.parentFile.mkdirs() }
    if (!file.exists() && !cacheOnly) {
        getPage(pageId)?.let { file.writeText(it) }
    }
    return if (file.exists()) file.readText() else ""
}

suspend fun WikiApi.fetchPagesIfEmpty(pageFile: File, basePageIds: List<String>, onlyOne: Boolean) {
    if (pageFile.readLines().isEmpty()) {
        val pageIds = basePageIds.flatMap { crawl(it, onlyOne) }.toSet()
        pageFile.writeText(pageIds.joinToString("\n"))
    }
}

suspend fun WikiApi.crawl(baseUrl: String, onlyOne: Boolean): List<String> {
    val cleanBase = if (baseUrl.startsWith("/")) "$wikiBase$baseUrl" else baseUrl
    println("Crawling $cleanBase")
    val page = getPage(cleanBase.replace(wikiUrl, ""))!!.let { Jsoup.parse(it) }
    val urls = page.select("li")
        .flatMap { li ->
            li.select("a").mapNotNull { it.attr("href") }
        }
        .map { if (it.startsWith("/")) "$wikiBase$it" else it }
        .filter { it.startsWith("${wikiUrl}Starfield:") }

    val nextUrl =
        page.select("a").firstOrNull { it.text() == "next page" }?.attr("href")?.let { "$wikiBase$it" }
    val nextUrls = if (onlyOne) listOf() else urls.filter { it.contains("Category") } + listOfNotNull(nextUrl)

    return urls.map { it.replace(wikiUrl, "") } + nextUrls.flatMap { crawl(it, onlyOne) }
}

suspend inline fun <reified T : WikiData> WikiApi.readFromUrls(
    pageFile: File,
    output: File,
    parse: (String, Document) -> List<T>,
    options: ScraperOptions,
) {
    val data = pageFile.readLines()
        .also { println("Found a total of ${it.size} page ids") }
//        .filter { it.contains("Seahag") }
        .let { if (options.onlyOne) it.take(1) else it.drop(options.start) }
        .let { if (options.limit > 0) it.take(options.limit) else it }
        .also { println("Crawling ${it.size} pages") }
        .chunked(options.chunkSize).flatMap { chunk ->
            println("Processing next ${options.chunkSize}, starting with ${chunk.first()}")
            chunk.flatMap {
                try {
                    parse(it, fetch(it, options.cacheDir))
                } catch (e: Exception) {
                    println("Unable to parse $it due to $e")
                    emptyList()
                }
            }
        }

    output.writeText(jsonMapper.encodeToString(data))
}
