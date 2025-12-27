import io.ktor.client.utils.EmptyContent.status
import kotlinx.serialization.Serializable
import org.w3c.dom.HTMLElement

//TODO - missing Eridani planets and children

fun starsByLegacy() = galaxy.systems.values.map { it.star }.associateBy { it.rawId }
fun planetsByLegacy(): Map<String, Map<Int?, Planet>> = galaxy.systems.values.associate { sys ->
    sys.star.id to sys.planets.values.associateBy { it.rawId }
}

fun String.isOldNumberId() = split("-").firstOrNull()?.toIntOrNull() != null
fun String.legacyNumberIdToModern(starsByLegacy: Map<Int?, Star>, planetsByLegacy: Map<String, Map<Int?, Planet>>): String {
    val (star, planet) = split("-").map { it.toInt() }
    val starId = starsByLegacy[star]?.id ?: throw IllegalArgumentException("Could not find $star")
    val planetId = planetsByLegacy[starId]?.get(planet)?.id  ?: throw IllegalArgumentException("Could not find $starId $planet in ${planetsByLegacy[starId]?.keys}")
    return "$starId:$planetId"
}

@Serializable
data class LegacyInMemoryStorage(
    val planetUserInfo: MutableMap<String, PlanetInfo> = mutableMapOf(),
    val discoveredStars: MutableSet<Int> = mutableSetOf(),
    val connectionSettings: GameConnectionSettings = GameConnectionSettings(),
    var quests: List<Quest> = listOf(),
    var stats: MiscStats = MiscStats(),
    var perks: MutableMap<String, Int> = mutableMapOf(),
    var research: MutableMap<String, Int> = mutableMapOf(),
    var showUndiscovered: Boolean? = true,
    var outpostResourceView: Boolean? = false,
    var paintBackgroundStars: Boolean? = null,
)

fun migrateInMemoryStorage(json: String): Boolean {
    println("Migrating User Data")
    return try {
        val legacy = jsonMapper.decodeFromString<LegacyInMemoryStorage>(json)
        inMemoryStorage = legacy.migrate()
        true
    } catch (e: Exception) {
        println(e.stackTraceToString())
        println(json)
        false
    }
}

fun LegacyInMemoryStorage.migrate(): InMemoryStorage {
    val starsByLegacy = starsByLegacy()
    val planetsByLegacy = planetsByLegacy()
    return InMemoryStorage(
        planetUserInfo.migrate(starsByLegacy, planetsByLegacy),
        discoveredStars.migrateStars(starsByLegacy),
        connectionSettings,
        quests,
        stats,
        perks,
        research,
        showUndiscovered,
        outpostResourceView,
        paintBackgroundStars
    )
}

private fun Map<String, PlanetInfo>.migrate(starsByLegacy: Map<Int?, Star>, planetsByLegacy: Map<String, Map<Int?, Planet>>): MutableMap<String, PlanetInfo> {
    return values.map { old ->
        with(old) {
            val newId = planetId.legacyNumberIdToModern(starsByLegacy, planetsByLegacy)
            println(old.planetId + " -> " + newId)
            PlanetInfo(newId, labels, notes, outPosts, scan)
        }
    }.associateBy { it.planetId }.toMutableMap()
}

private fun Set<Int>.migrateStars(starsByLegacy: Map<Int?, Star>) = mapNotNull { starsByLegacy[it]?.id }.toMutableSet()
