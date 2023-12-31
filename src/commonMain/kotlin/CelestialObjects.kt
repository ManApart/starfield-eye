import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
data class Star(
    val id: Int,
    val catalogueId: String,
    val name: String,
    val spectral: String,
    val temp: Int,
    val mass: Float,
    val radius: Float,
    val magnitude: Float,
)

@Serializable
data class Planet(
    val id: Int,
    val starId: Int,
    val parentId: Int,
    val name: String,
    val imageUrl: String? = null,
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
    val biomes: List<String>,
    val traits: List<String>,
    val organicResources: Set<String>,
    val inorganicResources: Set<ResourceType>,
) {
    @Transient
    val uniqueId = "$starId-$id"
}

@Serializable
data class Pos(val x: Float, val y: Float, val z: Float)
@Serializable
data class StarSystem(
    val star: Star,
    val pos: Pos,
    val planets: Map<Int, Planet>,
    val planetChildren: Map<Int, List<Int>>
)

@Serializable
data class Galaxy(
    val systems: Map<Int, StarSystem> = mapOf(),
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