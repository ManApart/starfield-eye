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
) : WikiData

@Serializable
data class MissionWikiData(
    override val name: String = "",
    val id: String = "",
    val type: MissionType = MissionType.OTHER,
) : WikiData

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
) : WikiData {
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
) : WikiData {
    @Transient
    val uniqueId = "$planetId-$name"
}

enum class PerkCategory(val color: String) { PHYSICAL("#4b5e46"), SOCIAL("#a68d4f"), COMBAT("#944236"), SCIENCE("#43688a"), TECH("#6b5887"), OTHER("#000") }
enum class PerkTier { NOVICE, ADVANCED, EXPERT, MASTER }

@Serializable
data class Perk(
    override val name: String,
    val category: PerkCategory,
    val tier: PerkTier,
    val url: String,
    val ranks: Map<Int, String>,
) : WikiData

enum class ResearchCategory {
    PHARMACOLOGY, FOOD_AND_DRINK, OUTPOST_MANAGEMENT, EQUIPMENT, WEAPONRY, OTHER;

    val pic = "./images/research/${name.lowercase()}.svg"
    val prettyName = name.capitalize().replace("_", " ")
}

enum class ProjectState { COMPLETED, BLOCKED, NONE }

@Serializable
data class ResearchProject(
    override val name: String,
    val category: ResearchCategory,
    val rank: Int,
    val description: String,
    val prerequisites: Map<String, Int>,
    val perks: Map<String, Int>,
    val materials: List<Material>
) : WikiData {
    @Transient
    val id = "$name $rank"
}

@Serializable
data class Material(
    val name: String,
    val count: Int,
    val url: String,
)

enum class POIType { CITY, SETTLEMENT, FARM, STARSTATION, STARYARD, SHIP, DERELICT_SHIP, FRACTURED_EARTH_LANDMARK, INDUSTRIAL_OUTPOST, MILITARY_BASE, MINING_BASE, CRASHED_STARSHIP, SCIENCE_LAB, LANDING_SITE, TEMPLE, OTHER }

fun String.toPOIType(): POIType {
    val clean = uppercase().dropLast(1).replace("CITIE", "CITY").replace("OTHER_SETTLEMENTS_AND_OUTPOST", "SETTLEMENT")
    return POIType.entries.firstOrNull { it.name == clean } ?: POIType.OTHER.also { println("No match for POI Type $clean") }
}

@Serializable
data class PointOfInterest(
    override val name: String,
    val description: String,
    val type: POIType,
    val wikiLink: String,
    val starSystem: String,
    val planet: String,
) : WikiData
