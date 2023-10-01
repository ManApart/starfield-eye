import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
data class PlanetSearchOptions(
    var searchText: String = ""
)

@Serializable
data class GameConnectionSettings(
    var host: String = "localhost",
    var port: String = "55555",
    var pollData: Boolean = false,
    var pollRateInSeconds: Int = 10,
)

@Serializable
data class Quest(
    val name: String,
    val stages: List<QuestStage>,
) {
    @Transient
    val latestState = stages.maxBy { it.id }.state
}

enum class QuestStageState{COMPLETED, DISPLAYED, DORMANT, OTHER}

@Serializable
data class QuestStage(
    val id: Int,
    val name: String,
    val state: QuestStageState
)