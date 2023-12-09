package views.system

import StarSystem
import el
import kotlinx.browser.window
import kotlinx.dom.addClass
import kotlinx.dom.removeClass
import kotlinx.html.*
import kotlinx.html.js.onClickFunction
import kotlinx.html.js.onMouseOutFunction
import kotlinx.html.js.onMouseOverFunction
import org.w3c.dom.HTMLElement
import org.w3c.dom.events.KeyboardEvent
import views.lifeSigns.clearFloraFaunaView
import views.clearOutpostsView
import views.lifeSigns.faunaView
import views.lifeSigns.floraView

private var currentPlanet = 0
private var currentPlanetType = "star"

fun TagConsumer<HTMLElement>.orrery(system: StarSystem) {
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
                    clearOutpostsView()
                    clearFloraFaunaView()
                }
                onMouseOverFunction = {
                    detailView(system, 0, false)
                    clearOutpostsView()
                    clearFloraFaunaView()
                }
                onMouseOutFunction = { detailView(system, currentPlanet, false) }
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
                            floraView(system.star.id, planetId)
                            faunaView(system.star.id, planetId)
                        }
                        onMouseOverFunction = {
                            detailView(system, planetId, false)
                            outpostsView(system, planetId)
                            floraView(system.star.id, planetId)
                            faunaView(system.star.id, planetId)
                        }
                        onMouseOutFunction = {
                            detailView(system, currentPlanet, false)
                            outpostsView(system, currentPlanet)
                            floraView(system.star.id, currentPlanet)
                            faunaView(system.star.id, currentPlanet)
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
                                    floraView(system.star.id, moonId)
                                    faunaView(system.star.id, moonId)
                                }
                                onMouseOverFunction = {
                                    detailView(system, moonId, false)
                                    outpostsView(system, moonId)
                                    floraView(system.star.id, moonId)
                                    faunaView(system.star.id, moonId)
                                }
                                onMouseOutFunction = {
                                    detailView(system, currentPlanet, false)
                                    outpostsView(system, currentPlanet)
                                    floraView(system.star.id, currentPlanet)
                                    faunaView(system.star.id, currentPlanet)
                                }
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

fun setSelected(prefix: String, planetId: Int) {
    el<HTMLElement?>("$currentPlanetType-$currentPlanet")?.removeClass("selected-circle")
    currentPlanetType = prefix
    currentPlanet = planetId
    el<HTMLElement?>("$prefix-$planetId")?.addClass("selected-circle")
}


fun navigateOrrery(key: KeyboardEvent) {
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