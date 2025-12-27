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
import kotlinx.dom.hasClass
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
import org.w3c.dom.HTMLImageElement
import org.w3c.dom.HTMLInputElement
import org.w3c.dom.HTMLSelectElement
import org.w3c.dom.HTMLTextAreaElement
import org.w3c.dom.events.KeyboardEvent
import persistMemory
import persistPictures
import pictureStorage
import replaceElement
import updateUrl
import views.system.attemptTravel
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
    var foundMissingPlanets = false
    inMemoryStorage.planetUserInfo.values
        .filter { it.outPosts.isNotEmpty() }
        .map {
            val planet = galaxy.planets[it.planetId]
            if (planet == null) {
                println("Unable to find ${it.planetId} in galaxy")
                foundMissingPlanets = true
            }
            it to planet
        }
        .sortedWith(compareBy({ (info, _) -> info.outPosts.none { it.favorite } }, { it.second?.name }))
        .forEach { (planetInfo, planet) ->
            div("section-view-box") {
                id = "outpost-view-${planetInfo.planetId}"
                outpostsView(planet, planetInfo, false, true)
            }
        }
    if (foundMissingPlanets) println(galaxy.planets.keys)
}

fun clearOutpostsView() {
    el<HTMLElement?>("outpost-view")?.let {
        it.removeClass("section-view-box")
        it.innerHTML = ""
    }
}

fun outpostsView(planet: Planet?, info: PlanetInfo) {
    val genericRoot = el<HTMLElement?>("outpost-view")
    val root = genericRoot ?: el<HTMLElement?>("outpost-view-${info.planetId}")
    if (root != null) {
        val showLink = genericRoot == null
        root.innerHTML = ""
        root.addClass("section-view-box")
        root.append {
            outpostsView(planet, info, true, showLink)
        }
    }
}

private fun TagConsumer<HTMLElement>.outpostsView(
    planet: Planet?,
    info: PlanetInfo,
    showAddButton: Boolean,
    linkToSystem: Boolean = false
) {
    h2 { +"${planet?.name ?: info.planetId} Outposts" }
    if (linkToSystem && planet != null) {
        button {
            +"View System"
            onClickFunction = {
                val system = galaxy.systems[planet.starId]!!
                systemView(system, planet.id)
            }
        }
    }
    if (info.outPosts.isNotEmpty() && planet != null) {
        button {
            +"Travel"
            title = "Set course to planet. In Future hopefully direct to outpost"
            onClickFunction = { attemptTravel(planet.name) }
        }
    }
    div {
        id = "existing-outposts-${info.planetId}"
        info.outPosts.dropLast(1).forEach { outpost ->
            outpost(outpost, info, planet)
            hr { }
        }
        if (info.outPosts.isNotEmpty()) {
            outpost(info.outPosts.last(), info, planet)
        }
    }
    if (showAddButton && planet != null) {
        hr { }
        addOutpost(info, planet)
    }
}

private fun TagConsumer<HTMLElement>.outpost(
    outpost: Outpost,
    info: PlanetInfo,
    planet: Planet?
) {
    outpostHeader(outpost, planet, info)

    screenshot("outposts/${info.planetId}/${outpost.id}")

    if (planet?.organicResources?.isNotEmpty() ?: false) {
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
    }
    if (outpost.organicResources.isNotEmpty()) {
        div("resource-wrapper") {
            +outpost.organicResources.joinToString()
        }
    }
    if (planet?.inorganicResources?.isNotEmpty() ?: false) {
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
            id = "outpost-player-info-notes-${info.planetId}-${outpost.name}"
            +info.notes
            onChangeFunction = {
                info.notes =
                    el<HTMLTextAreaElement>("outpost-player-info-notes-${info.planetId}-${outpost.name}").value
                saveOutpostInfo(planet, info)
            }
        }
    }
}

