package views

import Label
import Planet
import PlanetInfo
import el
import inMemoryStorage
import kotlinx.html.*
import kotlinx.html.dom.append
import kotlinx.html.js.onChangeFunction
import kotlinx.html.js.onClickFunction
import org.w3c.dom.HTMLElement
import org.w3c.dom.HTMLSelectElement
import org.w3c.dom.HTMLTextAreaElement
import persistMemory


fun userInfo(planet: Planet) {
    val root = el("user-info")
    root.innerHTML = ""
    root.append {
        val info = inMemoryStorage.planetUserInfo[planet.uniqueId] ?: PlanetInfo()
        table("detail-view-table") {
            tr {
                td { +"Labels" }
                td { labelInfo(info, planet) }
            }
            tr {
                td { +"Notes" }
                td { playerNotes(info, planet) }
            }
        }
    }
}

private fun TagConsumer<HTMLElement>.labelInfo(info: PlanetInfo, planet: Planet) {
    div {
        id = "info-labels"
        div {
            id = "existing-labels"
            info.labels.forEach { label ->
                span("planet-label") {
                    +label.name
                    button(classes = "remove-info-button") {
                        +"Del"
                        onClickFunction = {
                            info.labels.remove(label)
                            savePlanetInfo(planet, info)
                        }
                    }
                }
            }
        }
        Label.entries.filter { !info.labels.contains(it) }.takeIf { it.isNotEmpty() }?.let { newLabels ->
            div {
                id = "add-labels"
                select {
                    id = "label-select"
                    newLabels.forEach { newLabel ->
                        option {
                            value = newLabel.name
                            +newLabel.name
                        }
                    }
                }
                button(classes = "add-info-button") {
                    +"Add"
                    onClickFunction = {
                        val select = el<HTMLSelectElement>("label-select")
                        val selected = newLabels[select.selectedIndex]
                        info.labels.add(selected)
                        savePlanetInfo(planet, info)
                    }
                }
            }
        }
    }
}

private fun TagConsumer<HTMLElement>.playerNotes(info: PlanetInfo, planet: Planet) {
    div {
        id = "info-notes"
        textArea {
            id = "player-info-notes"
            +info.notes
            onChangeFunction = {
                info.notes = el<HTMLTextAreaElement>("player-info-notes").value
                savePlanetInfo(planet, info)
            }
        }
    }
}

private fun savePlanetInfo(planet: Planet, info: PlanetInfo) {
    inMemoryStorage.planetUserInfo[planet.uniqueId] = info
    userInfo(planet)
    persistMemory()
}