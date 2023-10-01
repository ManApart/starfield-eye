import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.utils.io.core.*
import io.ktor.client.*
import io.ktor.client.fetch.*
import io.ktor.client.plugins.Charsets
import io.ktor.client.plugins.compression.*
import io.ktor.util.*
import io.ktor.utils.io.charsets.*
import kotlinx.coroutines.await
import kotlinx.coroutines.delay
import org.w3c.dom.url.URL
import org.w3c.xhr.XMLHttpRequest
import kotlin.js.Promise

private val client = HttpClient() {
    install(ContentEncoding) {
        deflate()
        gzip()
    }
    Charsets {
        register(Charsets.UTF_8)
        sendCharset = Charsets.UTF_8
    }
}


private suspend fun postToConsole(body: String): String {
    return with(inMemoryStorage.connectionSettings) {
        client.post("http://$host:$port/console") {
            setBody(body)
        }.bodyAsText()
            .also { println("Result: $it") }
    }
}

private suspend fun postToConsole2(body: String): String {
    val xhr = XMLHttpRequest()
    xhr.onreadystatechange = {
        if (xhr.responseText != null) {
            println(xhr.responseText)
        }
    }
    with(inMemoryStorage.connectionSettings) {
        xhr.open("POST", "http://$host:$port/console", true)
    }
    xhr.send(body)
    return ""
}

private suspend fun postToConsole3(body: String): String {
    with(inMemoryStorage.connectionSettings) {
        val options = js("""{method: "POST", body:body}""")
        fetch("http://$host:$port/console", options).then { response ->
            response.text().then { res ->
                println("response: ${res}")
            }
        }
    }
    return ""
}

private suspend fun postToConsole4(body: String): String {
    with(inMemoryStorage.connectionSettings) {
        val options = js("""{method: "POST", body:body}""")
        val raw = promise {
            fetch("http://$host:$port/console", options)
        }!!
        return promise { raw.text() }!!
    }
}

suspend fun <T> promise(lambda: () -> Promise<T?>): T? {
    var done = false
    var result: T? = null
    lambda().then { res ->
        result = res
        done = true
    }
    while (!done) {
        delay(100)
    }

    return result
}


suspend fun healthCheck(): Boolean {
    val versionLine = try {
        val raw = postToConsole("GetSFSEVersion")
        raw.split("\n").last { it.isNotBlank() }
    } catch (e: Error) {
        println(e)
        println(e.stackTraceToString())
        null
    }
    println(versionLine ?: "Disconnected")
    return versionLine?.contains("SFSE version") ?: false
}

suspend fun getQuests(): List<Quest> {
    println("4")
    println( postToConsole4("sqo"))
//    postToConsole("GetPCMiscStat \"locks picked\"")
//    val lines = postToConsole("sqo").split("\n").drop(2)
//    println(lines.take(100))
//    return lines
//        .asSequence()
//        .mapIndexedNotNull { i, line ->
//            if (line.startsWith("==")) i else null
//        }
//        .windowed(2, 1, true) {
//            val end = if (it.size > 1) it.last() else lines.size
//            lines.subList(it.first(), end)
//        }.toList()
//        .also { println("Found ${it.size} raw quests") }
//        .map { parseQuest(it) }
//        .also { println("Found ${it.size} parsed quests") }
//        .filter { it.completed || it.displayed }
//        .toList()
    return listOf()
}

private fun parseQuest(lines: List<String>): Quest {
    val title = lines.first().replace("==", "").trim()
    val stages = lines.asSequence().drop(2)
        .filter { it.isNotBlank() }
        .map { it.split(" ").map { word -> word.trim() } }
        .filter { it.first().toIntOrNull() != null }
        .map { words ->
            QuestStage(
                words.first().toInt(),
                words.subList(1, words.size - 1).joinToString(" "),
                words.last() == "COMPLETED",
                words.last() == "DISPLAYED"
            )
        }.associateBy { it.id }

    val cleanTitle = title.ifBlank { stages.values.firstOrNull()?.name ?: "" }

    return Quest(cleanTitle, stages)
}

