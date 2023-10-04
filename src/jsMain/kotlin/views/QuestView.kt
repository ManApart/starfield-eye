package views

import MissionType
import Quest
import QuestStageState
import el
import inMemoryStorage
import isCity
import isFaction
import isMisc
import kotlinx.browser.window
import kotlinx.dom.addClass
import kotlinx.dom.removeClass
import kotlinx.html.*
import kotlinx.html.dom.append
import kotlinx.html.js.onClickFunction
import kotlinx.html.js.onKeyUpFunction
import org.w3c.dom.HTMLElement
import org.w3c.dom.HTMLInputElement
import pollHook
import searchMissions

private var oldQuests: List<Quest> = listOf()
var missionDivs: Map<String, HTMLElement> = mapOf()

fun questView() {
    window.history.pushState(null, "null", "#quests")
    val root = el("root")
    root.innerHTML = ""
    root.append {
        div {
            id = "quest-view"
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
        displayQuests(inMemoryStorage.quests)
    }
    pollHook = ::receivePoll
    pollData()
}

private fun needsDocking() {
    val root = el<HTMLElement?>("sections")
    root?.innerHTML = ""
    root?.append {
        div("section-view-box") {
            id = "quest-explanation"
            h2 { +"Quests" }
            div("accent-line") { +"Time dances its years forward" }

            p { +"Use dock to connect to the game and see near realtime updates to your quests page, or track your quest progress even when not playing!" }
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
    if (oldQuests != inMemoryStorage.quests) {
        oldQuests = inMemoryStorage.quests
        displayQuests(inMemoryStorage.quests)
    }
}

private fun displayQuests(quests: List<Quest>) {
    val root = el<HTMLElement?>("sections")
    root?.innerHTML = ""
    root?.append {
        div("section-view-box") {
            id = "quests"
            h2 { +"Quests" }
            div("accent-line") { +"Time dances its years forward" }
        }
        filterControls()
        val groupedQuests = quests.groupBy { it.latestState == QuestStageState.COMPLETED }
        quests(groupedQuests[false] ?: listOf())
        quests(groupedQuests[true] ?: listOf())
    }
    saveHtmlRefs(quests)
    searchMissions()
}

private fun saveHtmlRefs(quests: List<Quest>) {
    missionDivs = quests.associate { it.uniqueId to el(it.uniqueId) }
}


private fun TagConsumer<HTMLElement>.filterControls() {
    div {
        id = "quest-search"
        missionButton("toggle-completed", "Toggle Completed", ::toggleCompleted)
        missionButton("show-all", "All", ::showAll)
        missionButton("show-main", "Main Quests", ::showMain)
        missionButton("show-faction", "Faction", ::showFaction)
        missionButton("show-city", "City", ::showCity)
        missionButton("show-misc", "Misc", ::showMisc)
        missionButton("show-other", "Other", ::showOther)
        input(classes = "search") {
            id = "search"
            placeholder = "Filter by Name or stage"
            value = inMemoryStorage.missionSearchOptions.searchText
            onKeyUpFunction = {
                inMemoryStorage.missionSearchOptions.searchText = el<HTMLInputElement>("search").value
                searchMissions()
            }
        }
    }
}

private fun DIV.missionButton(id: String, title: String, onClick: () -> Unit) {
    button {
        this.id = id
        +title
        onClickFunction = { onClick() }
    }
}

private fun TagConsumer<HTMLElement>.quests(quests: List<Quest>) {
    quests.forEach { quest ->
        val completed = quest.latestState == QuestStageState.COMPLETED
        div("section-view-box") {
            id = quest.uniqueId
            val completedClass = if (completed) "quest-completed" else "quest-incomplete"
            val missionTypeClass = quest.type.name.lowercase().replace("_", "-")
            val missionTypeCategory = when {
                quest.type == MissionType.MAIN -> "main"
                quest.type.isFaction() -> "faction"
                quest.type.isCity() -> "city"
                else -> "misc"
            }

            div("quest $completedClass $missionTypeCategory-category $missionTypeClass-quest") {
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

fun filterMissions(shown: List<Quest>) {
    val shownMap = shown.associateBy { it.uniqueId }
    val (shownHtml, hiddenHtml) = missionDivs.entries.partition { (id, _) -> shownMap.containsKey(id) }

    shownHtml.forEach { (_, html) ->
        html.addClass("visible-block")
        html.removeClass("hidden")
    }
    hiddenHtml.forEach { (_, html) ->
        html.addClass("hidden")
        html.removeClass("visible-block")
    }
}


private fun showAll() {
    inMemoryStorage.missionSearchOptions.types = MissionType.entries
    searchMissions()
}

private fun showMain() {
    inMemoryStorage.missionSearchOptions.types = listOf(MissionType.MAIN)
    searchMissions()
}

private fun toggleCompleted() {
    inMemoryStorage.missionSearchOptions.showCompleted = !inMemoryStorage.missionSearchOptions.showCompleted
    searchMissions()
}

private fun showCity() {
    inMemoryStorage.missionSearchOptions.types = MissionType.entries.filter { it.isCity() }
    searchMissions()
}

private fun showFaction() {
    inMemoryStorage.missionSearchOptions.types = MissionType.entries.filter { it.isFaction() }
    searchMissions()
}

private fun showMisc() {
    inMemoryStorage.missionSearchOptions.types = MissionType.entries.filter { it.isMisc() }
    searchMissions()
}

private fun showOther() {
    inMemoryStorage.missionSearchOptions.types = listOf(MissionType.OTHER)
    searchMissions()
}
