import views.filterLife

fun searchLifeSigns() {
    val planets = getPlanets()
    val rawFlora = planets.mapNotNull { floraReference[it.uniqueId]}.flatten()
    val rawFauna = planets.mapNotNull { faunaReference[it.uniqueId]}.flatten()
    val flora = rawFlora.floraSearch(lifeSignsSearchOptions.searchText)
    val fauna = rawFauna.faunaSearch(planetSearchOptions.searchText)
    filterLife(flora, fauna)
}

private fun List<FloraWikiData>.floraSearch(searchText: String): List<FloraWikiData> {
    return if (searchText.isBlank()) this else {
        searchText.lowercase().split(",").fold(this) { acc, s -> acc.filter { floraMatches(it, s.trim()) }}
    }
}
private fun List<FaunaWikiData>.faunaSearch(searchText: String): List<FaunaWikiData> {
    return if (searchText.isBlank()) this else {
        searchText.lowercase().split(",").fold(this) { acc, s -> acc.filter { faunaMatches(it, s.trim()) }}
    }
}

private fun floraMatches(floraWikiData: FloraWikiData, searchText: String): Boolean {
    return true
//    return with(planet) {
//        name.lowercase().contains(searchText)
//                || bodyType == searchText.toIntOrNull()
//                || type.lowercase().contains(searchText)
//                || bodyTypeDescription.lowercase().contains(searchText)
//                || atmosphere.lowercase().contains(searchText)
//                || temperature.lowercase().contains(searchText)
//                || water.lowercase().contains(searchText)
//                || planetClass.lowercase().contains(searchText)
//                || magneticField.lowercase().contains(searchText)
//                || life.lowercase().contains(searchText)
//                || biomes.any { it.lowercase().contains(searchText) }
//                || traits.any { it.lowercase().contains(searchText) }
//                || flora.any { it.lowercase().contains(searchText) }
//                || fauna.any { it.lowercase().contains(searchText) }
//                || inorganicResources.any { it.contains(searchText) }
//                || organicResources.any { it.lowercase().contains(searchText) }
//    }
}

private fun faunaMatches(faunaWikiData: FaunaWikiData, searchText: String): Boolean {
    return true
//    return with(planet) {
//        name.lowercase().contains(searchText)
//                || bodyType == searchText.toIntOrNull()
//                || type.lowercase().contains(searchText)
//                || bodyTypeDescription.lowercase().contains(searchText)
//                || atmosphere.lowercase().contains(searchText)
//                || temperature.lowercase().contains(searchText)
//                || water.lowercase().contains(searchText)
//                || planetClass.lowercase().contains(searchText)
//                || magneticField.lowercase().contains(searchText)
//                || life.lowercase().contains(searchText)
//                || biomes.any { it.lowercase().contains(searchText) }
//                || traits.any { it.lowercase().contains(searchText) }
//                || flora.any { it.lowercase().contains(searchText) }
//                || fauna.any { it.lowercase().contains(searchText) }
//                || inorganicResources.any { it.contains(searchText) }
//                || organicResources.any { it.lowercase().contains(searchText) }
//    }
}
