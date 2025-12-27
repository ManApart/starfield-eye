import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import wikiScraper.urlIdToId
import java.io.File

val jsonMapper = kotlinx.serialization.json.Json {
    ignoreUnknownKeys = true
    prettyPrint = true
    encodeDefaults = false
}

private val failedPlanetResourceLookups = mutableSetOf<String>()
private val failedSystemResourceLookups = mutableSetOf<String>()
private val failedWikiResourceLookups = mutableSetOf<String>()

fun main() {
    val rawBiomes = File("./raw-data/biomedata.csv").readLines().drop(2).map { it.toBiome() }.groupBy { it.starId }
    val rawStars = File("./raw-data/stars.csv").readLines().drop(2).map { it.toStar() }
    val rawPlanets = File("./raw-data/galaxy.csv").readLines().drop(2).map { it.toPlanet() }.groupBy { it.starId }
    val resourceLookup = parseResourceLookup(File("./raw-data/raw-resources.csv").readLines())
    val starWikiDataFile = File("raw-data/star-wiki-data.json")
    val planetWikiDataFile = File("raw-data/planet-wiki-data.json")
    val starWikiData = if (starWikiDataFile.exists()) {
        jsonMapper.decodeFromString<Map<String, StarWikiData>>(starWikiDataFile.readText()).toMutableMap()
    } else mapOf()
    val planetWikiData = if (planetWikiDataFile.exists()) {
        jsonMapper.decodeFromString<Map<String, PlanetWikiData>>(planetWikiDataFile.readText()).toMutableMap()
    } else mapOf()
    val floraWikiData =
        jsonMapper.decodeFromString<List<FloraWikiData>>(File("./src/jsMain/resources/flora-wiki-data.json").readText())
            .filter { it.planetId != null }
            .groupBy { it.planetId!! }
    val faunaWikiData =
        jsonMapper.decodeFromString<List<FaunaWikiData>>(File("./src/jsMain/resources/fauna-wiki-data.json").readText())
            .filter { it.planetId != null }
            .groupBy { it.planetId!! }

    val stars = matchStars(starWikiData.values, rawStars)
    val planetWikiToRaw = matchPlanets(planetWikiData.values.toList(), rawPlanets.values.flatten().associateBy { it.name })

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

    val systems = stars.associate { (wikiStar, rawStar) ->
        val id = wikiStar?.id ?: rawStar!!.starId.toString()
        val biomes = rawStar?.starId?.let { rawBiomes[it] } ?: emptyList()
        val resources = rawStar?.name?.let { resourceLookup[it] } ?: emptyMap<String, List<ResourceType>>().also {
            failedSystemResourceLookups.add(id)
        }
        id to parseSystem(wikiStar, rawStar, planetWikiData, planetWikiToRaw, biomes, floraWikiData, faunaWikiData, resources)
    }

    println()
    if(failedSystemResourceLookups.isNotEmpty()) println("Failed to find resources for ${failedSystemResourceLookups.size} systems: ${failedSystemResourceLookups.joinToString()}.")
    if(failedPlanetResourceLookups.isNotEmpty()) println("Failed to find resources for ${failedPlanetResourceLookups.size} planets: ${failedPlanetResourceLookups.joinToString()}.")
    if(failedWikiResourceLookups.isNotEmpty()) println("Failed to find resources for ${failedWikiResourceLookups.size} wiki resources: ${failedWikiResourceLookups.joinToString()}.")

    File("src/jsMain/resources/data.json").writeText(jsonMapper.encodeToString(Galaxy(systems, galaxySummary)))
}

private fun matchStars(starWikiData: Collection<StarWikiData>, rawStars: List<RawStar>): List<Pair<StarWikiData?, RawStar?>> {
    val starsByCatalog = rawStars.associateBy { it.catalogueId.lowercase() }
    val starsByName = rawStars.associateBy { it.name.lowercase() }
    val stars = starWikiData.map { wikiStar ->
        (starsByName[wikiStar.name.lowercase()] ?: starsByCatalog[wikiStar.catalogueId.lowercase()]).let { raw ->
            if (raw == null) {
                println("Unable to find raw star for ${wikiStar.name}")
            }
            wikiStar to raw
        }
    }
    val missing = rawStars - stars.map { it.second }.toSet()
    if (missing.isNotEmpty()) println("Missing Star Wiki for ${missing.joinToString { it?.name ?: "" }}")

    return stars + missing.map { null to it }
}

private fun matchPlanets(wikiPlanets: List<PlanetWikiData>, rawPlanetsByName: Map<String, RawPlanet>): Map<PlanetWikiData, RawPlanet> {
    val missingRaw = mutableListOf<String>()
    return wikiPlanets.mapNotNull { wiki ->
        val raw = rawPlanetsByName[wiki.name]
        if (raw == null) {
            missingRaw.add(wiki.name)
            null
        } else wiki to raw
    }.toMap().also {
        val missing = rawPlanetsByName.values - it.values.toSet()
        if (missing.isNotEmpty()) {
            println("Missing ${missing.size} Planet Wiki for raw names: ${missing.joinToString { m -> m.name }}")
        }
        if (missingRaw.isNotEmpty()) {
            println("Unable to find ${missingRaw.size} raw planets for wiki names: ${missingRaw.joinToString()}")
        }
    }
}

