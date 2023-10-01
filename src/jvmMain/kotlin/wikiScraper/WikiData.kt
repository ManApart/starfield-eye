package wikiScraper

import kotlinx.serialization.Serializable

@Serializable
data class PlanetWikiData(
    val name: String = "",
    val type: String = "",
    val temperature: String = "",
    val atmosphere: String = "",
    val magnetosphere: String = "",
    val fauna: String = "",
    val flora: String = "",
    val water: String = "",
    val resources: List<String> = listOf(),
    val traits: List<String> = listOf(),
)


@Serializable
data class MissionWikiData(
    val name: String = "",
    val id: String = "",
)

enum class MissionType {MAIN, MISC, OTHER}

fun String.toMissionType() : MissionType {
    return MissionType.OTHER
}