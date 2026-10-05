package views

import docking.Autosort
import docking.Keyword
import docking.KeywordChest
import docking.refreshKeywordChests
import docking.refreshKeywords
import docking.sort
import docking.updateChest
import el
import inMemoryStorage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.html.*
import kotlinx.html.button
import kotlinx.html.js.div
import kotlinx.html.js.onClickFunction
import org.w3c.dom.HTMLButtonElement
import org.w3c.dom.HTMLElement
import persistMemory
import replaceElement
import updateUrl

fun autoSortView(section: String? = null) {
    updateUrl("sort", section)
    val autosort = inMemoryStorage.autoSort
    replaceElement {
        div {
            id = "autosort-view"
            navButtons()
            div("research") {
                div("research-accent") {
                    id = "autosort-title"
                    +"Sorting"
                }
                div { id = "header" }
                div("research-wrapper") {
                    div { id = "chests" }
                }
            }
        }
    }
    when {
        autosort.sortQuestId == null -> needsDocking()
        autosort.keywordChests.isEmpty() && autosort.keywords.isEmpty() -> needsRefresh(autosort)
        else -> displaySorting(autosort)
    }
}

private fun needsDocking() {
    replaceElement("header") {
        div("research-section") {
            h2 { +"Instructions" }
            p { +"Use dock to connect to the game and see Auto Sorting - TODO link to mod and write more instructions" }
            button {
                id = "dock-button"
                +"Dock"
                title = "Change Settings"
                onClickFunction = { dockView() }
            }
        }
    }
}

private fun needsRefresh(autosort: Autosort) {
    replaceElement("header") {
        refreshButton(autosort)
        div("research-section") {
            h2 { +"Instructions" }
            p { +"Quest Id: ${autosort.sortQuestId}" }
            p { +"Refresh to get chests if they exist" }
        }
    }
}

private fun TagConsumer<HTMLElement>.refreshButton(autosort: Autosort) {
    button {
        +"Refresh"
        onClickFunction = {
            CoroutineScope(Dispatchers.Default).launch {
                autosort.refreshKeywords()
                autosort.refreshKeywordChests()
                displaySorting(autosort)
                persistMemory()
            }
        }
    }
}

private fun displaySorting(autosort: Autosort) {
    replaceElement("header") {
        div {
            refreshButton(autosort)
            button {
                +"Sort Now"
                onClickFunction = {
                    CoroutineScope(Dispatchers.Default).launch {
                        autosort.sort()
                    }
                }
            }
        }
        drawChests(autosort)
    }
}

private fun drawChests(autosort: Autosort) {
    replaceElement("chests") {
        autosort.keywordChests.forEachIndexed { i, chest ->
            div("research-section") {
                h2 { +chest.name }
                div {
                    button {
                        +"Edit"
                        onClickFunction = { editChest(autosort, i, chest) }
                    }
                }
                p { +"Location: ${chest.location}" }
                p { +"Type: Keyword" }
                ul { chest.keywordIds.forEach { li { +(autosort.keywords[it]?.name ?: it) } } }
            }
        }
    }
}

private fun editChest(autosort: Autosort, i: Int, chest: KeywordChest) {
    replaceElement("chests") {
        div("research-section") {
            h2 { +chest.name }
            div {
                button {
                    +"Back"
                    onClickFunction = {
                        replaceElement("keywords") {}
                        drawChests(autosort)
                    }
                }
                button {
                    +"Persist"
                    onClickFunction = {
                        CoroutineScope(Dispatchers.Default).launch {
                            replaceElement("keywords") {}
                            autosort.updateChest(i, chest)
                            autosort.refreshKeywordChests()
                            drawChests(autosort)
                            persistMemory()
                        }
                    }
                }
            }
            autosort.keywords.values.groupBy { it.group }.forEach { (group, words) ->
                div("research-section") {
                    h2 { +group }
                    words.forEach { word ->
                        button(classes = "keyword-button") {
                            id = "${word.id}-button"
                            attributes["aria-pressed"] = if (chest.keywordIds.contains(word.id)) "true" else "false"
                            +word.name
                            onClickFunction = {
                                val og = chest.keywordIds.contains(word.id)
                                if (og) chest.keywordIds.remove(word.id) else chest.keywordIds.add(word.id)
                                el<HTMLButtonElement>("${word.id}-button").setAttribute("aria-pressed", (!og).toString())
                            }
                        }
                    }
                }
            }
        }
    }
}
