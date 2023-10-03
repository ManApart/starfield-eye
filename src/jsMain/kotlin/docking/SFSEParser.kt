package docking

import GeneralStats
import MiscStats
import PollResponse
import Quest
import QuestStage
import QuestStageState
import jsonMapper
import kotlinx.serialization.encodeToString

fun parseQuests(lines: List<String>): List<Quest> {
    return lines.chunkedBy("==")
        .map { parseQuest(it) }
        .filter { it.latestState == QuestStageState.COMPLETED || it.latestState == QuestStageState.DISPLAYED }
        .toList()
}

private fun List<String>.chunkedBy(delimiter: String): List<List<String>> {
    return asSequence()
        .mapIndexedNotNull { i, line ->
            if (line.startsWith(delimiter)) i else null
        }
        .windowed(2, 1, true) {
            val end = if (it.size > 1) it.last() else this.size
            this.subList(it.first(), end)
        }.toList()
}

private fun parseQuest(lines: List<String>): Quest {
    val title = lines.first().replace("==", "").trim()
    val stages = lines.asSequence().drop(2)
        .filter { it.isNotBlank() }
        .map { it.split(" ").map { word -> word.trim() } }
        .filter { it.first().toIntOrNull() != null }
        .map { words ->
            val id = words.first().toInt()
            val name = words.subList(1, words.size - 1).joinToString(" ")
            val state = QuestStageState.entries.firstOrNull { it.name == words.last() } ?: QuestStageState.OTHER
            QuestStage(id, name, state)
        }.toList()

    val cleanTitle = title.ifBlank { stages.firstOrNull()?.name ?: "" }

    return Quest(cleanTitle, stages)
}

fun parsePollResponse(lines: List<String>): PollResponse {
    println("Parsing strings ")
    val commands = lines.chunkedBy(">")
        .filter { it.size != 1 }
        .associate { it.first().replace(">",  "").trim() to it.drop(1) }
        .let { RawPollResponse(it) }

    //parse through map doing sections by sections
    println(jsonMapper.encodeToString(commands))

    return PollResponse(
        parseQuests(commands.getQuests()),
        MiscStats(
            GeneralStats(
                commands.getMiscStatInt("locations discovered"),
                commands.getMiscStatInt("locations explored"),
                commands.getMiscStatInt("days passed"),
            )
        )
    )
}