private fun TagConsumer<HTMLElement>.outpostHeader(
    outpost: Outpost,
    planet: Planet?,
    info: PlanetInfo,
) {
    var renameMode = false
    val oid = "${info.planetId}:${outpost.id}"
    h4 {
        img(
            "Favorite",
            src = "images/favorite-${if (outpost.favorite) "on" else "off"}.svg",
            classes = "favorite-image"
        ) {
            id = "outpost-${info.planetId}-${outpost.id}-favorite"
            onClickFunction = {
                outpost.favorite = !outpost.favorite
                el<HTMLImageElement>("outpost-${info.planetId}-${outpost.id}-favorite").src =
                    "images/favorite-${if (outpost.favorite) "on" else "off"}.svg"
                persistMemory()
            }
        }
        span {
            id = "outpost-$oid-header"
            +outpost.name
        }
        input(classes = "outpost-rename hidden") {
            id = "rename-outpost-$oid"
            onKeyPressFunction = {
                val e = it as KeyboardEvent
                if (e.key == "Enter") {
                    val input = el<HTMLInputElement>("rename-outpost-$oid")
                    val name = input.value
                    outpost.name = name
                    el("outpost-$oid-header").innerText = name
                    el("delete-outpost-$oid").removeClass("hidden")
                    input.addClass("hidden")
                    saveOutpostInfo(planet, info)
                }
            }
        }
        button(classes = "add-info-button") {
            +"Ren"
            title = "rename outpost"
            onClickFunction = {
                val input = el<HTMLInputElement>("rename-outpost-$oid")
                val header = el("outpost-$oid-header")
                if (renameMode) {
                    val name = input.value
                    outpost.name = name
                    el("outpost-$oid-header").innerText = name
                    el("delete-outpost-$oid").removeClass("hidden")
                    el("move-outpost-$oid").removeClass("hidden")
                    input.addClass("hidden")
                    saveOutpostInfo(planet, info)
                } else {
                    input.value = header.innerText
                    header.textContent = ""
                    el("delete-outpost-$oid").addClass("hidden")
                    el("move-outpost-$oid").addClass("hidden")
                    input.removeClass("hidden")
                }
                renameMode = !renameMode
            }
        }
        button(classes = "remove-info-button") {
            id = "delete-outpost-$oid"
            +"Del"
            onClickFunction = {
                if (window.confirm("Are you sure you want to delete ${outpost.name}?")) {
                    info.outPosts.remove(outpost)
                    saveOutpostInfo(planet, info)
                }
            }
        }
        button(classes = "add-info-button") {
            id = "move-outpost-$oid"
            +"Move"
            title = "move outpost"
            onClickFunction = {
                with(el("move-outpost-options-$oid")) {
                    if (hasClass("hidden")) removeClass("hidden") else addClass("hidden")
                }
            }
        }
        moveOptions(info, outpost, planet, oid)
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

private fun saveOutpostInfo(planet: Planet?, info: PlanetInfo) {
    inMemoryStorage.planetUserInfo[info.planetId] = info
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

    val noResources = outpostMap
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

private fun TagConsumer<HTMLElement>.moveOptions(info: PlanetInfo, outpost: Outpost, outpostPlanet: Planet?, oid: String) {
    div("hidden") {
        id = "move-outpost-options-$oid"
        val starSelectId = "outpost-system-$oid"
        val planetSelectId = "outpost-planet-$oid"
        val stars = galaxy.systems.values.toList()
        var currentStar = outpostPlanet?.starId?.let { galaxy.systems[it] } ?: stars.first()

        select {
            id = starSelectId
            stars.forEach { system ->
                option {
                    +system.star.name
                    value = system.star.id
                    selected = system == currentStar
                }
            }
            onChangeFunction = {
                currentStar = el<HTMLSelectElement>(starSelectId).selectedIndex.let { stars[it] }
                val planetSelect = el<HTMLSelectElement>(planetSelectId)
                planetSelect.innerText = ""
                planetSelect.append {
                    currentStar.planets.values.forEach { planet ->
                        option {
                            value = planet.id
                            +planet.name
                        }
                    }
                }
            }
        }
        select {
            id = planetSelectId
            currentStar.planets.values.forEach { planet ->
                option {
                    +planet.name
                    value = planet.id
                    selected = planet == outpostPlanet
                }
            }
        }

        button {
            +"Transfer"
            onClickFunction = {
                val selectedPlanet = el<HTMLSelectElement>(planetSelectId).selectedIndex.let { currentStar.planets.values.toList()[it] }
                val newInfo = inMemoryStorage.planetInfo(selectedPlanet.uniqueId)
                newInfo.outPosts.add(outpost)
                info.outPosts.remove(outpost)
                inMemoryStorage.planetUserInfo[info.planetId] = info
                inMemoryStorage.planetUserInfo[newInfo.planetId] = newInfo

                val oldPicKey = "outposts/${info.planetId}/${outpost.id}"
                if (pictureStorage.contains(oldPicKey)){
                    pictureStorage[oldPicKey]?.let { pictureStorage["outposts/${newInfo.planetId}/${outpost.id}"] = it}
                    pictureStorage.remove(oldPicKey)
                    persistPictures()
                }

                if (window.location.hash.startsWith("#outposts")){
                    outpostsPage()
                } else {
                    outpostsView(galaxy.planets[info.planetId], info)
                }
                persistMemory()
            }
        }
    }
    //TODO -CSS formatting dropdowns
}
