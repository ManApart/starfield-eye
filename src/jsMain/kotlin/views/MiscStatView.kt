package views

import MiscStats
import Quest
import QuestStageState
import inMemoryStorage
import kotlinx.browser.window
import kotlinx.html.*
import kotlinx.html.js.onClickFunction
import org.w3c.dom.HTMLElement
import pollHook
import replaceElement

fun miscStatView() {
    window.history.pushState(null, "null", "#misc-stats")
    replaceElement {
        div {
            id = "misc-stat-view"
            div {
                id = "nav"
                button {
                    +"Back to Galaxy"
                    onClickFunction = {
                        renderGalaxy()
                    }
                }
            }
            div { id = "sections" }
        }
    }
    if (inMemoryStorage.quests.isEmpty()) {
        needsDocking()
    } else {
        displayStats(inMemoryStorage.stats)
    }
    pollHook = ::receivePoll
    pollData()
}

private fun needsDocking() {
    replaceElement("sections") {
        div("section-view-box") {
            id = "misc-stat-explanation"
            h2 { +"Misc Stats" }
            div("accent-line") { +"See if he'll play the good man's game" }

            p { +"Use dock to connect to the game and see near realtime updates to your miscellaneous stats, or track your stats even when not playing!" }
            button {
                id = "dock-button"
                +"Dock"
                title = "Change Settings"
                onClickFunction = { dockView() }
            }
        }
    }
}

private fun receivePoll(success: Boolean) {
    if (success) displayStats(inMemoryStorage.stats)
}

private data class MiscStatItem(
    val name: String,
    val value: Any,
    val achievementName: String? = null,
    val achievementTotal: Int? = null,
)

