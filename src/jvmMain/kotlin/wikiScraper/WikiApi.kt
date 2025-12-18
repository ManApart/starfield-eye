package wikiScraper

import BotCreds
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.jetty.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.cookies.*
import io.ktor.client.request.*
import io.ktor.client.request.forms.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import jsonMapper
import readConfig

suspend fun authedApi(): WikiApi {
    val creds = readConfig().botCreds
    return WikiApi(creds).also { it.auth(creds.sessionKey) }
}

class WikiApi(val creds: BotCreds) {
    private var cookie = creds.cookie.replace("\n", "")
    private val client = HttpClient(Jetty) {
        install(ContentNegotiation) { json(jsonMapper) }
    }

    fun close() = client.close()

    suspend fun auth(sessionKey: String?) {
        val session = if (sessionKey != null) sessionKey else {
            println("Fetching Session Key")
            val token = client.get("https://starfieldwiki.net/w/api.php?action=query&meta=tokens&type=login&format=json") {
                userAgent()
                cookie()
            }.body<TokenResp>().query.tokens.logintoken

            val authResp = client.post("https://starfieldwiki.net/w/api.php?action=login&format=json") {
                userAgent()
                cookie()
                setBody(FormDataContent(Parameters.build {
                    append("lgname", creds.name)
                    append("lgpassword", creds.pass)
                    append("lgtoken", token)
                    append("action", "login")
                    append("format", "json")
                }))
            }

            authResp.setCookie()["sfwiki_BPsession"]!!.value
        }
        cookie += "; sfwiki_BPsession=${session}"
        println("Updated cookie with session $session")
    }

    suspend fun getPage(pageId: String): String? {
        return try {
            client.get("https://starfieldwiki.net/w/api.php?action=parse&page=$pageId&format=json") {
                userAgent()
                cookie()
            }.body<PageResult>().parse.text.text
        } catch (e: Exception) {
            println("Unable to fetch $pageId")
            null
        }
    }

    private fun HttpMessageBuilder.cookie() = header("Cookie", cookie)
    private fun HttpMessageBuilder.userAgent() = header("User-Agent", "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/143.0.0.0 Safari/537.36")
}
