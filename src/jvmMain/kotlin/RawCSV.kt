data class RawPlanet(
    val starId: Int,
    val planetId: Int,
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
)

fun String.toPlanet(): RawPlanet {
    val parts = this.split(",")
    return RawPlanet(
        parts[4].toInt(),
        parts[3].toInt(),
        parts[8],
        parts[7],
        bodyType = parts[10].toInt(),
        parts[16].toFloat(),
        parts[20].toFloat(),
        parts[21].toFloat(),
        parts[22].toFloat(),
        year = parts[23].toFloat(),
        parts[24].toFloat(),
        parts[27].toInt(),
        parts[29].toInt(),
        parts[34].toInt(),
        type = parts[28],
        parts[30],
        parts[35],
        settled = parts[36],
    )
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
    val name: String,
    val x: Float,
    val y: Float,
    val z: Float,
    val spectral: String,
    val temp: Int
)

fun String.toStar(): RawStar {
    val parts = this.split(",")
    return RawStar(
        parts[0].toInt(),
        parts[6],
        parts[17].toFloat(),
        parts[18].toFloat(),
        parts[19].toFloat(),
        parts[15],
        parts[42].toInt()
    )
}