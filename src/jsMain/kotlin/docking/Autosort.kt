package docking

import persistMemory


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

suspend fun Autosort.refreshKeywords(persist: Boolean = true) {
    if (sortQuestId == null) {
        println("No quest id")
        return
    }
    postToConsole("cqf $sortQuestId printkeywords")
        ?.let { parseKeywords(it.split("\n")) }
        ?.let { words ->
            keywords = words.associateBy { it.id }
            if (persist) persistMemory()
        }
}

suspend fun Autosort.refreshDrainChests(persist: Boolean = true) {
    if (sortQuestId == null) {
        println("No quest id")
        return
    }
    postToConsole("cqf $sortQuestId printDrainChests")
        ?.let { parseDrainChests(it) }
        ?.let {
            drainChests = it
            if (persist) persistMemory()
        }
}

suspend fun Autosort.refreshKeywordChests(persist: Boolean = true) {
    if (sortQuestId == null) {
        println("No quest id")
        return
    }
    postToConsole("cqf $sortQuestId printKeywordChests")
        ?.let { parseKeywordChests(it) }
        ?.let {
            keywordChests = it
            if (persist) persistMemory()
        }
}

suspend fun Autosort.refreshExactChests(persist: Boolean = true) {
    if (sortQuestId == null) {
        println("No quest id")
        return
    }
    postToConsole("cqf $sortQuestId printExactChests")
        ?.let { parseExactChests(it) }
        ?.let {
            exactChests = it
            if (persist) persistMemory()
        }
}

suspend fun Autosort.removeChest(type: ChestType, i: Int) {
    if (sortQuestId == null) {
        println("No quest id")
        return
    }
    postToConsole("cqf $sortQuestId removeNthContainer ${type.id} $i")
    when (type) {
        ChestType.KEYWORD -> refreshKeywordChests()
        ChestType.EXACT -> refreshExactChests()
        ChestType.DRAIN -> refreshDrainChests()
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

private fun String.splitOutKeywordGroup(): Pair<String, String> {
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

private fun parseDrainChests(textChunk: String): List<DrainChest> {
    println(textChunk)
    return textChunk.split("==Chest==\n").filter { it.isNotBlank() && it.contains("|") }.map { rawChest ->
        println(rawChest)
        val chest = rawChest.split("\n").first { it.isNotBlank() }
        val (id, name) = chest.split("|")
        DrainChest(id.uppercase(), name)
    }
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

private fun parseExactChests(textChunk: String): List<ExactChest> {
    println(textChunk)
    return textChunk.split("==Chest==\n").filter { it.isNotBlank() && it.contains("|") }.map { rawChest ->
        println(rawChest)
        val chest = rawChest.split("\n").first { it.isNotBlank() }
        val (id, name, loc) = chest.split("|")
        ExactChest(id.uppercase(), name, loc)
    }
}
