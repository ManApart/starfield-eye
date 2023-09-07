data class RawGalaxy(
    val starId: Int,
    val planetId: Int,
    val name: String,
    val density: Float,
    val mass: Float,
    val gravity: Float,
    val year: Int,
    val day: Int,
    val asteroids: Int,
    val rings: Int,
    val heat: Int,
    val type: String,
    val magneticField: String,
    val planetClass: String,
    val life: String,
    val settled: String,
)

fun String.toGalaxy(): RawGalaxy {
    throw NotImplementedError()
}

data class RawBiome(
    val starId: Int,
    val planetId: Int,
    val name: String,
)

fun String.toBiome(): RawBiome {
    val parts = this.split(",")
    return RawBiome(parts[1].toInt(), parts[0].toInt(), parts[3])
}

data class RawStar(
    val starId: Int,
    val name: String
)

fun String.toStar(): RawStar {
    val parts = this.split(",")
    return RawStar(parts[0].toInt(), parts[6])
}