import kotlinx.serialization.encodeToString
import java.io.File

val jsonMapper = kotlinx.serialization.json.Json { ignoreUnknownKeys = true }

fun main() {
    println("Hello worlds")
    val rawBiomes = File("./raw-data/biomedata.csv").readLines().drop(2).map { it.toBiome() }.groupBy { it.starId }
    val rawStars = File("./raw-data/stars.csv").readLines().drop(2).map { it.toStar() }
    val rawPlanets = File("./raw-data/galaxy.csv").readLines().drop(2).map { it.toPlanet() }.groupBy { it.starId }
    println("Parsed Raw Records")

    val systems = rawStars.associate { it.starId to parseSystem(it, rawPlanets[it.starId] ?: emptyList(), rawBiomes[it.starId] ?: emptyList()) }
    println("Parsed Star systems")
    File("src/jsMain/resources/data.json").writeText(jsonMapper.encodeToString(systems))
}

private fun parseSystem(rawStar: RawStar, rawPlanets: List<RawPlanet>, rawBiomes: List<RawBiome>): StarSystem {
    val star = with(rawStar) { Star(starId, name, spectral, temp) }
    val pos = with(rawStar) { Pos(x, y, z) }
    val planets = rawPlanets.associate { rawPlanet ->
        val biomes = rawBiomes.filter { it.planetId == rawPlanet.planetId }.map { it.name }
        val planet = with(rawPlanet) { Planet(planetId, name, planetClass, bodyType, radius, density, mass, gravity, year, day, asteroids, rings, heat, type, magneticField, life, settled, biomes) }
        rawPlanet.planetId to planet
    }
    return StarSystem(star, pos, planets)
}

private fun parseStars(raw: List<RawStar>): Map<Int, Star> {
    return raw.associate { it.starId to Star(it.starId, it.name, it.spectral, it.temp) }
}

private fun parsePlanets(): Map<Int, List<Planet>> {
    throw NotImplementedError()
}

private fun parseSystems(rawBiomes: RawBiome): Map<Int, StarSystem> {
    throw NotImplementedError()

}