import views.filterPlanets

fun searchPlanets(){
    val planets = getPlanets().filterSearch(planetSearchOptions.searchText)
    println("Filtered to ${planets.size}")
    filterPlanets(planets)
}

private fun List<Planet>.filterSearch(searchText: String): List<Planet> {
    println("Search $searchText")
    return if(searchText.isBlank()) this else {
        searchText.lowercase().split(",").fold(this) { acc, s -> filterPlanet(acc, s) }
    }
}

private fun filterPlanet(initial: List<Planet>, searchText: String): List<Planet> {
    return initial.filter { planet ->
        planet.name.lowercase().contains(searchText)
    }
}