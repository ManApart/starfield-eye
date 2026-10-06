package docking

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
            sortQuestId = it
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
            keywords = words.associateBy { it.id }
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
            keywordChests = it
            persistMemory()
        }
}

private fun parseQuestId(lines: List<String>): String? {
    return lines.firstOrNull { it.startsWith("QUST: AKASAutoSort") }
        ?.split("(")?.last()?.split(")")?.first()
}

suspend fun Autosort.updateChest(i: Int, chest: KeywordChest) {
    postToConsole("cqf $sortQuestId setKeywords $i \"${chest.keywordIds.joinToString(",")}\"")
}

suspend fun Autosort.sort() {
    postToConsole("cqf $sortQuestId sortItems")
}

private fun parseKeywords(lines: List<String>): List<Keyword> {
    if (lines.size < 3) return emptyList()
    return lines.drop(3).filter { it.contains(" ") }.map { line ->
        val (id, rawName) = line.split(" ")
        val (group, name) = rawName.splitByCapital().splitOutKeywordGroup()
        Keyword(id.uppercase(), group, name)
    }
}

private fun String.splitByCapital(): String {
    return this.split(Regex("(?=[A-Z])")).filter { it.isNotBlank() }.joinToString(" ")
}

private fun String.splitOutKeywordGroup() : Pair<String, String>{
    return when {
        startsWith("Object Type") -> clean("Object Type", "Object Types")
        startsWith("Resource Type") -> clean("Resource Type", "Resource Types")
        startsWith("Weapon Type") -> clean("Weapon Type", "Weapon Types")
        startsWith("Manufacturer") -> clean("Manufacturer", "Manufacturers")
        startsWith("Inventory Category") -> clean("Inventory Category", "Inventory Categories")
        else -> "Other" to this
    }
}

private fun String.clean(delete: String, label: String): Pair<String, String> {
    return Pair(label, replace(delete, "").trim())
}

private fun parseKeywordChests(textChunk: String): List<KeywordChest> {
    println(textChunk)
    return textChunk.split("==Chest==\n").filter { it.isNotBlank() && it.contains("|") }.map { rawChest ->
        println(rawChest)
        val (chest, keywords) = rawChest.split("\n").filter { it.isNotBlank() }
        val (id, name, loc) = chest.split("|")
        val words = keywords.split("|").filter { it.isNotBlank() }.map { it.uppercase() }.toMutableSet()
        KeywordChest(id.uppercase(), name, loc, words)
    }
}
