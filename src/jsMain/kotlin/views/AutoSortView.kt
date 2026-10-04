package views

import docking.Autosort
import docking.KeywordChest
import docking.refreshKeywordChests
import docking.refreshKeywords
import docking.sort
import docking.updateChest
import inMemoryStorage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.html.*
import kotlinx.html.button
import kotlinx.html.js.div
import kotlinx.html.js.onClickFunction
import org.w3c.dom.HTMLElement
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
                    div { id = "keywords" }
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
        div("section-view-box") {
            id = "sort-explanation"
            h2 { +"Sorting" }
            div("accent-line") { +"Time dances its years forward" }

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
        div("section-view-box") {
            id = "sort-explanation"
            h2 { +"Sorting" }
            div("accent-line") { +"Time dances its years forward" }
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
                        autosort.refreshKeywordChests()
                        drawChests(autosort)
                    }
                }
            }
        }
        drawChests(autosort)
    }
}

private fun drawChests(autosort: Autosort) {
    replaceElement("chests") {
        autosort.keywordChests.forEach { chest ->
            div("research-section") {
                h2 { +chest.name }
                div {
                    button {
                        +"Edit"
                        onClickFunction = { editChest(autosort, chest) }
                    }
                }
                p { +"Location: ${chest.location}" }
                p { +"Type: Keyword" }
                p { +chest.keywordIds.joinToString { autosort.keywords[it]?.name ?: it } }
            }
        }
    }
}

private fun editChest(autosort: Autosort, chest: KeywordChest) {
    replaceElement("chests") {
        div("research-section") {
            h2 { +"Editing ${chest.name}" }
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
                        autosort.updateChest(chest)
                        autosort.refreshKeywordChests()
                        drawChests(autosort)
                    }
                }
            }
            p { +"test" }
        }
        replaceElement("keywords") {
            div("research-section") {
                h2 { +"Keywords" }
                p { +"Keywords: ${autosort.keywords.values.joinToString { it.name }}" }
            }
        }
    }
}

private fun persist(chest: KeywordChest) {

}