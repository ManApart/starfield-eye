package views

import Outpost
import Planet
import PlanetInfo
import ResourceType
import Star
import StarSystem
import docking.setCourse
import el
import inMemoryStorage
import keyPressedHook
import kotlinx.browser.window
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.dom.addClass
import kotlinx.dom.removeClass
import kotlinx.html.*
import kotlinx.html.dom.append
import kotlinx.html.js.a
import kotlinx.html.js.onClickFunction
import kotlinx.html.js.onMouseOutFunction
import kotlinx.html.js.onMouseOverFunction
import org.w3c.dom.HTMLElement
import org.w3c.dom.events.KeyboardEvent

private var currentPlanet = 0
private var currentPlanetType = "star"
private var currentSystem: StarSystem? = null

fun systemView(system: StarSystem, planetId: Int = 0) {
    currentSystem = system
    updateUrl(system, planetId)
    val root = el("root")
    root.innerHTML = ""
    root.append {
        div {
            id = "system-view"
            button {
                +"Back to Galaxy"
                onClickFunction = {
                    renderGalaxy()
                }
            }

            div("section-wrapper") {
                orrery(system)
                div("section-view-box") { id = "detail-view" }
                div("section-view-box") { id = "outpost-view" }
            }
        }
    }
    detailView(system, planetId)
    outpostsView(system, planetId)

    val planetType = when {
        planetId == 0 -> "star"
        system.planetChildren.keys.contains(planetId) -> "planet"
        else -> "moon"
    }
    setSelected(planetType, planetId)
    keyPressedHook = ::navigateOrrery
}

private fun updateUrl(system: StarSystem, planetId: Int) {
    window.history.pushState(null, "null", "#system/${system.star.id}/$planetId")
}

private fun TagConsumer<HTMLElement>.orrery(system: StarSystem) {
    div("section-view-box") {
        id = "orrery"

        h2 {
            id = "system-title"
            +"Designation ${system.star.name}"
        }
        div {
            id = "system-map"
            div("system-star-circle") {
                id = "star-0"
                onClickFunction = {
                    setSelected("star", 0)
                    detailView(system, 0)
                    system.planets[currentPlanet]?.let { clearOutpostsView() }
                }
                onMouseOverFunction = {
                    detailView(system, 0)
                    system.planets[currentPlanet]?.let { clearOutpostsView() }
                }
                onMouseOutFunction = { detailView(system, currentPlanet) }
            }
            system.planetChildren.entries.forEach { (planetId, moons) ->
                hr("planet-spacer")
                div("planet-column") {
                    id = "planet-$planetId-row"
                    div("system-planet-circle") {
                        id = "planet-$planetId"
                        title = system.planets[planetId]!!.name
                        onClickFunction = {
                            setSelected("planet", planetId)
                            detailView(system, planetId)
                            outpostsView(system, planetId)
                        }
                        onMouseOverFunction = {
                            detailView(system, planetId)
                            outpostsView(system, planetId)
                        }
                        onMouseOutFunction = {
                            detailView(system, currentPlanet)
                            outpostsView(system, currentPlanet)
                        }
                    }
                    if (moons.isNotEmpty()) {
                        moons.forEach { moonId ->
                            hr("orrery-line")
                            div("system-moon-circle") {
                                id = "moon-$moonId"
                                title = system.planets[moonId]!!.name
                                onClickFunction = {
                                    setSelected("moon", moonId)
                                    detailView(system, moonId)
                                }
                                onMouseOverFunction = { detailView(system, moonId) }
                                onMouseOutFunction = { detailView(system, currentPlanet) }
                            }
                        }
                    }
                }
            }
        }
    }
}

private fun setSelected(system: StarSystem, planetId: Int) {
    val prefix = when {
        planetId == 0 -> "star"
        system.planetChildren.keys.contains(planetId) -> "planet"
        else -> "moon"
    }
    setSelected(prefix, planetId)
}

private fun setSelected(prefix: String, planetId: Int) {
    el<HTMLElement?>("$currentPlanetType-$currentPlanet")?.removeClass("selected-circle")
    currentPlanetType = prefix
    currentPlanet = planetId
    el<HTMLElement?>("$prefix-$planetId")?.addClass("selected-circle")
}

fun detailView(system: StarSystem, planetId: Int, updateUrl: Boolean = true, linkToSystem: Boolean = false) {
    if (updateUrl) updateUrl(system, planetId)
    val root = el("detail-view")
    root.innerHTML = ""
    root.append {
        if (linkToSystem) {
            button {
                +"View System"
                onClickFunction = { systemView(system, planetId) }
            }
        }

        if (planetId == 0) detailView(system.star, system) else detailView(system, system.planets[planetId]!!)
    }
    if (planetId != 0) {
        system.planets[planetId]?.let { userInfo(it) }
    }
}

private fun TagConsumer<HTMLElement>.detailView(star: Star, system: StarSystem) {
    with(star) {
        h2 { +name }
        a("https://starfieldwiki.net/wiki/Starfield:${name.replace(" ", "_")}", target = "_blank") {
            id = "wiki-link"
            +"View on Wiki"
        }
        table("detail-view-table") {
            listOf(
                "Spectral Class" to spectral,
                "Catalogue Id" to catalogueId,
                "Mass" to mass,
                "Radius" to radius,
                "Magnitude" to magnitude,
                "Temperature" to temp,
                "Planets" to system.planetChildren.size,
                "Moons" to system.planetChildren.values.sumOf { it.size },
                "Outposts" to system.planets.values.sumOf {
                    (inMemoryStorage.planetInfo(it.uniqueId)).outPosts.size
                },
            )
                .filter { (_, data) -> data.toString().isNotBlank() && data.toString() != "0" }
                .forEach { (title, data) ->
                    tr {
                        td { +title }
                        td { +data.toString() }
                    }
                }
            resourceRow(system.planets.values.flatMap { it.resources }.toSet())
        }
    }
}

