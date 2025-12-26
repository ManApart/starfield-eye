import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
data class Star(
    val id: String,
    val rawId: Int?,
    val wikiUrlId: String,
    val catalogueId: String,
    val name: String,
    val level: Int,
    val spectral: String,
    val temp: String,
    val mass: String,
    val radius: Float,
    val magnitude: Float,
)

@Serializable
data class Planet(
    val id: String,
    val rawId: Int?,
    val starId: String,
    var parentId: String,
    val name: String,
    val planetClass: String,
    val bodyType: Int,
    val bodyTypeDescription: String,
    val radius: Float,
    val density: Float,
    val mass: Float,
    val gravity: Float,
    val year: Float,
    val day: Float,
    val asteroids: Int,
    val rings: Int,
    val atmosphere: String,
    val heat: Int,
    val temperature: String,
    val type: String,
    val magneticField: String,
    val water: String,
    val life: String,
    val settled: String,
    val flora: String,
    val fauna: String,
    val biomes: List<String> = emptyList(),
    val traits: List<String> = emptyList(),
    val moonIds: List<String> = listOf(),
    val organicResources: Set<String> = emptySet(),
    val inorganicResources: Set<ResourceType> = emptySet(),
) {
    @Transient
    val uniqueId = "$starId:$id"
}

@Serializable
data class Pos(val x: Float = 0f, val y: Float = 0f, val z: Float = 0f)

@Serializable
data class StarSystem(
    val star: Star,
    val pos: Pos,
    val planets: Map<String, Planet>,
    val planetChildren: Map<String, List<String>>
)

@Serializable
data class Galaxy(
    val systems: Map<String, StarSystem> = mapOf(),
    val summary: GalaxySummary = GalaxySummary()
) {
    @Transient
    val planets = systems.values.flatMap { it.planets.values }.associateBy { it.uniqueId }
}

@Serializable
data class GalaxySummary(
    val minX: Float = 0f,
    val maxX: Float = 0f,
    val minY: Float = 0f,
    val maxY: Float = 0f,
    val minZ: Float = 0f,
    val maxZ: Float = 0f,
    val distX: Float = 0f,
    val distY: Float = 0f,
    val distZ: Float = 0f,
)