private fun displayStats(stats: MiscStats) {
    replaceElement("sections") {
        div("section-view-box") {
            id = "misc-stats"
            h2 { +"Miscellaneous Stats" }
            div("accent-line") { +"See if he'll play the good man's game" }
        }
        with(stats.general) {
            statsSection(
                "General",
                MiscStatItem("Locations Discovered", locationsDiscovered),
                MiscStatItem("Locations Explored", locationsExplored),
                MiscStatItem("Days Passed", daysPassed),
                MiscStatItem("Hours Slept", hoursSlept),
                MiscStatItem("Hours Waited", hoursWaited),
                MiscStatItem("Credits Found", creditsFound),
                MiscStatItem("Most Credits Carried", mostCreditsCarried),
                MiscStatItem("Containers Looted", containersLooted),
                MiscStatItem("Magazines Found", magazinesFound, "Thirst for Knowledge", 20),
                MiscStatItem("Persuasion Successes", persuasionSuccesses),
                MiscStatItem("Afflictions Suffered", afflictionsSuffered),
                MiscStatItem("Food Eaten", foodEaten),
                MiscStatItem("Data Slates Read", dataSlatesRead),
                MiscStatItem("Skill Challenges Completed", skillChallengesCompleted),
                MiscStatItem("Snow Globes Collected", snowGlobesCollected),
            )
        }
        with(stats.exploration) {
            statsSection(
                "Exploration",
                MiscStatItem("Systems Visited", systemsVisited, "Stellar Cartography", 20),
                MiscStatItem("Planets Scanned", planetsScanned),
                MiscStatItem("Planets Fully Surveyed", planetsFullySurveyed),
                MiscStatItem("Planets Landed On", planetsLandedOn, "Boots on the Ground", 100),
                MiscStatItem("Flora Fully Scanned", floraFullyScanned),
                MiscStatItem("Fauna Fully Scanned", faunaFullyScanned),
                MiscStatItem("Stations Docked With", stationsDockedWith),
                MiscStatItem("Ships Docked With", shipsDockedWith),
                MiscStatItem("Unique Creatures Scanned", uniqueCreaturesScanned),
                MiscStatItem("Temples Found", templesFound),
            )
        }
        with(stats.ship) {
            statsSection(
                "Ship",
                MiscStatItem("Grav Jumps", gravJumps),
                MiscStatItem("Farthest Grav Jump", farthestGravJump),
                MiscStatItem("Ships Registered", shipsRegistered),
                MiscStatItem("Max Ships Owned", maxShipsOwned),
                MiscStatItem("Most Expensive Ship Owned", mostExpensiveShipOwned),
                MiscStatItem("Largest Ship Owned", largestShipOwned),
                MiscStatItem("Largest Crew Size", largestCrewSize),
                MiscStatItem("Ships Customized", shipsCustomized),
                MiscStatItem("Ship Builder Credits Spent", shipBuilderCreditsSpent),
                MiscStatItem("Ships Sold", shipsSold),
                MiscStatItem("Ships Painted", shipsPainted),
                MiscStatItem("Ship Modules Built", shipModulesBuilt),
            )
        }
        with(stats.mission) {
            statsSection(
                "Mission",
                MiscStatItem("Quests Completed", questsCompleted),
                MiscStatItem("Activities Completed", activitiesCompleted, "Fixer", 30),
                MiscStatItem("Main Quests Completed", mainQuestsCompleted),
                MiscStatItem("Crimson Fleet Collective Quests Completed", crimsonFleetCollectiveQuestsCompleted),
                MiscStatItem("Freestar Collective Quests Completed", freestarCollectiveQuestsCompleted),
                MiscStatItem("Ryujin Industries Quests Completed", ryujinIndustriesQuestsCompleted),
                MiscStatItem("United Colonies Quests Completed", unitedColoniesQuestsCompleted),
                MiscStatItem("Side Quests Completed", sideQuestsCompleted, "Privateer", 30),
            )
        }
        with(stats.combat) {
            statsSection(
                "Combat",
                MiscStatItem("People Killed", peopleKilled, "Dark Matter", 300),
                MiscStatItem("Creatures Killed", creaturesKilled, "Another Bug Hunt", 300),
                MiscStatItem("Robots Killed", robotsKilled),
                MiscStatItem("Turrets Killed", turretsKilled),
                MiscStatItem("Elite Enemies Killed", eliteEnemiesKilled),
                MiscStatItem("Critical Strikes", criticalStrikes),
                MiscStatItem("Sneak Attacks", sneakAttacks),
                MiscStatItem("Boost Packs Exploded", boostPacksExploded),
                MiscStatItem("Zero G Kills", zeroGKills),
                MiscStatItem("Powers Used", powersUsed),
                MiscStatItem("Scoped Kills", scopedKills),
                MiscStatItem("Max Damage Shot", maxDamageShot),
                MiscStatItem("Melee Kills", meleeKills),
                MiscStatItem("Laser Kills", laserKills),
                MiscStatItem("Ballistic Kills", ballisticKills),
                MiscStatItem("Head Shots", headShots),
                MiscStatItem("Explosive Kills", explosiveKills),
                MiscStatItem("Starborn Killed", starbornKilled, "War of Angels", 20),
                MiscStatItem("Ships Destroyed", shipsDestroyed),
                MiscStatItem("Favorite Weapon", favoriteWeapon),
                MiscStatItem("Favorite Power", favoritePower),
            )
        }
        with(stats.crafting) {
            statsSection(
                "Crafting",
                MiscStatItem("Weapon Mods Crafted", weaponModsCrafted, "Soldier of Fortune", 50),
                MiscStatItem("Armor Mods Crafted", armorModsCrafted),
                MiscStatItem("Organic Resources Gathered", organicResourcesGathered, "Life Begets Life", 500),
                MiscStatItem("Inorganic Resources Gathered", inorganicResourcesGathered, "Rock Collection", 500),
                MiscStatItem("Chems Crafted", chemsCrafted),
                MiscStatItem("Food Cooked", foodCooked),
                MiscStatItem("Outposts Built", outpostsBuilt),
                MiscStatItem("Objects Built", objectsBuilt),
                MiscStatItem("Cargo Links Established", cargoLinksEstablished, "Shipping Magnate", 5),
            )
        }
        with(stats.crime) {
            statsSection(
                "Crime",
                MiscStatItem("Locks Picked", locksPicked, "Cyber Jockey", 50),
                MiscStatItem("Pockets Picked", pocketsPicked),
                MiscStatItem("Items Stolen", itemsStolen),
                MiscStatItem("Assaults", assaults),
                MiscStatItem("Murders", murders),
                MiscStatItem("Trespasses", trespasses),
                MiscStatItem("Times Arrested", timesArrested),
                MiscStatItem("Most Contraband Carried", mostContrabandCarried),
                MiscStatItem("Times Caught With Contraband", timesCaughtWithContraband),
                MiscStatItem("Acts Of Piracy", actsOfPiracy),
                MiscStatItem("Total Lifetime Bounty", totalLifetimeBounty),
                MiscStatItem("Largest Bounty", largestBounty),
            )
        }
    }
}

private fun TagConsumer<HTMLElement>.statsSection(title: String, vararg data: MiscStatItem) {
    div("section-view-box misc-stat-box") {
        id = "$title-stats"
        details {
            open = true
            summary { h3 { +title } }
            table("stat-table") {
                data.forEach { stat ->
                    with(stat) {
                        tr {
                            td { +name }
                            td { +value.toString() }
                        }
                        if (value is Int && achievementName != null && achievementTotal != null && value != 0) {
                            val progress = minOf(achievementTotal, value) / achievementTotal.toFloat() * 100
                            tr {
                                td("progress-bar") {
                                    this.title = "Achievement: $achievementName: $value/$achievementTotal"
                                    style =
                                        "background-image: linear-gradient(to right, var(--blue) $progress%, gray $progress%);"
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}


private fun TagConsumer<HTMLElement>.quests(quests: List<Quest>) {
    quests.forEach { quest ->
        val completed = quest.latestState == QuestStageState.COMPLETED
        div("section-view-box") {
            val completedClass = if (completed) "quest-completed" else "quest-incomplete"
            div("quest $completedClass") {
                details {
                    open = false
                    summary { h4 { +quest.name } }
                    ul {
                        quest.stages.sortedByDescending { it.id }.forEach { stage ->
                            val stageCompletedClass =
                                if (stage.state == QuestStageState.COMPLETED) "quest-stage-completed" else "quest-stage-incomplete"
                            li("quest-stage $stageCompletedClass") {
                                title = "${stage.id}"
                                +stage.name
                            }
                        }
                    }
                }
            }
        }
    }
}