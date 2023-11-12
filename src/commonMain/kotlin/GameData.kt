import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

enum class MissionType {
    MAIN,
    NEW_ATLANTIS,
    AKILA,
    NEON,
    CYDONIA,
    CRIMSON_FLEET,
    COMPANION,
    CITY_OTHER,
    FREESTAR_RANGERS,
    UNITED_COLONIES,
    RYUJIN_INDUSTRIES,
    MISSION_BOARD,
    RADIANT,
    OTHER;

    fun isMisc(): Boolean {
        return this in listOf(COMPANION, MISSION_BOARD, RADIANT)
    }

    fun isFaction(): Boolean {
        return this in listOf(
            CRIMSON_FLEET,
            UNITED_COLONIES,
            RYUJIN_INDUSTRIES,
            FREESTAR_RANGERS
        )
    }

    fun isCity(): Boolean {
        return this in listOf(
            AKILA,
            CYDONIA,
            NEW_ATLANTIS,
            NEON,
            CITY_OTHER
        )
    }
}

fun String.toMissionType(): MissionType {
    with(this) {
        return when {
            startsWith("MQ") -> MissionType.MAIN
            startsWith("CF") -> MissionType.CRIMSON_FLEET
            startsWith("FC") -> MissionType.FREESTAR_RANGERS
            startsWith("RI") -> MissionType.RYUJIN_INDUSTRIES
            startsWith("UC") -> MissionType.UNITED_COLONIES
            startsWith("COM") -> MissionType.COMPANION
            startsWith("City_Akila") || this.startsWith("FFAkila") -> MissionType.AKILA
            startsWith("City_Neon") || this.startsWith("FFNeon") -> MissionType.NEON
            startsWith("City_Cydonia") || this.startsWith("FFCydonia") -> MissionType.CYDONIA
            startsWith("City_NewAtlantis") || this.startsWith("FFNewAtlantis") -> MissionType.NEW_ATLANTIS
            startsWith("MB") -> MissionType.MISSION_BOARD
            startsWith("RQ") || this.startsWith("RAD") -> MissionType.RADIANT
            startsWith("City") -> MissionType.CITY_OTHER
            else -> MissionType.OTHER
        }
    }
}

@Serializable
data class Quest(
    val name: String,
    val id: String = "",
    val batIndex: Int = 0,
    val instance: Int = 0,
    val type: MissionType = MissionType.OTHER,
    val stages: List<QuestStage>,
) {
    @Transient
    val uniqueId = "$name-$id-$instance-$batIndex"

    @Transient
    val latestState = stages.maxBy { it.id }.state
}

enum class QuestStageState { COMPLETED, DISPLAYED, DORMANT, OTHER }

@Serializable
data class QuestStage(
    val id: Int,
    val name: String,
    val state: QuestStageState
)

enum class Temperament { PEACEFUL, SKITTISH, WARY, DEFENSIVE, TERRITORIAL, FEARLESS, AGGRESSIVE, UNKNOWN }
