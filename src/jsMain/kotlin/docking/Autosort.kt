package docking

import inMemoryStorage
import kotlinx.serialization.Serializable
import persistMemory

private typealias Id = String

@Serializable
data class Autosort(var sortQuestId: String? = null, var keywordChests: List<KeywordChest> = listOf(), var keywords: Map<Id, Keyword> = mapOf())

@Serializable
data class KeywordChest(val id: String, val name: String, val location: String, val keywordIds: MutableSet<String>)

@Serializable
data class Keyword(val id: String, val group: String, val name: String)

//TODO - if no mod, handle error gracefully
suspend fun Autosort.connectAutoSort() {
    println("Auto sort connect")
    postToConsole("help autosort 4")
        ?.let { parseQuestId(it.split("\n")) }
        ?.let {
            inMemoryStorage.autoSort.sortQuestId = it
            persistMemory()
        }
}

suspend fun Autosort.refreshKeywords() {
    if (sortQuestId == null) {
        println("No quest id")
        return
    }
    postToConsole("cqf $sortQuestId printkeywords")
        ?.let { parseKeywords(it.split("\n")) }
        ?.let { words ->
            inMemoryStorage.autoSort.keywords = words.associateBy { it.id }
            persistMemory()
        }
}

suspend fun Autosort.refreshKeywordChests() {
    if (sortQuestId == null) {
        println("No quest id")
        return
    }
    postToConsole("cqf $sortQuestId printKeywordChests")
        ?.let { parseKeywordChests(it) }
        ?.let {
            inMemoryStorage.autoSort.keywordChests = it
            persistMemory()
        }
}

private fun parseQuestId(lines: List<String>): String? {
    return lines.firstOrNull { it.startsWith("QUST: AKASAutoSort") }
        ?.split("(")?.last()?.split(")")?.first()
}

suspend fun Autosort.updateChest(chest: KeywordChest) {
    //TODO - test
    postToConsole("cqf $sortQuestId setKeywords ${chest.id} ${chest.keywordIds.joinToString(",")}")
}

suspend fun Autosort.sort() {
    //TODO - test
    postToConsole("cqf $sortQuestId sortChests")
}

private fun parseKeywords(lines: List<String>): List<Keyword> {
    return lines.drop(1).map { line ->
        val (id, rawName) = line.split(" ")
        val (group, name) = rawName.splitByCapital().splitOutKeywordGroup()
        Keyword(id, group, name)
    }
}

private fun String.splitByCapital(): String {
    return this.split(Regex("(?=[A-Z])")).filter { it.isNotBlank() }.joinToString(" ")
}

private fun String.splitOutKeywordGroup() : Pair<String, String>{
    return when {
        startsWith("Object Type") -> Pair("Object Types", replace("Object Type", "").trim())
        else -> "Other" to this
    }
}

private fun parseKeywordChests(textChunk: String): List<KeywordChest> {
    return textChunk.split("==Chest==\n").filter { it.isNotBlank() }.map { rawChest ->
        val (chest, keywords) = rawChest.split("\n").filter { it.isNotBlank() }
        val (id, name, loc) = chest.split("|")
        val words = keywords.split("|").filter { it.isNotBlank() }.toMutableSet()
        KeywordChest(id, name, loc, words)
    }
}
