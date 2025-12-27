import org.w3c.dom.HTMLElement

fun isLegacyPictureStorage() = pictureStorage.keys.firstOrNull()?.split("/")?.getOrNull(1)?.isOldNumberId() ?: false

fun migratePictures(pictures: MutableMap<String, String>, status: HTMLElement) {
    println("Migrating ${pictures.size} pictures")
    try {
        val starsByLegacy = starsByLegacy()
        val planetsByLegacy = planetsByLegacy()
        pictureStorage = pictures.entries.associate { (key, url) ->
            val parts = key.split("/")
            val newKey = if (parts[0] == "outposts") "${parts[0]}/${parts[1].legacyNumberIdToModern(starsByLegacy, planetsByLegacy)}/${parts[2]}" else key
            newKey to url
        }.toMutableMap()
    } catch (e: Exception) {
        status.innerText = "Failed to migrate pictures! Consider saving json from the console. Then delete user data and rebuild it."
    }
}
