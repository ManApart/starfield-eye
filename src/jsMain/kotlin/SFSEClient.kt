import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.statement.*

private val client = HttpClient()

suspend fun healthCheck(): Boolean {
    val raw = with(inMemoryStorage.connectionSettings) {
        client.post("http://$host:$port/console") {
            setBody("GetSFSEVersion")
        }.bodyAsText()
    }
    val versionLine = raw.split("\n").last()
    println(versionLine)
    return versionLine.contains("SFSE version")
}

