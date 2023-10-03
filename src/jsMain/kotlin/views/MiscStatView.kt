package views

import MiscStats
import Quest
import QuestStageState
import el
import inMemoryStorage
import kotlinx.browser.window
import kotlinx.html.*
import kotlinx.html.dom.append
import kotlinx.html.js.onClickFunction
import org.w3c.dom.HTMLElement
import pollHook

fun miscStatView() {
    window.history.pushState(null, "null", "#misc-stats")
    val root = el("root")
    root.innerHTML = ""
    root.append {
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
    val root = el<HTMLElement?>("sections")
    root?.innerHTML = ""
    root?.append {
        div("section-view-box") {
            id = "misc-stat-explanation"
            h2 { +"Misc Stats" }
            div("accent-line") { +"" }

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

private fun displayStats(stats: MiscStats) {
    val root = el<HTMLElement?>("sections")
    root?.innerHTML = ""
    root?.append {
        div("section-view-box") {
            id = "misc-stats"
            h2 { +"Miscellaneous Stats" }
            div("accent-line") { +"" }
        }
        with(stats.general) {
            statsSection(
                "General",
                "Locations Discovered" to locationsDiscovered,
                "Locations Explored" to locationsExplored,
                "Days Passed" to daysPassed,
                "Hours Slept" to hoursSlept,
                "Hours Waited" to hoursWaited,
                "Credits Found" to creditsFound,
                "Most Credits Carried" to mostCreditsCarried,
                "Containers Looted" to containersLooted,
                "Magazines Found" to magazinesFound,
                "Persuasion Successes" to persuasionSuccesses,
                "Afflictions Suffered" to afflictionsSuffered,
                "Food Eaten" to foodEaten,
                "Data Slates Read" to dataSlatesRead,
                "Skill Challenges Completed" to skillChallengesCompleted,
                "Snow Globes Collected" to snowGlobesCollected,
            )
        }
        with(stats.exploration) {
            statsSection(
                "Exploration",
                "Systems Visited" to locationsDiscovered,
                "Planets Scanned" to systemsVisited,
                "Planets Fully Surveyed" to planetsScanned,
                "Planets Landed On" to planetsFullySurveyed,
                "Flora Fully Scanned" to planetsLandedOn,
                "Fauna Fully Scanned" to floraFullyScanned,
                "Stations Docked With" to faunaFullyScanned,
                "Ships Docked With" to stationsDockedWith,
                "Unique Creatures Scanned" to shipsDockedWith,
                "Temples Found" to uniqueCreaturesScanned,
            )
        }
        with(stats.ship) {
            statsSection(
                "Ship",
                "Grav Jumps" to gravJumps,
                "Farthest Grav Jump" to farthestGravJump,
                "Ships Registered" to shipsRegistered,
                "Max Ships Owned" to maxShipsOwned,
                "Most Expensive Ship Owned" to mostExpensiveShipOwned,
                "Largest Ship Owned" to largestShipOwned,
                "Largest Crew Size" to largestCrewSize,
                "Ships Customized" to shipsCustomized,
                "Ship Builder Credits Spent" to shipBuilderCreditsSpent,
                "Ships Sold" to shipsSold,
                "Ships Painted" to shipsPainted,
                "Ship Modules Built" to shipModulesBuilt,
            )
        }
        with(stats.mission) {
            statsSection(
                "Mission",
                "Quests Completed" to questsCompleted,
                "Activities Completed" to activitiesCompleted,
                "Main Quests Completed" to mainQuestsCompleted,
                "Crimson Fleet Collective Quests Completed" to crimsonFleetCollectiveQuestsCompleted,
                "Freestar Collective Quests Completed" to freestarCollectiveQuestsCompleted,
                "Ryujin Industries Quests Completed" to ryujinIndustriesQuestsCompleted,
                "United Colonies Quests Completed" to unitedColoniesQuestsCompleted,
                "Side Quests Completed" to sideQuestsCompleted,
            )
        }
        with(stats.combat) {
            statsSection(
                "Combat",
                "People Killed" to peopleKilled,
                "Creatures Killed" to creaturesKilled,
                "Robots Killed" to robotsKilled,
                "Turrets Killed" to turretsKilled,
                "Elite Enemies Killed" to eliteEnemiesKilled,
                "Critical Strikes" to criticalStrikes,
                "Sneak Attacks" to sneakAttacks,
                "Boost Packs Exploded" to boostPacksExploded,
                "Zero G Kills" to zeroGKills,
                "Powers Used" to powersUsed,
                "Scoped Kills" to scopedKills,
                "Max Damage Shot" to maxDamageShot,
                "Melee Kills" to meleeKills,
                "Laser Kills" to laserKills,
                "Ballistic Kills" to ballisticKills,
                "Head Shots" to headShots,
                "Explosive Kills" to explosiveKills,
                "Starborn Killed" to starbornKilled,
                "Ships Destroyed" to shipsDestroyed,
                "Favorite Weapon" to favoriteWeapon,
                "Favorite Power" to favoritePower,
            )
        }
        with(stats.crafting) {
            statsSection(
                "Crafting",
                "Weapon Mods Crafted" to weaponModsCrafted,
                "Armor Mods Crafted" to armorModsCrafted,
                "Organic Resources Gathered" to organicResourcesGathered,
                "Inorganic Resources Gathered" to inorganicResourcesGathered,
                "Chems Crafted" to chemsCrafted,
                "Food Cooked" to foodCooked,
                "Outposts Built" to outpostsBuilt,
                "Objects Built" to objectsBuilt,
                "Cargo Links Established" to cargoLinksEstablished,
            )
        }
        with(stats.crime) {
            statsSection(
                "Crime",
                "Locks Picked" to locksPicked,
                "Pockets Picked" to pocketsPicked,
                "Items Stolen" to itemsStolen,
                "Assaults" to assaults,
                "Murders" to murders,
                "Trespasses" to trespasses,
                "Times Arrested" to timesArrested,
                "Most Contraband Carried" to mostContrabandCarried,
                "Times Caught With Contraband" to timesCaughtWithContraband,
                "Acts Of Piracy" to actsOfPiracy,
                "Total Lifetime Bounty" to totalLifetimeBounty,
                "Largest Bounty" to largestBounty,
            )
        }
    }
}

private fun TagConsumer<HTMLElement>.statsSection(title: String, vararg data: Pair<String, Any>) {
    div("section-view-box misc-stat-box") {
        id = "$title-stats"
        details {
            open = true
            summary { h3 { +title } }
            table("stat-table") {
                data.forEach { (name, value) ->
                    tr {
                        td { +name }
                        td { +value.toString() }
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