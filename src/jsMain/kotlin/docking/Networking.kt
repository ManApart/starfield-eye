package docking

import inMemoryStorage
import io.ktor.client.*
import io.ktor.client.fetch.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import kotlinx.coroutines.delay
import kotlin.js.Promise

private val client = HttpClient()

suspend fun postToConsole(body: String): String? {
    return try {
        with(inMemoryStorage.connectionSettings) {
            docking.client.post("https://$host:$port/console") {
                setBody(body)
            }.bodyAsText()
        }
    }catch (e: Error){
        println(e)
        println(e.stackTraceToString())
        null
    }
}

suspend fun postToConsoleJs(body: String): List<String> {
    return try {
        with(inMemoryStorage.connectionSettings) {
            val options = js("""{method: "POST", body:body}""")
            val raw = promise { fetch("http://$host:$port/console", options) }
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