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
            backgroundStars()
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
    readyStars()
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
            div("accent-line") { +"" }

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
    println("Received Poll")
    if (oldQuests != inMemoryStorage.quests) {
        oldQuests = inMemoryStorage.quests
        println("Displaying ${inMemoryStorage.quests.size} quests")
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
            div("accent-line") { +"" }
        }
        val groupedQuests = quests.groupBy { it.completed }
        quests(groupedQuests[false]!!)
        quests(groupedQuests[true]!!)
    }
}

private fun TagConsumer<HTMLElement>.quests(quests: List<Quest>){
    quests.forEach { quest ->
        div("section-view-box") {
            val completedClass = if (quest.completed) "quest-completed" else "quest-incomplete"
            div("quest $completedClass") {
                h3 { +quest.name }
                ul {
                    quest.stages.values.sortedByDescending { it.id }.forEach { stage ->
                        val stageCompletedClass = if (stage.completed) "quest-stage-completed" else "quest-stage-incomplete"
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