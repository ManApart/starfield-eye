package views

import MissionType
import Quest
import QuestStageState
import components.toggle
import el
import inMemoryStorage
import kotlinx.dom.addClass
import kotlinx.dom.removeClass
import kotlinx.html.*
import kotlinx.html.js.onClickFunction
import kotlinx.html.js.onKeyUpFunction
import missionSearchOptions
import org.w3c.dom.HTMLButtonElement
import org.w3c.dom.HTMLElement
import org.w3c.dom.HTMLInputElement
import pollHook
import replaceElement
import searchMissions
import updateUrl

private var oldQuests: List<Quest> = listOf()
var missionDivs: Map<String, HTMLElement> = mapOf()
private var navButtons: List<HTMLButtonElement> = listOf()

fun questView() {
    updateUrl("quests")
    replaceElement{
        div {
            id = "quest-view"
            navButtons()
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
    replaceElement("sections") {
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
    replaceElement("sections") {
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
    navButtons = listOf("show-all", "show-main", "show-faction", "show-city", "show-misc", "show-other").mapNotNull {
        el<HTMLButtonElement?>(it)
    }
}

private fun TagConsumer<HTMLElement>.filterControls() {
    div {
        id = "quest-search"
        missionButton("show-all", "All", ::showAll)
        missionButton("show-main", "Main Quests", ::showMain)
        missionButton("show-faction", "Faction", ::showFaction)
        missionButton("show-city", "City", ::showCity)
        missionButton("show-misc", "Misc", ::showMisc)
        missionButton("show-other", "Other", ::showOther)
        div("toggle-wrapper") {
            +"Completed"
            toggle(missionSearchOptions::showCompleted) {
                searchMissions()
            }
        }
        input(classes = "search") {
            id = "search"
            placeholder = "Filter by Name or stage"
            value = missionSearchOptions.searchText
            onKeyUpFunction = {
                missionSearchOptions.searchText = el<HTMLInputElement>("search").value
                searchMissions()
            }
        }
    }
}

private fun DIV.missionButton(id: String, title: String, onClick: () -> Unit, classes: String? = null) {
    button(classes = classes) {
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
                    summary {
                        img(src = "images/missions/${missionTypeClass}.svg", classes = "quest-icon")
                        h4 { +quest.name }
                        a(
                            href = "https://starfieldwiki.net/wiki/Starfield:${quest.name.replace(" ", "_")}",
                            "_blank",
                            "quest-wiki-link"
                        ) { +"Wiki" }
                    }
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

private fun pushButton(id: String) {
    navButtons.forEach {
        it.removeClass("button-pushed")
    }
    el(id).addClass("button-pushed")
}


private fun showAll() {
    missionSearchOptions.types = MissionType.entries
    pushButton("show-all")
    searchMissions()
}

private fun showMain() {
    missionSearchOptions.types = listOf(MissionType.MAIN)
    pushButton("show-main")
    searchMissions()
}

private fun showCity() {
    missionSearchOptions.types = MissionType.entries.filter { it.isCity() }
    pushButton("show-city")
    searchMissions()
}

private fun showFaction() {
    missionSearchOptions.types = MissionType.entries.filter { it.isFaction() }
    pushButton("show-faction")
    searchMissions()
}

private fun showMisc() {
    missionSearchOptions.types = MissionType.entries.filter { it.isMisc() }
    pushButton("show-misc")
    searchMissions()
}

private fun showOther() {
    missionSearchOptions.types = listOf(MissionType.OTHER)
    pushButton("show-other")
    searchMissions()
}
