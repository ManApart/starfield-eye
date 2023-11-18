package views

import Outpost
import Planet
import PlanetInfo
import el
import galaxy
import inMemoryStorage
import kotlinx.browser.window
import kotlinx.dom.addClass
import kotlinx.dom.removeClass
import kotlinx.html.*
import kotlinx.html.dom.append
import kotlinx.html.js.h4
import kotlinx.html.js.onChangeFunction
import kotlinx.html.js.onClickFunction
import kotlinx.html.js.onKeyPressFunction
import org.w3c.dom.HTMLElement
import org.w3c.dom.HTMLInputElement
import org.w3c.dom.HTMLTextAreaElement
import org.w3c.dom.events.KeyboardEvent
import persistMemory


fun outpostsPage() {
    window.history.pushState(null, "null", "#outposts")
    val root = el("root")
    root.innerHTML = ""
    root.append {
        div {
            id = "outposts-view"
            button {
                +"Back to Galaxy"
                onClickFunction = {
                    renderGalaxy()
                }
            }

            p { +"Add more outposts from the System View" }

            div("section-wrapper") {
                inMemoryStorage.planetUserInfo.values
                    .filter { it.outPosts.isNotEmpty() }
                    .forEach { planetInfo ->
                        val planet = galaxy.planets[planetInfo.planetId]!!
                        div("section-view-box") {
                            outpostsView(planet, planetInfo, false, true)
                        }
                    }
            }
        }
    }
}

fun clearOutpostsView() {
    el<HTMLElement?>("outpost-view")?.let {
        it.removeClass("section-view-box")
        it.innerHTML = ""
    }
}

fun outpostsView(planet: Planet, info: PlanetInfo) {
    val root = el("outpost-view")
    root.innerHTML = ""
    root.addClass("section-view-box")
    root.append {
        outpostsView(planet, info, true)
    }
}

private fun TagConsumer<HTMLElement>.outpostsView(
    planet: Planet,
    info: PlanetInfo,
    showAddButton: Boolean,
    linkToSystem: Boolean = false
) {
    h2 { +"${planet.name} Outposts" }
    if (linkToSystem) {
        button {
            +"View System"
            onClickFunction = {
                val system = galaxy.systems[planet.starId]!!
                systemView(system, planet.id)
            }
        }
    }
    div {
        id = "existing-outposts-${planet.name}"
        info.outPosts.forEach { outpost ->
            outpost(outpost, info, planet)
        }
    }
    if (showAddButton) {
        div {
            button {
                +"View ALl Outposts"
                onClickFunction = { outpostsPage() }
            }
        }
        addOutpost(info, planet)
    }
}

private fun TagConsumer<HTMLElement>.outpost(
    outpost: Outpost,
    info: PlanetInfo,
    planet: Planet
) {
    h4 {
        +outpost.name
        button(classes = "remove-info-button") {
            +"Del"
            onClickFunction = {
                info.outPosts.remove(outpost)
                saveOutpostInfo(planet, info)
            }
        }
    }

    h5 { +"Resources" }
    div {
        button(classes = "add-info-button") {
            +"Add"
            onClickFunction = {
                showResourcePicker(planet.resources - outpost.resources) {
                    outpost.resources.add(it)
                    saveOutpostInfo(planet, info)
                }
            }
        }
        button(classes = "remove-info-button") {
            +"Del"
            onClickFunction = {
                showResourcePicker(outpost.resources) {
                    outpost.resources.remove(it)
                    saveOutpostInfo(planet, info)
                }
            }
        }
    }
    if (outpost.resources.isNotEmpty()) {
        div("resource-wrapper") {
            resourceSquares(outpost.resources)
        }
    }
    h5 { +"Notes" }
    div {
        textArea {
            id = "outpost-player-info-notes-${planet.uniqueId}-${outpost.name}"
            +info.notes
            onChangeFunction = {
                info.notes =
                    el<HTMLTextAreaElement>("outpost-player-info-notes-${planet.uniqueId}-${outpost.name}").value
                saveOutpostInfo(planet, info)
            }
        }
    }
    hr { }
}

private fun TagConsumer<HTMLElement>.addOutpost(info: PlanetInfo, planet: Planet) {
    div {
        id = "add-outpost"
        textInput {
            id = "add-outpost-input"
            placeholder = "Outpost Name"
            onKeyPressFunction = {
                val e = it as KeyboardEvent
                if (e.key == "Enter"){
                    val name = el<HTMLInputElement>("add-outpost-input").value
                    info.outPosts.add(Outpost(name))
                    saveOutpostInfo(planet, info)
                }
            }
        }
        button(classes = "add-info-button") {
            +"Add"
            onClickFunction = {
                val name = el<HTMLInputElement>("add-outpost-input").value
                info.outPosts.add(Outpost(name))
                saveOutpostInfo(planet, info)
            }
        }
    }
}

private fun saveOutpostInfo(planet: Planet, info: PlanetInfo) {
    inMemoryStorage.planetUserInfo[planet.uniqueId] = info
    outpostsView(planet, info)
    persistMemory()
}
