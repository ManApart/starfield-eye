package wikiScraper

import BotCreds
import io.ktor.client.*
import io.ktor.client.call.body
import io.ktor.client.engine.jetty.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpMessageBuilder
import io.ktor.serialization.kotlinx.json.*
import jsonMapper
import kotlinx.serialization.Serializable

@Serializable
private data class TokenResp(val query: TokenRespQuery)

@Serializable
private data class TokenRespQuery(val tokens: TokenRespTokens)

@Serializable
private data class TokenRespTokens(val logintoken: String)


class WikiApi(val creds: BotCreds) {
    val client = HttpClient(Jetty) {
        install(ContentNegotiation) { json(jsonMapper) }
    }

    suspend fun auth() {
        val token = client.get("https://starfieldwiki.net/w/api.php?action=query&meta=tokens&type=login&format=json") {
            header("Cookie", creds.cookie)
            userAgent()
        }.body<TokenResp>().query.tokens.logintoken

        val authResp = client.post("https://starfieldwiki.net/w/api.php?action=login&format=json") {
            userAgent()
            header("Cookie", creds.cookie)
            parameter("lgname", creds.name)
            parameter("lgpassword", creds.pass)
            parameter("lgtoken", token)
        }
        println("Token $authResp")
    }

    private fun HttpMessageBuilder.userAgent() = header("User-Agent", "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/143.0.0.0 Safari/537.36")
}
