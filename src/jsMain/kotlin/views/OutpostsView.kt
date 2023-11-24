package views

import Outpost
import Planet
import PlanetInfo
import ResourceType
import components.*
import el
import galaxy
import inMemoryStorage
import kotlinx.browser.window
import kotlinx.dom.addClass
import kotlinx.dom.removeClass
import kotlinx.html.*
import kotlinx.html.button
import kotlinx.html.div
import kotlinx.html.dom.append
import kotlinx.html.h2
import kotlinx.html.h5
import kotlinx.html.hr
import kotlinx.html.js.*
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
            div("toggle-wrapper") {
                +"By Resource"
                toggle(inMemoryStorage::outpostResourceView) {
                    persistMemory()
                    outpostsPage()
                }
            }

            p { +"Add more outposts from the System View" }

            div("section-wrapper") {
                if (inMemoryStorage.outpostResourceView) {
                    viewOutpostsByResearch()
                } else viewAllOutposts()
            }
        }
    }
}

private fun TagConsumer<HTMLElement>.viewAllOutposts() {
    inMemoryStorage.planetUserInfo.values
        .filter { it.outPosts.isNotEmpty() }
        .forEach { planetInfo ->
            val planet = galaxy.planets[planetInfo.planetId]!!
            div("section-view-box") {
                id = "outpost-view-${planet.uniqueId}"
                outpostsView(planet, planetInfo, false, true)
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
    val genericRoot = el<HTMLElement?>("outpost-view")
    val root = genericRoot ?: el("outpost-view-${planet.uniqueId}")
    val showLink = genericRoot == null
    root.innerHTML = ""
    root.addClass("section-view-box")
    root.append {
        outpostsView(planet, info, true, showLink)
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
    button {
        +"Travel"
        title = "Set course to planet. In Future hopefully direct to outpost"
        onClickFunction = { attemptTravel(planet.name) }
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

    h5 { +"Organic Resources" }
    div {
        button(classes = "add-info-button") {
            +"Add"
            onClickFunction = {
                showStringPicker(planet.organicResources - outpost.organicResources) {
                    outpost.organicResources.add(it)
                    saveOutpostInfo(planet, info)
                }
            }
        }
        button(classes = "remove-info-button") {
            +"Del"
            onClickFunction = {
                showStringPicker(outpost.organicResources) {
                    outpost.organicResources.remove(it)
                    saveOutpostInfo(planet, info)
                }
            }
        }
    }
    if (outpost.organicResources.isNotEmpty()) {
        div("resource-wrapper") {
            +outpost.organicResources.joinToString()
        }
    }
    if (planet.organicResources.isNotEmpty()) {
        h5 { +"Inorganic Resources" }
        div {
            button(classes = "add-info-button") {
                +"Add"
                onClickFunction = {
                    showResourcePicker(planet.inorganicResources - outpost.inorganicResources) {
                        outpost.inorganicResources.add(it)
                        saveOutpostInfo(planet, info)
                    }
                }
            }
            button(classes = "remove-info-button") {
                +"Del"
                onClickFunction = {
                    showResourcePicker(outpost.inorganicResources) {
                        outpost.inorganicResources.remove(it)
                        saveOutpostInfo(planet, info)
                    }
                }
            }
        }
    }
    if (outpost.inorganicResources.isNotEmpty()) {
        div("resource-wrapper") {
            resourceSquares(outpost.inorganicResources)
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
                if (e.key == "Enter") {
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

private data class ResourceEntry(val planetId: String, val name: String, val resource: ResourceType)

private fun TagConsumer<HTMLElement>.viewOutpostsByResearch() {
    val resourceEntries = inMemoryStorage.planetUserInfo.values
        .filter { it.outPosts.isNotEmpty() }
        .flatMap { planet -> planet.outPosts.map { planet.planetId to it } }
        .flatMap { (id, outpost) -> outpost.inorganicResources.map { ResourceEntry(id, outpost.name, it) } }
        .groupBy { it.resource }
        .entries.sortedBy { it.key.name }

    div("section-view-box by-resource-view") {
        table {
            resourceEntries.forEach { (resource, outposts) ->
                tr("outpost-resource-row") {
                    td { resourceSquare(resource) }
                    td {
                        outposts.forEach { outpost ->
                            val planet = galaxy.planets[outpost.planetId]!!
                            span("outpost-resource-item") {
                                a(href = "#system/${outpost.planetId.replace("-", "/")}") {
                                    +"${outpost.name} (${planet.name})"
                                }
                                button {
                                    +"Travel"
                                    title = "Set course to planet. In Future hopefully direct to outpost"
                                    onClickFunction = { attemptTravel(planet.name) }
                                }
                            }
                        }
                    }
                }
            }
        }
    }


}