import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient


@Serializable
data class PlanetWikiData(
    val name: String = "",
    val type: String = "",
    val temperature: String = "",
    val atmosphere: String = "",
    val magnetosphere: String = "",
    val fauna: String = "",
    val flora: String = "",
    val water: String = "",
    val resources: List<String> = listOf(),
    val traits: List<String> = listOf(),
)

@Serializable
data class MissionWikiData(
    val name: String = "",
    val id: String = "",
    val type: MissionType = MissionType.OTHER,
)

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
    OTHER
}

fun MissionType.isMisc(): Boolean {
    return this in listOf(MissionType.COMPANION, MissionType.MISSION_BOARD, MissionType.RADIANT)
}

fun MissionType.isFaction(): Boolean {
    return this in listOf(
        MissionType.CRIMSON_FLEET,
        MissionType.UNITED_COLONIES,
        MissionType.RYUJIN_INDUSTRIES,
        MissionType.FREESTAR_RANGERS
    )
}

fun MissionType.isCity(): Boolean {
    return this in listOf(
        MissionType.AKILA,
        MissionType.CYDONIA,
        MissionType.NEW_ATLANTIS,
        MissionType.NEON,
        MissionType.CITY_OTHER
    )
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
    val type: MissionType = MissionType.OTHER,
    val stages: List<QuestStage>,
) {
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

