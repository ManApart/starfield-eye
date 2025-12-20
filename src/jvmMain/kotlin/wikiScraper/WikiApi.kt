package wikiScraper

import BotCreds
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.jetty.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.cookies.*
import io.ktor.client.request.*
import io.ktor.client.request.forms.*
import io.ktor.client.statement.bodyAsText
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import jsonMapper
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import readConfig
import red

fun authedApi(): WikiApi {
    val creds = readConfig().botCreds
    return WikiApi(creds)
}

class WikiApi(val creds: BotCreds) {
    private var isAuthed = false
    private val mutex = Mutex()
    private var cookie = creds.cookie.replace("\n", "")
    private val sessionKey = creds.sessionKey
    private val client = HttpClient(Jetty) {
        install(ContentNegotiation) { json(jsonMapper) }
    }

    fun close() = client.close()

    private suspend fun authIfNeeded() {
        mutex.withLock {
            if (!isAuthed) auth(sessionKey)
        }
    }

    suspend fun auth(sessionKey: String? = null) {
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
            val responseCookie = authResp.setCookie()["sfwiki_BPsession"] ?: authResp.setCookie()["sfwiki_session"]
            if (responseCookie == null) {
                println(red(authResp.bodyAsText()))
            }

            responseCookie!!.value
        }
        cookie += "; sfwiki_BPsession=${session}"
        println("Updated cookie with session $session")
        isAuthed = true
    }

    suspend fun getPage(pageId: String): String? {
        authIfNeeded()
        println("Fetching $pageId")
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
