import kotlinx.serialization.Serializable

@Serializable
data class MiscStats(
    val general: GeneralStats = GeneralStats(),
    val exploration: ExplorationStats = ExplorationStats(),
    val ship: ShipStats = ShipStats(),
    val mission: MissionStats = MissionStats(),
    val combat: CombatStats = CombatStats(),
    val crafting: CraftingStats = CraftingStats(),
    val crime: CrimeStats = CrimeStats(),
)

@Serializable
data class GeneralStats(
    val locationsDiscovered: Int = 0,
    val locationsExplored: Int = 0,
    val daysPassed: Int = 0,
    val hoursSlept: Int = 0,
    val hoursWaited: Int = 0,
    val creditsFound: Int = 0,
    val mostCreditsCarried: Int = 0,
    val containersLooted: Int = 0,
    val magazinesFound: Int = 0,
    val persuasionSuccesses: Int = 0,
    val afflictionsSuffered: Int = 0,
    val foodEaten: Int = 0,
    val dataSlatesRead: Int = 0,
    val skillChallengesCompleted: Int = 0,
    val snowGlobesCollected: Int = 0,
)

@Serializable
data class ExplorationStats(
    val locationsDiscovered: Int = 0,
    val systemsVisited: Int = 0,
    val planetsScanned: Int = 0,
    val planetsFullySurveyed: Int = 0,
    val planetsLandedOn: Int = 0,
    val floraFullyScanned: Int = 0,
    val faunaFullyScanned: Int = 0,
    val stationsDockedWith: Int = 0,
    val shipsDockedWith: Int = 0,
    val uniqueCreaturesScanned: Int = 0,
    val templesFound: Int = 0,
)
@Serializable
data class ShipStats(
    val gravJumps: Int = 0,
    val farthestGravJump: Int = 0,
    val shipsRegistered: Int = 0,
    val maxShipsOwned: Int = 0,
    val mostExpensiveShipOwned: Int = 0,
    val largestShipOwned: Int = 0,
    val largestCrewSize: Int = 0,
    val shipsCustomized: Int = 0,
    val shipBuilderCreditsSpent: Int = 0,
    val shipsSold: Int = 0,
    val shipsPainted: Int = 0,
    val shipModulesBuilt: Int = 0,
)
@Serializable
data class MissionStats(
    val questsCompleted: Int = 0,
    val activitiesCompleted: Int = 0,
    val mainQuestsCompleted: Int = 0,
    val crimsonFleetCollectiveQuestsCompleted: Int = 0,
    val freestarCollectiveQuestsCompleted: Int = 0,
    val ryujinIndustriesQuestsCompleted: Int = 0,
    val unitedColoniesQuestsCompleted: Int = 0,
    val sideQuestsCompleted: Int = 0,
)
@Serializable
data class CombatStats(
    val peopleKilled: Int = 0,
    val creaturesKilled: Int = 0,
    val robotsKilled: Int = 0,
    val turretsKilled: Int = 0,
    val eliteEnemiesKilled: Int = 0,
    val criticalStrikes: Int = 0,
    val sneakAttacks: Int = 0,
    val boostPacksExploded: Int = 0,
    val zeroGKills: Int = 0,
    val powersUsed: Int = 0,
    val scopedKills: Int = 0,
    val maxDamageShot: Int = 0,
    val meleeKills: Int = 0,
    val laserKills: Int = 0,
    val ballisticKills: Int = 0,
    val headShots: Int = 0,
    val explosiveKills: Int = 0,
    val starbornKilled: Int = 0,
    val shipsDestroyed: Int = 0,
    val favoriteWeapon: String = "",
    val favoritePower: String = "",
)
@Serializable
data class CraftingStats(
    val weaponModsCrafted: Int = 0,
    val armorModsCrafted: Int = 0,
    val organicResourcesGathered: Int = 0,
    val inorganicResourcesGathered: Int = 0,
    val chemsCrafted: Int = 0,
    val foodCooked: Int = 0,
    val outpostsBuilt: Int = 0,
    val objectsBuilt: Int = 0,
    val cargoLinksEstablished: Int = 0,
)
@Serializable
data class CrimeStats(
    val locksPicked: Int = 0,
    val pocketsPicked: Int = 0,
    val itemsStolen: Int = 0,
    val assaults: Int = 0,
    val murders: Int = 0,
    val trespasses: Int = 0,
    val timesArrested: Int = 0,
    val mostContrabandCarried: Int = 0,
    val timesCaughtWithContraband: Int = 0,
    val actsOfPiracy: Int = 0,
    val totalLifetimeBounty: Int = 0,
    val largestBounty: Int = 0,
)
