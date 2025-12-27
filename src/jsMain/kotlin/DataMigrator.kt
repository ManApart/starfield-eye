import kotlinx.serialization.Serializable
import org.w3c.dom.HTMLElement


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
    try {

    } catch (e: Exception) {
        status.innerText = "Failed to migrate data! Consider saving json from the console. Then delete user data and rebuild it."
        println(json)
    }
}

fun isLegacyPictureStorage() = pictureStorage.keys.firstOrNull()?.split("/")?.getOrNull(1)?.isOldNumberId() ?: false
fun String.isOldNumberId() = split("-").firstOrNull()?.toIntOrNull() != null

fun migratePictures(pictures: MutableMap<String, String>, status: HTMLElement) {
    println("Migrating ${pictures.size} pictures")
    try {
        val starsByLegacy = galaxy.systems.values.map { it.star }.associateBy { it.rawId }
        val planetsByLegacy = galaxy.planets.values.associateBy { it.rawId }
        pictureStorage = pictures.entries.associate { (key, url) ->
            val parts = key.split("/")
            val newKey = if (parts[0] == "outposts") "${parts[0]}/${parts[1].legacyNumberIdToModern(starsByLegacy, planetsByLegacy)}/${parts[2]}" else key
            newKey to url
        }.toMutableMap()
    } catch (e: Exception) {
        status.innerText = "Failed to migrate pictures! Consider saving json from the console. Then delete user data and rebuild it."
    }
}

private fun String.legacyNumberIdToModern(starsByLegacy: Map<Int?, Star>, planetsByLegacy: Map<Int?, Planet>): String {
    val (star, planet) = split("-").map { it.toInt() }
    val starId = starsByLegacy[star]!!.id
    val planetId = planetsByLegacy[planet]!!.id
    return "$starId:$planetId"
}
