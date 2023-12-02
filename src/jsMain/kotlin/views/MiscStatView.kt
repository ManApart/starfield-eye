package views

import MiscStatItem
import MiscStats
import Quest
import QuestStageState
import inMemoryStorage
import kotlinx.html.*
import kotlinx.html.js.onClickFunction
import org.w3c.dom.HTMLElement
import pollHook
import replaceElement
import updateUrl

fun miscStatView(addHistory: Boolean = true) {
    updateUrl("misc-stats", addHistory)
    replaceElement {
        div {
            id = "misc-stat-view"
            navButtons()
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

private fun displayStats(stats: MiscStats) {
    replaceElement("sections") {
        div("section-view-box") {
            id = "misc-stats"
            h2 { +"Miscellaneous Stats" }
            div("accent-line") { +"See if he'll play the good man's game" }
        }
        statsSection("General", stats.general)
        statsSection("Exploration", stats.exploration)
        statsSection("Ship", stats.ship)
        statsSection("Mission", stats.mission)
        statsSection("Combat", stats.combat)
        statsSection("Crafting", stats.crafting)
        statsSection("Crime", stats.crime)
    }
}

private fun TagConsumer<HTMLElement>.statsSection(title: String, data: List<MiscStatItem>) {
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
                            td { +value }
                        }
                        val intVal = value.toIntOrNull()
                        if (intVal != null && achievementName != null && achievementTotal != null && intVal != 0) {
                            val progress = minOf(achievementTotal, intVal) / achievementTotal.toFloat() * 100
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