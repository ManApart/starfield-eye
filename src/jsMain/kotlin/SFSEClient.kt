import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.statement.*

private val client = HttpClient()

private suspend fun postToConsole(body: String): String {
    return with(inMemoryStorage.connectionSettings) {
        client.post("http://$host:$port/console") {
            setBody(body)
        }.bodyAsText()
    }
}

suspend fun healthCheck(): Boolean {
    val raw = postToConsole("GetSFSEVersion")
    val versionLine = raw.split("\n").last { it.isNotBlank() }
    println(versionLine)
    return versionLine.contains("SFSE version")
}

suspend fun getQuests(): List<Quest> {
    val lines = postToConsole("sqo").split("\n").drop(1)
    return lines
        .asSequence()
        .filter { it.startsWith("==") }
        //Ignore quests with duplicate names
        .toSet()
        .asSequence()
        .map { lines.indexOf(it) }
        .windowed(2, 1, true) {
            val end = if (it.size > 1) it.last() else lines.size
            lines.subList(it.first(), end)
        }
        .map { parseQuest(it) }
        .filter { it.completed || it.displayed }
        .toList()
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
    return Quest(title, stages)
}

