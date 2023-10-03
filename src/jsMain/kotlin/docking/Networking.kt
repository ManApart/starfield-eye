package docking

import inMemoryStorage
import io.ktor.client.*
import io.ktor.client.fetch.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import kotlinx.coroutines.delay
import kotlin.js.Promise

private val client = HttpClient()

suspend fun postToConsole(body: String): String {
    return with(inMemoryStorage.connectionSettings) {
        docking.client.post("http://$host:$port/console") {
            setBody(body)
        }.bodyAsText()
    }
}

suspend fun postToConsoleJs(body: String): String? {
    with(inMemoryStorage.connectionSettings) {
        val options = js("""{method: "POST", body:body}""")
        val raw = promise { fetch("http://$host:$port/console", options) }
        return raw?.let { promise { raw.text() } }
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