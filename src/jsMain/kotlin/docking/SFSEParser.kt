package docking

import Quest
import QuestStage
import QuestStageState

fun parseQuests(lines: List<String>): List<Quest>{
    return lines.asSequence()
        .mapIndexedNotNull { i, line ->
            if (line.startsWith("==")) i else null
        }
        .windowed(2, 1, true) {
            val end = if (it.size > 1) it.last() else lines.size
            lines.subList(it.first(), end)
        }.toList()
        .map { parseQuest(it) }
        .filter { it.latestState == QuestStageState.COMPLETED || it.latestState == QuestStageState.DISPLAYED }
        .toList()
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

