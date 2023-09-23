package views

import Planet
import PlanetInfo
import ResourceType
import Star
import StarSystem
import inMemoryStorage
import kotlinx.browser.window
import kotlinx.dom.addClass
import kotlinx.dom.removeClass
import kotlinx.html.*
import kotlinx.html.button
import kotlinx.html.div
import kotlinx.html.dom.append
import kotlinx.html.h2
import kotlinx.html.js.*
import kotlinx.html.table
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
            backgroundStars()
            button {
                +"Back to Galaxy"
                onClickFunction = {
                    renderGalaxy()
                }
            }

            div("section-wrapper") {
                orrery(system)
                div("section-view-box") { id = "detail-view" }
            }
        }
    }
    detailView(system, planetId)
    val planetType = when {
        planetId == 0 -> "star"
        system.planetChildren.keys.contains(planetId) -> "planet"
        else -> "moon"
    }
    setSelected(planetType, planetId)
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
                }
                onMouseOverFunction = { detailView(system, 0) }
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
                        }
                        onMouseOverFunction = { detailView(system, planetId) }
                        onMouseOutFunction = { detailView(system, currentPlanet) }
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
                    (inMemoryStorage.planetUserInfo[it.uniqueId] ?: PlanetInfo()).outPosts.size
                },
            )
                .filter { (_, data) -> data.toString().isNotBlank() && data.toString() != "0" }
                .forEach { (title, data) ->
                    tr {
                        td { +title }
                        td { +data.toString() }
                    }
                }
            resourceRow(system.planets.values.flatMap { it.resources }.toSet().toList())
        }
    }
}

private fun TagConsumer<HTMLElement>.detailView(system: StarSystem, planet: Planet) {
    with(planet) {
        h2 { +name }
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
        }
        div { id = "user-info" }
    }
}

private fun TABLE.resourceRow(resources: List<ResourceType>) {
    tr {
        td("resource-td") { +"Resources" }
        td {
            if (resources.isEmpty()) {
                +"None"
            } else {
                resources.forEach { resource ->
                    div("resource") {
                        +"${resource.name} (${resource.readableName})"
                    }
                }
            }
        }
    }
}

fun navigateOrrery(key: KeyboardEvent) {
    if (window.location.hash.contains("system") && currentSystem != null) {
        when (key.key) {
            "ArrowRight" -> selectNextPlanet(currentSystem!!)
            "ArrowLeft" -> selectNextPlanet(currentSystem!!, -1)
            else -> println("Key: ${key.key}")
        }
    }
}

private fun selectNextPlanet(system: StarSystem, shift: Int = 1) {
    val planetIds = listOf(0) + system.planetChildren.keys.toList()
    var i = planetIds.indexOf(currentPlanet) + shift
    if (i >= planetIds.size) i = 0
    if (i < 0) i = planetIds.size - 1
    val planetId = planetIds[i]
    setSelected(system, planetId)
    detailView(system, planetId)
}