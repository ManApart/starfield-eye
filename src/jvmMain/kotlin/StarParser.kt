import kotlinx.serialization.encodeToString
import java.io.File

val jsonMapper = kotlinx.serialization.json.Json { ignoreUnknownKeys = true }

private val failedPlanetResourceLookups = mutableListOf<String>()
private val failedSystemResourceLookups = mutableListOf<String>()

fun main() {
    val rawBiomes = File("./raw-data/biomedata.csv").readLines().drop(2).map { it.toBiome() }.groupBy { it.starId }
    val rawStars = File("./raw-data/stars.csv").readLines().drop(2).map { it.toStar() }
    val rawPlanets = File("./raw-data/galaxy.csv").readLines().drop(2).map { it.toPlanet() }.groupBy { it.starId }
    val resourceLookup = parseResourceLookup(File("./raw-data/raw-resources.csv").readLines())


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

    val systems = rawStars.associate { star ->
        val planets = rawPlanets[star.starId] ?: emptyList()
        val biomes = rawBiomes[star.starId] ?: emptyList()
        val resources = resourceLookup[star.name] ?: emptyMap<String, List<ResourceType>>().also {
            failedSystemResourceLookups.add(star.name)
        }
        star.starId to parseSystem(star, planets, biomes, resources)
    }

    println("Failed to find resources for ${failedSystemResourceLookups.size} systems: ${failedSystemResourceLookups.joinToString()}.")
    println("Failed to find resources for ${failedPlanetResourceLookups.size} planets: ${failedPlanetResourceLookups.joinToString()}.")

    File("src/jsMain/resources/data.json").writeText(jsonMapper.encodeToString(Galaxy(systems, galaxySummary)))
}

private fun parseSystem(rawStar: RawStar, rawPlanets: List<RawPlanet>, rawBiomes: List<RawBiome>, systemResources: Map<String, List<ResourceType>>): StarSystem {
    val star = with(rawStar) { Star(starId, catalogueId, name, spectral, temp, mass, radius, magnitude) }
    val pos = with(rawStar) { Pos(x, y, z) }
    val planets = rawPlanets.associate { rawPlanet ->
        val biomes = rawBiomes.filter { it.planetId == rawPlanet.planetId }.map { it.name }
        val resources = systemResources[rawPlanet.name] ?: emptyList<ResourceType>().also { failedPlanetResourceLookups.add(rawPlanet.name) }
        val planet =
            with(rawPlanet) {
                Planet(
                    planetId,
                    parentId,
                    name,
                    planetClass,
                    bodyType,
                    radius,
                    density,
                    mass,
                    gravity,
                    year,
                    day,
                    asteroids,
                    rings,
                    heat,
                    type,
                    magneticField,
                    life,
                    settled,
                    biomes,
                    resources
                )
            }
        rawPlanet.planetId to planet
    }
    val nestedPlanets = planets.values.filter { it.parentId == 0 }.associate { it.id to mutableListOf<Int>() }
    planets.values.filter { it.parentId != 0 }.forEach { moon ->
        nestedPlanets[moon.parentId]?.add(moon.id)
    }
    return StarSystem(star, pos, planets, nestedPlanets)
}


private fun parseResourceLookup(lines: List<String>): Map<String, Map<String, List<ResourceType>>> {
    val lookup = mutableMapOf<String, MutableMap<String, List<ResourceType>>>()
    lines.forEach { line ->
        val parts = line.split(",").map { it.trim() }
        val system = parts.first()
        val planetName = parts[1]
        val resourcesPresent = parts.subList(4, 48)
        val resources = ResourceType.entries.filterIndexed { i, _ -> resourcesPresent[i].isNotBlank() }

        lookup.putIfAbsent(system, mutableMapOf())
        lookup[system]?.put(planetName, resources)
    }

    return lookup
}