import views.filterPlanets

fun searchPlanets() {
    val planets = getPlanets().filterSearch(inMemoryStorage.planetSearchOptions.searchText)
    println("${planets.size} planets match ${inMemoryStorage.planetSearchOptions.searchText}")
    filterPlanets(planets)
    persistMemory()
}

private fun List<Planet>.filterSearch(searchText: String): List<Planet> {
    return if (searchText.isBlank()) this else {
        searchText.lowercase().split(",").fold(this) { acc, s -> filterPlanet(acc, s.trim()) }
    }
}

private fun filterPlanet(initial: List<Planet>, searchText: String): List<Planet> {
    return initial.filter { planet ->
        planetMatches(planet, searchText) || planetInfoMatches(planet, searchText)
    }
}

private fun planetMatches(planet: Planet, searchText: String): Boolean {
    return with(planet) {
        name.lowercase().contains(searchText)
                || bodyType == searchText.toIntOrNull()
                || type.lowercase().contains(searchText)
                || planetClass.lowercase().contains(searchText)
                || magneticField.lowercase().contains(searchText)
                || biomes.any { it.lowercase().contains(searchText) }
                || resources.any { it.name.lowercase().contains(searchText) || it.readableName.lowercase().contains(searchText) }
    }
}

private fun planetInfoMatches(planet: Planet, searchText: String): Boolean {
    val info = inMemoryStorage.planetUserInfo[planet.uniqueId]
    return info != null && with(info) {
        labels.any { it.name.lowercase().contains(searchText) }
                || outPosts.isNotEmpty() && "outposts".contains(searchText)
                || outPosts.any { it.lowercase().contains(searchText) }
    }
}