private fun parseSystem(
    wikiStar: StarWikiData?,
    rawStar: RawStar?,
    planetWikiData: Map<String, PlanetWikiData>,
    planetWikiToRaw: Map<PlanetWikiData, RawPlanet>,
    rawBiomes: List<RawBiome>,
    floraResources: Map<String, List<FloraWikiData>>,
    faunaResources: Map<String, List<FaunaWikiData>>,
    systemResources: Map<String, List<ResourceType>>,
): StarSystem {
    val star = parseStar(wikiStar, rawStar)
    val pos = rawStar?.let { Pos(it.x, it.y, it.z) } ?: Pos()
    val wikiPlanetList = wikiStar?.planetIds?.mapNotNull { planetWikiData[it.urlIdToId()] } ?: emptyList()
    val planets = parsePlanets(star, wikiPlanetList, planetWikiToRaw, rawBiomes, systemResources, floraResources, faunaResources)
    val nestedPlanets = parseNestedPlanets(planets)
    return StarSystem(star, pos, planets, nestedPlanets)
}

private fun parseStar(w: StarWikiData?, r: RawStar?): Star {
    return Star(
        w?.id ?: r!!.id,
        r?.starId,
        w?.wikiUrlId ?: r!!.wikiUrlId,
        w?.catalogueId ?: r!!.catalogueId,
        w?.name ?: r!!.name,
        w?.level ?: 0,
        w?.spectral ?: r!!.spectral,
        w?.temp ?: r!!.temp.toString(),
        w?.mass ?: r!!.mass.toString(),
        w?.radius ?: r!!.radius,
        w?.magnitude ?: r!!.magnitude,
    )
}

private fun parsePlanets(
    star: Star,
    planetWikis: List<PlanetWikiData>,
    planetWikiToRaw: Map<PlanetWikiData, RawPlanet>,
    rawBiomes: List<RawBiome>,
    systemResources: Map<String, List<ResourceType>>,
    floraResources: Map<String, List<FloraWikiData>>,
    faunaResources: Map<String, List<FaunaWikiData>>
): Map<String, Planet> {
    return planetWikis.associate { w ->
        val r = planetWikiToRaw[w] ?: RawPlanet(star.rawId ?: 0, 0, 0, "", "", 0, 0f, 0f, 0f, 0f, 0f, 0f, 0, 0, 0, "", "", "", "")
        val biomes = rawBiomes.filter { it.planetId == r.planetId }.map { it.name }

        val inorganicResources = determineResources(r, systemResources, w)
        val uniqueId = "${star.id}:${w.id}"

        val floraList = floraResources[uniqueId]?.map { it.resource } ?: listOf()
        val faunaList = faunaResources[uniqueId]?.map { it.resource } ?: listOf()
        val organicResources = (floraList + faunaList).sorted().toSet()

        val flora = w.flora.replace("[[#Flora|]]", "")
        val fauna = w.fauna.replace("[[#Fauna|]]", "")

        val planet = with(r) {
            Planet(
                w.id,
                r.planetId,
                star.id,
                star.id,
                w.name,
                planetClass,
                bodyType,
                w.type,
                radius,
                density,
                mass,
                gravity,
                year,
                day,
                asteroids,
                rings,
                w.atmosphere,
                heat,
                w.temperature,
                type,
                magneticField collapse w.magnetosphere,
                w.water,
                life,
                settled,
                flora,
                fauna,
                biomes,
                w.traits,
                w.moonIds,
                organicResources,
                inorganicResources
            )
        }
        w.id to planet
    }
}

private fun determineResources(
    rawPlanet: RawPlanet?,
    systemResources: Map<String, List<ResourceType>>,
    wikiData: PlanetWikiData
): Set<ResourceType> {
    val wikiResources = wikiData.resources.mapNotNull { rawName ->
        ResourceType.entries.firstOrNull { resource -> resource.matches(rawName) }
            .also { if (it == null) failedWikiResourceLookups.add(rawName) }
    }

    val resources = rawPlanet?.let { systemResources[it.name] } ?: emptyList<ResourceType>().also {
        failedPlanetResourceLookups.add(wikiData.name)
    }
    return (resources + wikiResources).sortedByDescending { it.name }.toSet()
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


private fun parseNestedPlanets(planets: Map<String, Planet>): Map<String, List<String>> {
    val nestedPlanets = mutableMapOf<String, MutableList<String>>()
    val children = mutableSetOf<Planet>()
    planets.values.forEach { parent ->
        if (parent.moonIds.isNotEmpty()) nestedPlanets[parent.id] = mutableListOf()
        parent.moonIds.forEach { moonId ->
            planets[moonId]?.let { moon ->
                moon.parentId = parent.id
                nestedPlanets[parent.id]?.add(moon.id)
                children.add(moon)
            } ?: println("Unable to find moon $moonId referenced by ${parent.id} in ${parent.parentId}")
        }
    }
    //Add planets who have no children
    (planets.values.toSet() - children).forEach { nestedPlanets.putIfAbsent(it.id, mutableListOf()) }
    return nestedPlanets.toSortedMap()
}

private infix fun String.collapse(other: String): String {
    return if (this == other) this else listOf(this, other).filter { it.isNotBlank() }.joinToString(" ")
}
