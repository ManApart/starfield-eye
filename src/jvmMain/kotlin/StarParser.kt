import kotlinx.serialization.encodeToString
import java.io.File

val jsonMapper = kotlinx.serialization.json.Json { ignoreUnknownKeys = true }

fun main() {
    val rawBiomes = File("./raw-data/biomedata.csv").readLines().drop(2).map { it.toBiome() }.groupBy { it.starId }
    val rawStars = File("./raw-data/stars.csv").readLines().drop(2).map { it.toStar() }
    val rawPlanets = File("./raw-data/galaxy.csv").readLines().drop(2).map { it.toPlanet() }.groupBy { it.starId }

    val galaxySummary = with(rawStars) {
        val maxX = maxOf { it.x }
        val maxY = maxOf { it.y }
        val maxZ = maxOf { it.z }
        val minX = minOf { it.x }
        val minY = minOf { it.y }
        val minZ = minOf { it.z }
        val distX = maxX - minX
        val distY = maxY - minY
        val distZ = maxZ - minZ
        GalaxySummary(minX, maxX, minY, maxY, minZ, maxZ, distX, distY, distZ)
    }

    val systems = rawStars.associate { it.starId to parseSystem(it, rawPlanets[it.starId] ?: emptyList(), rawBiomes[it.starId] ?: emptyList()) }
    File("src/jsMain/resources/data.json").writeText(jsonMapper.encodeToString(Galaxy(systems, galaxySummary)))
}

private fun parseSystem(rawStar: RawStar, rawPlanets: List<RawPlanet>, rawBiomes: List<RawBiome>): StarSystem {
    val star = with(rawStar) { Star(starId, name, spectral, temp) }
    val pos = with(rawStar) { Pos(x, y, z) }
    val planets = rawPlanets.associate { rawPlanet ->
        val biomes = rawBiomes.filter { it.planetId == rawPlanet.planetId }.map { it.name }
        val planet =
            with(rawPlanet) { Planet(planetId, parentId, name, planetClass, bodyType, radius, density, mass, gravity, year, day, asteroids, rings, heat, type, magneticField, life, settled, biomes) }
        rawPlanet.planetId to planet
    }
    val nestedPlanets = planets.values.filter { it.parentId == 0 }.associate { it.id to mutableListOf<Int>() }
    planets.values.filter { it.parentId != 0 }.forEach { moon ->
        nestedPlanets[moon.parentId]?.add(moon.id)
    }
    return StarSystem(star, pos, planets, nestedPlanets)
}
