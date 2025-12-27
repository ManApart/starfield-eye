fun isLegacyPictureStorage() = pictureStorage.keys.firstOrNull()?.split("/")?.getOrNull(1)?.isOldNumberId() ?: false

fun migratePictures(pictures: MutableMap<String, String>): Boolean {
    println("Migrating ${pictures.size} pictures")
    return try {
        val starsByLegacy = starsByLegacy()
        val planetsByLegacy = planetsByLegacy()
        pictureStorage = pictures.entries.associate { (key, url) ->
            val parts = key.split("/")
            val newKey = if (parts[0] == "outposts") "${parts[0]}/${parts[1].legacyNumberIdToModern(starsByLegacy, planetsByLegacy)}/${parts[2]}" else key
            newKey to url
        }.toMutableMap()
        true
    } catch (e: Exception) {
        false
    }
}
