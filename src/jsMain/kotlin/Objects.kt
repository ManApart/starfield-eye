import kotlinx.serialization.Serializable

@Serializable
data class PlanetSearchOptions(
    var searchText: String = ""
)

@Serializable
data class LifeSignsSearchOptions(
    var searchText: String = ""
)

@Serializable
data class MissionSearchOptions(
    var searchText: String = "",
    var showCompleted: Boolean? = false,
    var types: List<MissionType> = MissionType.entries
)

@Serializable
data class GameConnectionSettings(
    var host: String = "localhost",
    var port: String = "55555",
    var pollData: Boolean = false,
    var pollRateInSeconds: Int = 60 * 5,
)

data class PollResponse(
    val quests: List<Quest>,
    val stats: MiscStats? = null,
) {
    val success = quests.isNotEmpty() && stats != null
}
