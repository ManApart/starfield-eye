package views

import Quest
import el
import inMemoryStorage
import kotlinx.atomicfu.TraceBase.None.append
import kotlinx.browser.window
import kotlinx.html.*
import kotlinx.html.dom.append
import kotlinx.html.js.onClickFunction
import org.w3c.dom.HTMLDivElement
import org.w3c.dom.HTMLElement
import pollHook

private var oldQuests: List<Quest> = listOf()

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

fun needsDocking() {
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

fun displayQuests(quests: List<Quest>) {
    val root = el<HTMLElement?>("sections")
    root?.innerHTML = ""
    root?.append {
        div("section-view-box") {
            id = "quests"
            h2 { +"Quests" }
            div("accent-line") { +"Time dances its years forward" }
        }
        val groupedQuests = quests.groupBy { it.latestState == QuestStageState.COMPLETED }
        quests(groupedQuests[false] ?: listOf())
        quests(groupedQuests[true] ?: listOf())
    }
}

private fun TagConsumer<HTMLElement>.quests(quests: List<Quest>) {
    quests.forEach { quest ->
        val completed = quest.latestState == QuestStageState.COMPLETED
        div("section-view-box") {
            val completedClass = if (completed) "quest-completed" else "quest-incomplete"
            div("quest $completedClass") {
                details {
                    open = !completed
                    summary { h3 { +quest.name } }
                    ul {
                        quest.stages.sortedByDescending { it.id }.forEach { stage ->
                            val stageCompletedClass = if (stage.state == QuestStageState.COMPLETED) "quest-stage-completed" else "quest-stage-incomplete"
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