import kotlinx.serialization.Serializable

//TODO - add labels and notes

@Serializable
data class Star(
    val id: Int,
    val name: String,
    val spectral: String,
    val temp: Int
)

@Serializable
data class Planet(
    val id: Int,
    val name: String,
    val planetClass: String,
    val bodyType: Int,
    val radius: Float,
    val density: Float,
    val mass: Float,
    val gravity: Float,
    val year: Float,
    val day: Float,
    val asteroids: Int,
    val rings: Int,
    val heat: Int,
    val type: String,
    val magneticField: String,
    val life: String,
    val settled: String,
    val biomes: List<String>
)

@Serializable
data class Pos(val x: Float, val y: Float, val z: Float)
@Serializable
data class StarSystem(
    val star: Star,
    val pos: Pos,
    val planets: Map<Int, Planet>
)
