package docking

import MissionWikiData
import PollResponse
import Quest
import kotlinx.coroutines.delay

suspend fun healthCheck(): Boolean {
    val response = postToConsole("GetSFSEVersion")
    val versionLine = response?.split("\n")?.lastOrNull { it.isNotBlank() }
    println(versionLine ?: "Disconnected: $response")
    return versionLine?.contains("SFSE version") ?: false
}

suspend fun getQuests(missionReference: Map<String, MissionWikiData>): List<Quest> {
    val lines = postToConsoleJs("sqo")
    return parseQuests(lines, missionReference)
}

suspend fun poll(missionReference: Map<String, MissionWikiData>): PollResponse {
    val lines = postToConsoleJs("bat starfield-eye-poll")
    return parsePollResponse(lines, missionReference)
}


suspend fun setCourse(destination: String) {
    try {
        postToConsole("showmenu galaxyStarMapMenu")
        delay(100)
        println(postToConsole("AddPlotToBody \"${destination}\""))
    } catch (e: Error) {
        println(e)
    }
}
