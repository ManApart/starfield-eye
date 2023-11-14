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
    val type: MissionType = MissionType.OTHER,
)

@Serializable
data class FaunaWikiData(
    val name: String = "",
    val temperament: Temperament = Temperament.UNKNOWN,
    val planet: String? = null,
    val biomes: List<String> = listOf(),
    val resource: String,
    val abilities: List<String> = listOf(),
    val other: Map<String, String> = mapOf()
)
