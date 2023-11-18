package views

import Outpost
import Planet
import PlanetInfo
import el
import inMemoryStorage
import kotlinx.browser.document
import kotlinx.html.*
import kotlinx.html.dom.append
import kotlinx.html.js.h4
import kotlinx.html.js.onChangeFunction
import kotlinx.html.js.onClickFunction
import org.w3c.dom.HTMLElement
import org.w3c.dom.HTMLInputElement
import org.w3c.dom.HTMLTextAreaElement
import persistMemory

fun clearOutpostsView() {
    val root = el("outpost-view")
    root.innerHTML = ""
}

fun outpostsView(planet: Planet, info: PlanetInfo) {
    val root = el("outpost-view")
    root.innerHTML = ""
    root.append {
        h2 { +"${planet.name} Outposts" }
        div {
            id = "existing-outposts"
            info.outPosts.forEach { outpost ->
                outpost(outpost, info, planet)
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
            id = "add-resource-button"
            +"Add"
            onClickFunction = {
                val pos = el("add-resource-button").getBoundingClientRect()
                showResourcePicker(planet.resources - outpost.resources, pos) {
                    outpost.resources.add(it)
                    saveOutpostInfo(planet, info)
                }
            }
        }
        button(classes = "remove-info-button") {
            id = "remove-resource-button"
            +"Del"
            onClickFunction = {
                val pos = el("remove-resource-button").getBoundingClientRect()
                showResourcePicker(outpost.resources, pos) {
                    outpost.resources.remove(it)
                    saveOutpostInfo(planet, info)
                }
            }
        }
    }
    div("resource-wrapper") {
        resourceSquares(outpost.resources)
    }
    h5 { +"Notes" }
    div {
        id = "outpost-info-notes"
        textArea {
            id = "outpost-player-info-notes"
            +info.notes
            onChangeFunction = {
                info.notes = el<HTMLTextAreaElement>("outpost-player-info-notes").value
                saveOutpostInfo(planet, info)
            }
        }
    }

}


private fun TagConsumer<HTMLElement>.addOutpost(info: PlanetInfo, planet: Planet) {
    div {
        id = "add-outpost"
        textInput {
            id = "new-outpost-name"
            placeholder = "Outpost Name"
        }
        button(classes = "add-info-button") {
            +"Add"
            onClickFunction = {
                val name = el<HTMLInputElement>("new-outpost-name").value
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
