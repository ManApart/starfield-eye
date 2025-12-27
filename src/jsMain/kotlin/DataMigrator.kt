import kotlinx.serialization.Serializable
import org.w3c.dom.HTMLElement


fun starsByLegacy() = galaxy.systems.values.map { it.star }.associateBy { it.rawId }
fun planetsByLegacy() = galaxy.planets.values.associateBy { it.rawId }

fun String.isOldNumberId() = split("-").firstOrNull()?.toIntOrNull() != null
fun String.legacyNumberIdToModern(starsByLegacy: Map<Int?, Star>, planetsByLegacy: Map<Int?, Planet>): String {
    val (star, planet) = split("-").map { it.toInt() }
    val starId = starsByLegacy[star]!!.id
    val planetId = planetsByLegacy[planet]!!.id
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


fun migrateInMemoryStorage(json: String, status: HTMLElement) {
    println("Migrating User Data")
    try {
        val legacy = jsonMapper.decodeFromString<LegacyInMemoryStorage>(json)
        inMemoryStorage = legacy.migrate()
    } catch (e: Exception) {
        status.innerText = "Failed to migrate data! Consider saving json from the console. Then delete user data and rebuild it."
        println(json)
    }
}

fun LegacyInMemoryStorage.migrate(): InMemoryStorage {
    val starsByLegacy = starsByLegacy()
    val planetsByLegacy = planetsByLegacy()
    return InMemoryStorage(
        planetUserInfo.migrate(starsByLegacy, planetsByLegacy), discoveredStars.migrateStars(starsByLegacy), connectionSettings, quests, stats, perks, research, showUndiscovered, outpostResourceView, paintBackgroundStars
    )
}

private fun Map<String, PlanetInfo>.migrate(starsByLegacy: Map<Int?, Star>, planetsByLegacy: Map<Int?, Planet>): MutableMap<String, PlanetInfo> {
    return values.map { old ->
        with(old) {
            val newId = planetId.legacyNumberIdToModern(starsByLegacy, planetsByLegacy)
            PlanetInfo(newId, labels, notes, outPosts, scan)
        }
    }.associateBy { it.planetId }.toMutableMap()
}

private fun Set<Int>.migrateStars(starsByLegacy: Map<Int?, Star>) = mapNotNull { starsByLegacy[it]?.id }.toMutableSet()
