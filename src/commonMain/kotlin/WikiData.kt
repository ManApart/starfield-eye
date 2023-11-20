import kotlinx.serialization.Serializable

interface WikiData {
    val name: String
}

@Serializable
data class PlanetWikiData(
    override val name: String = "",
    val type: String = "",
    val temperature: String = "",
    val atmosphere: String = "",
    val magnetosphere: String = "",
    val fauna: String = "",
    val flora: String = "",
    val water: String = "",
    val resources: List<String> = listOf(),
    val traits: List<String> = listOf(),
): WikiData

@Serializable
data class MissionWikiData(
    override val name: String = "",
    val id: String = "",
    val type: MissionType = MissionType.OTHER,
): WikiData

@Serializable
data class FaunaWikiData(
    override val name: String = "",
    val temperament: Temperament = Temperament.UNKNOWN,
    val planet: String? = null,
    val planetId: String? = null,
    val biomes: List<String> = listOf(),
    val resource: String,
    val abilities: List<String> = listOf(),
    val other: Map<String, String> = mapOf()
): WikiData

@Serializable
data class FloraWikiData(
    override val name: String = "",
    val planet: String? = null,
    val planetId: String? = null,
    val biomes: List<String> = listOf(),
    val resource: String,
    val other: Map<String, String> = mapOf()
): WikiData