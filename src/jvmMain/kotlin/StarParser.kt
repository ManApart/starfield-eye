import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import wikiScraper.PlanetWikiData
import java.io.File
import java.lang.IllegalArgumentException

val jsonMapper = kotlinx.serialization.json.Json { ignoreUnknownKeys = true }

private val failedPlanetResourceLookups = mutableSetOf<String>()
private val failedSystemResourceLookups = mutableSetOf<String>()
private val failedWikiResourceLookups = mutableSetOf<String>()

fun main() {
    val rawBiomes = File("./raw-data/biomedata.csv").readLines().drop(2).map { it.toBiome() }.groupBy { it.starId }
    val rawStars = File("./raw-data/stars.csv").readLines().drop(2).map { it.toStar() }
    val rawPlanets = File("./raw-data/galaxy.csv").readLines().drop(2).map { it.toPlanet() }.groupBy { it.starId }
    val resourceLookup = parseResourceLookup(File("./raw-data/raw-resources.csv").readLines())
    val wikiDataFile = File("raw-data/wiki-data.json")
    val wikiData = if (wikiDataFile.exists()) {
        jsonMapper.decodeFromString<Map<String, PlanetWikiData>>(wikiDataFile.readText()).toMutableMap()
    } else mapOf()

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
        star.starId to parseSystem(star, planets, biomes, resources, wikiData)
    }

    println("Failed to find resources for ${failedSystemResourceLookups.size} systems: ${failedSystemResourceLookups.joinToString()}.")
    println("Failed to find resources for ${failedPlanetResourceLookups.size} planets: ${failedPlanetResourceLookups.joinToString()}.")
    println("Failed to find resources for ${failedWikiResourceLookups.size} wiki resources: ${failedWikiResourceLookups.joinToString()}.")

    File("src/jsMain/resources/data.json").writeText(jsonMapper.encodeToString(Galaxy(systems, galaxySummary)))
}

private fun parseSystem(
    rawStar: RawStar,
    rawPlanets: List<RawPlanet>,
    rawBiomes: List<RawBiome>,
    systemResources: Map<String, List<ResourceType>>,
    wikiDataMap: Map<String, PlanetWikiData>
): StarSystem {
    val star = with(rawStar) { Star(starId, catalogueId, name, spectral, temp, mass, radius, magnitude) }
    val pos = with(rawStar) { Pos(x, y, z) }
    val planets = rawPlanets.associate { rawPlanet ->
        val biomes = rawBiomes.filter { it.planetId == rawPlanet.planetId }.map { it.name }

        //TODO - resource matchup
        val wikiData = wikiDataMap[rawPlanet.name] ?: PlanetWikiData()
        val resources = determineResources(rawPlanet, systemResources, wikiData)

        val flora = wikiData.flora.replace("[[#Flora|]]", "")
        val fauna = wikiData.fauna.replace("[[#Fauna|]]", "")

        val planet =
            with(rawPlanet) {
                Planet(
                    planetId,
                    starId,
                    parentId,
                    name,
                    planetClass,
                    bodyType,
                    wikiData.type,
                    radius,
                    density,
                    mass,
                    gravity,
                    year,
                    day,
                    asteroids,
                    rings,
                    wikiData.atmosphere,
                    heat,
                    wikiData.temperature,
                    type,
                    magneticField collapse wikiData.magnetosphere,
                    wikiData.water,
                    life,
                    settled,
                    flora,
                    fauna,
                    biomes,
                    wikiData.traits,
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

private fun determineResources(
    rawPlanet: RawPlanet,
    systemResources: Map<String, List<ResourceType>>,
    wikiData: PlanetWikiData
): List<ResourceType> {
    val wikiResources = wikiData.resources.mapNotNull { rawName ->
        ResourceType.entries.firstOrNull { resource -> resource.matches(rawName) }
            .also { if (it == null) failedWikiResourceLookups.add(rawName) }
    }

    val resources = systemResources[rawPlanet.name] ?: emptyList<ResourceType>().also {
        failedPlanetResourceLookups.add(rawPlanet.name)
    }
    return (resources + wikiResources).toSet().toList()
}

private fun parseResourceLookup(lines: List<String>): Map<String, Map<String, List<ResourceType>>> {
    val columnToResource = lines.first().split(",").subList(4, 48).mapIndexed { i, name ->
        val lookupName = name.lowercase().replace("-", "")
        val resourceType = ResourceType.entries.firstOrNull { resourceType ->
            resourceType.name.lowercase() == lookupName || resourceType.aliases.any { it.lowercase() == lookupName }
        } ?: throw IllegalArgumentException("Could not find resource for $lookupName at $i")
        i to resourceType
    }.toMap()

    val lookup = mutableMapOf<String, MutableMap<String, List<ResourceType>>>()
    lines.forEach { line ->
        val parts = line.split(",").map { it.trim() }
        val system = parts.first()
        val planetName = parts[1]
        val resourcesPresent = parts.subList(4, 48)
        val resources =
            resourcesPresent.mapIndexedNotNull { i, content -> if (content.isNotBlank()) columnToResource[i] else null }

        lookup.putIfAbsent(system, mutableMapOf())
        lookup[system]?.put(planetName, resources)
    }

    return lookup
}

private infix fun String.collapse(other: String): String {
    return if (this == other) this else listOf(this, other).filter { it.isNotBlank() }.joinToString(" ")
}