package views

import docking.Autosort
import docking.refreshKeywordChests
import docking.refreshKeywords
import inMemoryStorage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.html.*
import kotlinx.html.js.div
import kotlinx.html.js.onClickFunction
import replaceElement
import updateUrl

fun autoSortView(section: String? = null) {
    updateUrl("sort", section)
    val autosort = inMemoryStorage.autoSort
    replaceElement {
        div {
            id = "autosort-view"
            navButtons()
            div("auto-sort") {
                div("research-accent") {
                    id = "autosort-title"
                    +"Sorting"
                }
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
                div { id = "sections" }

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
    replaceElement("sections") {
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
    replaceElement("sections") {
        div("section-view-box") {
            id = "sort-explanation"
            h2 { +"Sorting" }
            div("accent-line") { +"Time dances its years forward" }
            p { +"Quest Id: ${autosort.sortQuestId}" }

            p { +"Refresh to get chests if they exist" }
        }
    }
}

private fun displaySorting(autosort: Autosort) {
    replaceElement("sections") {
        div("section-view-box") {
            id = "sort-explanation"
            h2 { +"Sorting" }
            div("accent-line") { +"Time dances its years forward" }

            p { +"Keywords: ${autosort.keywords.values.joinToString { it.name }}" }
            autosort.keywordChests.forEach { chest ->
                div {
                    h4 { +"${chest.name} - ${chest.location}" }
                    p { +chest.keywordIds.joinToString { autosort.keywords[it]?.name ?: it } }
                }
            }
        }
    }
}
