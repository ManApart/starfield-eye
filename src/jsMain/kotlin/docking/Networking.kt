package docking

import inMemoryStorage
import io.ktor.client.*
import io.ktor.client.fetch.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import kotlinx.coroutines.delay
import kotlin.js.Promise

private val client = HttpClient()

suspend fun postToConsole(body: String, timeout: Int = 0): String? {
    return try {
        with(inMemoryStorage.connectionSettings) {
            val fullTimeout = timeout + additionalTimeout
            val url = "http://$host:$port/console" + if (fullTimeout > 0) "?timeout=$fullTimeout" else ""
            client.post(url) {
                setBody(body)
            }.bodyAsText()
        }
    } catch (e: Error) {
        println(e)
        println(e.stackTraceToString())
        null
    }
}

suspend fun postToConsoleJs(body: String, timeout: Int = 0): List<String> {
    return try {
        with(inMemoryStorage.connectionSettings) {
            val fullTimeout = timeout + additionalTimeout
            val url = "http://$host:$port/console" + if (fullTimeout > 0) "?timeout=$fullTimeout" else ""
            val options = js("""{method: "POST", body:body}""")
            val raw = promise { fetch(url, options) }
            raw?.let { promise { raw.text() } }?.split("\n")?.drop(2) ?: listOf()
        }
    } catch (e: Error) {
        listOf()
    }
}

suspend fun <T> promise(lambda: () -> Promise<T?>): T? {
    var done = false
    var result: T? = null
    lambda().then { res ->
        result = res
        done = true
    }.catch { done = true }
    while (!done) {
        delay(100)
    }
    return result
}
