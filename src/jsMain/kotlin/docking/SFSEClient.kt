package docking

import Quest

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
    val lines = try {
        postToConsoleJs("bat starfield-eye-poll")?.split("\n")?.drop(2) ?: listOf()
    } catch (e: Error) {
        listOf()
    }
    return parseQuests(lines)
}


suspend fun setCourse(destination: String) {
    try {
        println(postToConsole("AddPlotToBody \"${destination}\""))
    } catch (e: Error) {
        println(e)
    }
}
