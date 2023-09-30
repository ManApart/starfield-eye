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
)

@Serializable
data class Quest(
    val name: String,
    val stages: Map<Int, QuestStage>,
) {
    @Transient
    val completed = stages.entries.maxBy { it.key }.value.completed
}

@Serializable
data class QuestStage(
    val id: Int,
    val name: String,
    val completed: Boolean
)