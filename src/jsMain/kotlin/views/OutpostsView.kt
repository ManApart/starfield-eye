package views

import Outpost
import Planet
import PlanetInfo
import ResourceType
import components.*
import el
import galaxy
import inMemoryStorage
import kotlinx.dom.addClass
import kotlinx.dom.removeClass
import kotlinx.html.*
import kotlinx.html.button
import kotlinx.html.consumers.filter
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
import replaceElement
import updateUrl
import views.system.attemptTravel
import views.system.discoverParents
import views.system.landAndDiscover
import views.system.systemView


fun outpostsPage() {
    updateUrl("outposts")
    replaceElement {
        div {
            id = "outposts-view"
            navButtons()
            div("toggle-wrapper") {
                +"By Resource"
                toggle(inMemoryStorage::outpostResourceView) {
                    persistMemory()
                    outpostsPage()
                }
            }

            p { +"Add more outposts from the System View" }

            div("section-wrapper") {
                if (inMemoryStorage.outpostResourceView == true) {
                    viewOutpostsByResearch()
                } else viewAllOutposts()
            }
        }
    }
}

private fun TagConsumer<HTMLElement>.viewAllOutposts() {
    inMemoryStorage.planetUserInfo.values
        .filter { it.outPosts.isNotEmpty() }
        .map { it to galaxy.planets[it.planetId]!! }
        .sortedBy { it.second.name }
        .forEach { (planetInfo, planet) ->
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
    if (info.outPosts.isNotEmpty()) {
        button {
            +"Travel"
            title = "Set course to planet. In Future hopefully direct to outpost"
            onClickFunction = { attemptTravel(planet.name) }
        }
    }
    div {
        id = "existing-outposts-${planet.name}"
        info.outPosts.dropLast(1).forEach { outpost ->
            outpost(outpost, info, planet)
            hr { }
        }
        if (info.outPosts.isNotEmpty()) {
            outpost(info.outPosts.last(), info, planet)
        }
    }
    if (showAddButton) {
        hr { }
        addOutpost(info, planet)
    }
}

private fun TagConsumer<HTMLElement>.outpost(
    outpost: Outpost,
    info: PlanetInfo,
    planet: Planet
) {
    val i = info.outPosts.indexOf(outpost)
    outpostHeader(i, outpost, planet, info)

    screenshot("outposts/${planet.uniqueId}/${outpost.id}")

    if (outpost.organicResources.isNotEmpty()) {
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

        div("resource-wrapper") {
            +outpost.organicResources.joinToString()
        }
    }
    if (planet.inorganicResources.isNotEmpty()) {
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
}

private fun TagConsumer<HTMLElement>.outpostHeader(
    i: Int,
    outpost: Outpost,
    planet: Planet,
    info: PlanetInfo,
) {
    var renameMode = false
    h4 {
        span {
            id = "outpost-$i-header"
            +outpost.name
        }
        input(classes = "outpost-rename hidden") {
            id = "rename-outpost-$i"
            onKeyPressFunction = {
                val e = it as KeyboardEvent
                if (e.key == "Enter") {
                    val input = el<HTMLInputElement>("rename-outpost-$i")
                    val name = input.value
                    outpost.name = name
                    el("outpost-$i-header").innerText = name
                    el("delete-outpost-$i").removeClass("hidden")
                    input.addClass("hidden")
                    saveOutpostInfo(planet, info)
                }
            }
        }
        button(classes = "add-info-button") {
            +"Ren"
            title = "rename outpost"
            onClickFunction = {
                val input = el<HTMLInputElement>("rename-outpost-$i")
                val header = el("outpost-$i-header")
                if (renameMode) {
                    val name = input.value
                    outpost.name = name
                    el("outpost-$i-header").innerText = name
                    el("delete-outpost-$i").removeClass("hidden")
                    input.addClass("hidden")
                    saveOutpostInfo(planet, info)
                } else {
                    input.value = header.innerText
                    header.textContent = ""
                    el("delete-outpost-$i").addClass("hidden")
                    input.removeClass("hidden")
                }
                renameMode = !renameMode
            }
        }
        button(classes = "remove-info-button") {
            id = "delete-outpost-$i"
            +"Del"
            onClickFunction = {
                info.outPosts.remove(outpost)
                saveOutpostInfo(planet, info)
            }
        }
    }
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
                    info.addOutpost(name)
                    planet.landAndDiscover(info)
                    saveOutpostInfo(planet, info)
                }
            }
        }
        button(classes = "add-info-button") {
            +"Add"
            onClickFunction = {
                val name = el<HTMLInputElement>("add-outpost-input").value
                info.addOutpost(name)
                planet.landAndDiscover(info)
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
private data class OrganicResourceEntry(val planetId: String, val name: String, val resource: String)

private fun TagConsumer<HTMLElement>.viewOutpostsByResearch() {
    val outpostMap = inMemoryStorage.planetUserInfo.values
        .filter { it.outPosts.isNotEmpty() }
        .flatMap { planet -> planet.outPosts.map { planet.planetId to it } }

    val inorganicResources =
        outpostMap.flatMap { (id, outpost) -> outpost.inorganicResources.map { ResourceEntry(id, outpost.name, it) } }
            .groupBy { it.resource }
            .entries.sortedBy { it.key.name }

    val organicResources =
        outpostMap.flatMap { (id, outpost) ->
            outpost.organicResources.map {
                OrganicResourceEntry(
                    id,
                    outpost.name,
                    it
                )
            }
        }
            .groupBy { it.resource }
            .entries.sortedBy { it.key }

    val noResources =        outpostMap
        .filter { (_, outpost) -> outpost.organicResources.isEmpty() && outpost.inorganicResources.isEmpty() }
        .toSet()
        .sortedBy { it.second.name }

    div("section-view-box by-resource-view") {
        table {
            id = "inorganic-resources"
            inorganicResources.forEach { (resource, outposts) ->
                tr("outpost-resource-row") {
                    td { resourceSquare(resource) }
                    td {
                        outposts.forEach { outpost ->
                            outpostCell(outpost.planetId, outpost.name)
                        }
                    }
                }
            }
        }
        table {
            id = "organic-resources"
            organicResources.forEach { (resource, outposts) ->
                tr("outpost-resource-row") {
                    td { +resource }
                    td {
                        outposts.forEach { outpost ->
                            outpostCell(outpost.planetId, outpost.name)
                        }
                    }
                }
            }
        }
        table {
            id = "no-resources"
            tr("outpost-resource-row") {
                td { +"None" }
                td {
                    noResources.forEach { (planetId, outpost) ->
                        outpostCell(planetId, outpost.name)
                    }
                }
            }
        }
    }
}

private fun TD.outpostCell(planetId: String, name: String) {
    val planet = galaxy.planets[planetId]!!
    span("outpost-resource-item") {
        a(href = "#system/${planetId.replace("-", "/")}") {
            +"$name (${planet.name})"
        }
        button {
            +"Travel"
            title = "Set course to planet. In Future hopefully direct to outpost"
            onClickFunction = { attemptTravel(planet.name) }
        }
    }
}