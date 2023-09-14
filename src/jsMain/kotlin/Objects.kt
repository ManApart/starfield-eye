import kotlinx.serialization.Serializable

@Serializable
data class PlanetSearchOptions(
    var searchText: String = ""
)