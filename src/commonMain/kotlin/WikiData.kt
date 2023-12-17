import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

interface WikiData {
    val name: String
}

@Serializable
data class PlanetWikiData(
    override val name: String = "",
    val imageUrl: String? = null,
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
    val imageUrl: String? = null,
    val temperament: Temperament = Temperament.UNKNOWN,
    val planet: String? = null,
    val planetId: String? = null,
    val biomes: List<String> = listOf(),
    val resource: String,
    val abilities: List<String> = listOf(),
    val other: Map<String, String> = mapOf()
): WikiData {
    @Transient
    val uniqueId = "$planetId-$name"
}

@Serializable
data class FloraWikiData(
    override val name: String = "",
    val imageUrl: String? = null,
    val planet: String? = null,
    val planetId: String? = null,
    val biomes: List<String> = listOf(),
    val resource: String,
    val other: Map<String, String> = mapOf()
): WikiData {
    @Transient
    val uniqueId = "$planetId-$name"
}

enum class PerkCategory { PHYSICAL, SOCIAL, COMBAT, SCIENCE, TECH, OTHER }
enum class PerkTier { NOVICE, ADVANCED, EXPERT, MASTER }

@Serializable
data class Perk(
    override val name: String,
    val category: PerkCategory,
    val tier: PerkTier,
    val url: String,
    val ranks: Map<Int, String>,
) : WikiData