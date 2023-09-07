//TODO - build out parsed data
//TODO - add labels and notes
data class Star(val id: Int)
data class Planet(val id: Int)
data class StarSystem(val star: Star, val planets: Map<Int, Planet>)
