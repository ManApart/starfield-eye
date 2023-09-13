data class RawPlanet(
    val starId: Int,
    val planetId: Int,
    val parentId: Int,
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
    val parts = this.split(",").map { it.trim() }
    return RawPlanet(
        parts[4].toInt(),
        parts[3].toInt(),
        parts[11].toInt(),
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
    val parts = this.split(",").map { it.trim() }
    val name = parts[3].replace("-", " ").replace("_", ": ")
    return RawBiome(parts[1].toInt(), parts[0].toInt(), name)
}

data class RawStar(
    val starId: Int,
    val name: String,
    val catalogueId: String,
    val x: Float,
    val y: Float,
    val z: Float,
    val spectral: String,
    val temp: Int,
    val mass: Float,
    val radius: Float,
    val magnitude: Float,
    )

fun String.toStar(): RawStar {
    val parts = this.split(",").map { it.trim() }
    return RawStar(
        parts[0].toInt(),
        parts[6],
        parts[4],
        parts[17].toFloat(),
        parts[18].toFloat(),
        parts[19].toFloat(),
        parts[15],
        parts[42].toInt(),
        parts[38].toFloat(),
        radius = parts[7].toFloat(),
        parts[13].toFloat(),
        )
}