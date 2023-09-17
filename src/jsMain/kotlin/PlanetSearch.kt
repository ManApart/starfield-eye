import views.filterPlanets

fun searchPlanets() {
    val planets = getPlanets().filterSearch(inMemoryStorage.planetSearchOptions.searchText)
    println("Filtered to ${planets.size}")
    filterPlanets(planets)
    persistMemory()
}

private fun List<Planet>.filterSearch(searchText: String): List<Planet> {
    println("Search $searchText")
    return if (searchText.isBlank()) this else {
        searchText.lowercase().split(",").fold(this) { acc, s -> filterPlanet(acc, s.trim()) }
    }
}

private fun filterPlanet(initial: List<Planet>, searchText: String): List<Planet> {
    return initial.filter { planet ->
        planet.name.lowercase().contains(searchText)
                || planet.bodyType == searchText.toIntOrNull()
                || planet.type.lowercase().contains(searchText)
                || planet.planetClass.lowercase().contains(searchText)
                || planet.magneticField.lowercase().contains(searchText)
                || planet.biomes.any { it.lowercase().contains(searchText) }
                || planet.resources.any { it.name.lowercase().contains(searchText) || it.readableName.lowercase().contains(searchText) }
    }
}