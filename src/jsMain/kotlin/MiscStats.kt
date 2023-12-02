import docking.RawPollResponse
import kotlinx.serialization.Serializable

@Serializable
data class MiscStats(
    val general: List<MiscStatItem> = listOf(),
    val exploration: List<MiscStatItem> = listOf(),
    val ship: List<MiscStatItem> = listOf(),
    val mission: List<MiscStatItem> = listOf(),
    val combat: List<MiscStatItem> = listOf(),
    val crafting: List<MiscStatItem> = listOf(),
    val crime: List<MiscStatItem> = listOf(),
)

@Serializable
data class MiscStatItem(
    val name: String,
    val value: String,
    val achievementName: String? = null,
    val achievementTotal: Int? = null,
)

fun RawPollResponse.stat(key: String, achievementName: String? = null, achievementTotal: Int? = null) = MiscStatItem(
    key.split(" ").joinToString(" ") { it.capitalize() },
    getMiscStatInt(key).toString(),
    achievementName,
    achievementTotal
)
