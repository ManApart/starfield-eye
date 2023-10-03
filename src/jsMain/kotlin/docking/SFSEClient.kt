package docking

import PollResponse
import Quest

suspend fun healthCheck(): Boolean {
    val versionLine = postToConsole("GetSFSEVersion")?.split("\n")?.last { it.isNotBlank() }
    println(versionLine ?: "Disconnected")
    return versionLine?.contains("SFSE version") ?: false
}

suspend fun getQuests(): List<Quest> {
    val lines = postToConsoleJs("sqo")
    return parseQuests(lines)
}

suspend fun poll(): PollResponse {
    val lines = postToConsoleJs("bat starfield-eye-poll")
    return parsePollResponse(lines)
}


suspend fun setCourse(destination: String) {
    try {
        println(postToConsole("AddPlotToBody \"${destination}\""))
    } catch (e: Error) {
        println(e)
    }
}
