package views

import Planet
import PlanetInfo
import inMemoryStorage
import kotlinx.html.*
import kotlinx.html.dom.append
import kotlinx.html.js.onClickFunction
import org.w3c.dom.HTMLElement
import org.w3c.dom.HTMLInputElement
import org.w3c.dom.HTMLSelectElement
import persistMemory


fun userInfo(planet: Planet) {
    val root = el("user-info")
    root.innerHTML = ""
    root.append {
        val info = inMemoryStorage.planetNotes[planet.uniqueId] ?: PlanetInfo()
        table {
            tr {
                td { +"Labels" }
                td { labelInfo(info, planet) }
            }
            tr {
                td { +"Outposts" }
                td { outpostInfo(info, planet) }
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
                        +"-"
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
                    +"+"
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


private fun TagConsumer<HTMLElement>.outpostInfo(info: PlanetInfo, planet: Planet) {
    div {
        id = "info-outposts"
        div {
            id = "existing-outposts"
            info.outPosts.forEach { outpost ->
                span("planet-outpost") {
                    +outpost
                    button(classes = "remove-info-button") {
                        +"-"
                        onClickFunction = {
                            info.outPosts.remove(outpost)
                            savePlanetInfo(planet, info)
                        }
                    }
                }
            }
        }
        div {
            id = "add-outpost"
            input {
                id = "add-outpost-input"
            }
            button(classes = "add-info-button") {
                +"+"
                onClickFunction = {
                    val outpost = el<HTMLInputElement>("add-outpost-input").value
                    info.outPosts.add(outpost)
                    savePlanetInfo(planet, info)
                }
            }
        }
    }
}

private fun savePlanetInfo(planet: Planet, info: PlanetInfo) {
    inMemoryStorage.planetNotes[planet.uniqueId] = info
    userInfo(planet)
    persistMemory()
}