private fun TagConsumer<HTMLElement>.detailView(system: StarSystem, planet: Planet) {
    with(planet) {
        h2 { +name }
        button {
            id = "travel-button"
            +"Travel (Alpha)"
            title = "Set course (requires docking, must have game map open)"
            onClickFunction = { attemptTravel(planet.name) }
        }
        a("https://starfieldwiki.net/wiki/Starfield:${name.replace(" ", "_")}", target = "_blank") {
            id = "wiki-link"
            +"View on Wiki"
        }
        table("detail-view-table") {
            listOf(
                "Type" to "$bodyTypeDescription ($bodyType)",
                "Class" to planetClass,
                "Mass" to mass,
                "Radius" to radius,
                "Density" to density,
                "Gravity" to "$gravity G",
                "Temperature" to "$temperature ($heat C)",
                "Atmosphere" to atmosphere,
                "Magnetosphere" to magneticField,
                "Biomes" to biomes.joinToString(),
                "Life" to life,
                "Fauna" to fauna,
                "Flora" to flora,
                "Water" to water,
                "Year" to "$year days",
                "Day" to "$day hours",
                "Traits" to traits,
                "Asteroids" to asteroids,
                "Rings" to rings,
            )
                .filter { (_, data) -> data.toString().isNotBlank() && data.toString() != "0" }
                .forEach { (title, data) ->
                    tr {
                        td { +title }
                        td { +data.toString() }
                    }
                }
            system.planetChildren[planet.id]?.let { moons ->
                if (moons.isNotEmpty()) {
                    tr {
                        td { +"Moons" }
                        td("moons-detail") {
                            moons.forEach { moonId ->
                                id = "moons-detail"
                                val moon = system.planets[moonId]!!
                                a("#system/${system.star.id}/$moonId") { +moon.name }
                            }
                        }
                    }
                }
            }
            resourceRow(resources)
            val info = inMemoryStorage.planetInfo(planet.uniqueId)
            outPostsRow(info.outPosts)
        }
        div { id = "user-info" }
    }
}

fun outpostsView(system: StarSystem, planetId: Int) {
    if (planetId != 0) {
        val planet = system.planets[planetId]!!
        val info = inMemoryStorage.planetInfo(planet.uniqueId)
        outpostsView(planet, info)
    }
}

private fun TagConsumer<HTMLElement>.resourceRow(resources: Set<ResourceType>) {
    tr {
        td("resource-td") { +"Resources" }
        td("resource-value-td") {
            if (resources.isEmpty()) {
                +"None"
            } else {
                resourceSquares(resources)
            }
        }
    }
}
private fun TagConsumer<HTMLElement>.outPostsRow(outposts: List<Outpost>) {
    tr {
        td("outpost-td") { +"Outposts" }
        td("outpost-value-td") {
            if (outposts.isEmpty()) {
                +"None"
            } else {
                +outposts.joinToString(", ") { it.name }
            }
        }
    }
}

private fun navigateOrrery(key: KeyboardEvent) {
    if (window.location.hash.contains("system") && currentSystem != null) {
        when (key.key) {
            "ArrowRight" -> selectNextPlanet(currentSystem!!)
            "ArrowLeft" -> selectNextPlanet(currentSystem!!, -1)
            "ArrowUp" -> selectNextMoon(currentSystem!!, -1)
            "ArrowDown" -> selectNextMoon(currentSystem!!)
            else -> println("Key: ${key.key}")
        }
    }
}

private fun selectNextPlanet(system: StarSystem, shift: Int = 1) {
    val planetIds = listOf(0) + system.planetChildren.keys.toList()
    var i = planetIds.indexOf(currentPlanet)
    if (i == -1) {
        i = system.planetChildren.entries.first { (_, moons) -> moons.contains(currentPlanet) }.key
    }
    i += shift
    if (i >= planetIds.size) i = 0
    if (i < 0) i = planetIds.size - 1
    val planetId = planetIds[i]
    setSelected(system, planetId)
    detailView(system, planetId)
    outpostsView(system, planetId)
}

private fun selectNextMoon(system: StarSystem, shift: Int = 1) {
    val planetIds = listOf(0) + system.planetChildren.keys.toList()
    var parentId = planetIds.indexOf(currentPlanet)
    if (parentId == -1) parentId =
        system.planetChildren.entries.first { (_, moons) -> moons.contains(currentPlanet) }.key

    val moons = listOf(parentId) + (system.planetChildren[parentId] ?: listOf())

    var i = moons.indexOf(currentPlanet) + shift

    if (i >= moons.size) i = 0
    if (i < 0) i = moons.size - 1
    val planetId = moons[i]

    setSelected(system, planetId)
    detailView(system, planetId)
    outpostsView(system, planetId)
}

private fun attemptTravel(destination: String) {
    CoroutineScope(Dispatchers.Default).launch {
        setCourse(destination)
    }
}