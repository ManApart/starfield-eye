import views.lifeSigns.filterLife

fun searchLifeSigns() {
    val planets = getPlanets()
    val rawFlora = planets.mapNotNull { floraReference[it.uniqueId]}.flatten()
    val rawFauna = planets.mapNotNull { faunaReference[it.uniqueId]}.flatten()
    val flora = rawFlora.floraSearch(lifeSignsSearchOptions.searchText)
    val fauna = rawFauna.faunaSearch(lifeSignsSearchOptions.searchText)
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
    return with(floraWikiData) {
        name.lowercase().contains(searchText)
                || resource.lowercase().contains(searchText)
                || biomes.any { it.lowercase().contains(searchText) }
                || other.values.any { it.lowercase().contains(searchText) }
    }
}

private fun faunaMatches(faunaWikiData: FaunaWikiData, searchText: String): Boolean {
    return with(faunaWikiData) {
        name.lowercase().contains(searchText)
                || resource.lowercase().contains(searchText)
                || temperament.name.lowercase().contains(searchText)
                || biomes.any { it.lowercase().contains(searchText) }
                || abilities.any { it.lowercase().contains(searchText) }
                || other.values.any { it.lowercase().contains(searchText) }
    }
}
