import views.filterPlanets

fun searchPlanets() {
    val (planets, additionalStars) = getPlanets().filterSearch(planetSearchOptions.searchText)
    filterPlanets(planets, additionalStars)
}

private fun List<Planet>.filterSearch(searchText: String): Pair<List<Planet>, List<Int>> {
    return if (searchText.isBlank()) this to listOf() else {
        val terms = searchText.lowercase().split(",")
        val poi = poiMatches(terms)
        println("Search found poi: $poi")
        val planets = terms.fold(this) { acc, s -> filterPlanet(acc, s.trim(), poi) }
        //TODO - get list of additional stars as well
        planets to listOf()
    }
}

private fun filterPlanet(initial: List<Planet>, searchText: String, poi: List<PointOfInterest>): List<Planet> {
    return initial.filter { planet ->
        planetMatches(planet, searchText, poi) || planetInfoMatches(planet, searchText)
    }
}

private fun planetMatches(planet: Planet, searchText: String, poi: List<PointOfInterest>): Boolean {
    return with(planet) {
        name.lowercase().contains(searchText)
                || bodyType == searchText.toIntOrNull()
                || type.lowercase().contains(searchText)
                || bodyTypeDescription.lowercase().contains(searchText)
                || atmosphere.lowercase().contains(searchText)
                || temperature.lowercase().contains(searchText)
                || water.lowercase().contains(searchText)
                || planetClass.lowercase().contains(searchText)
                || magneticField.lowercase().contains(searchText)
                || life.lowercase().contains(searchText)
                || poi.any { it.planet?.lowercase() == name.lowercase()}
//                || poi.any { it.planet == null && it.starSystem == planet.parentId}
                || biomes.any { it.lowercase().contains(searchText) }
                || traits.any { it.lowercase().contains(searchText) }
                || flora.any { it.lowercase().contains(searchText) }
                || fauna.any { it.lowercase().contains(searchText) }
                || inorganicResources.any { it.contains(searchText) }
                || organicResources.any { it.lowercase().contains(searchText) }
    }
}

private fun planetInfoMatches(planet: Planet, searchText: String): Boolean {
    val info = inMemoryStorage.planetUserInfo[planet.uniqueId]
    return info != null && with(info) {
        labels.any { it.name.lowercase().contains(searchText) }
                || outPosts.isNotEmpty() && "outposts".contains(searchText)
                || outPosts.any { it.name.lowercase().contains(searchText) }
    }
}

private fun poiMatches(terms: List<String>) = poiReference.values.flatten().filter { poi ->  terms.any { term -> poi.name.lowercase().contains(term) } }
