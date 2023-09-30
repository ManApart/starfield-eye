import kotlinx.serialization.Serializable

@Serializable
data class PlanetSearchOptions(
    var searchText: String = ""
)

@Serializable
data class GameConnectionSettings(
    var host: String = "localhost",
    var port: String = "55555",
    var pollData: Boolean = false,
)