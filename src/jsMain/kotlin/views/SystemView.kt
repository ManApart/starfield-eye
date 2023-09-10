package views

import Planet
import Star
import StarSystem
import kotlinx.browser.window
import kotlinx.html.*
import kotlinx.html.dom.append
import kotlinx.html.js.onClickFunction
import kotlinx.html.js.onMouseOverFunction
import org.w3c.dom.HTMLElement

fun systemView(system: StarSystem, planetId: Int = 0) {
    updateUrl(system, planetId)
    val root = el("root")
    root.innerHTML = ""
    root.append {
        h1 {
            id = "system-title"
            +"Designation ${system.star.name}"
        }
        h2 {
            +"Back to Galaxy"
            onClickFunction = {
                renderGalaxy()
            }
        }
        orrery(system)
        div { id = "detail-view" }
    }
    detailView(system, planetId)
}

private fun updateUrl(system: StarSystem, planetId: Int) {
    window.history.pushState(null, "null", "#system/${system.star.id}/$planetId")
}

private fun TagConsumer<HTMLElement>.orrery(system: StarSystem) {
    div {
        id = "orrery"
        div("system-star-circle") {
            onClickFunction = { detailView(system, 0) }
        }
        system.planetChildren.entries.forEach { (planetId, moons) ->
            div("planet-row") {
                id = "planet-$planetId"
                div("system-planet-circle") {
                    onClickFunction = { detailView(system, planetId) }
                    onMouseOverFunction = {detailView(system, planetId) }
                }
                if (moons.isNotEmpty()) {
                    moons.forEach { moonId ->
                        div("system-moon-circle") {
                            id = "moon-$moonId"
                            onClickFunction = { detailView(system, moonId) }
                            onMouseOverFunction = {detailView(system, moonId) }
                        }
                    }
                }
            }
        }
    }
}

private fun detailView(system: StarSystem, planetId: Int) {
    updateUrl(system, planetId)
    val root = el("detail-view")
    root.innerHTML = ""
    root.append {
        if (planetId == 0) detailView(system.star) else detailView(system.planets[planetId]!!)
    }
}

private fun TagConsumer<HTMLElement>.detailView(star: Star) {
    with(star) {
        h2 { +name }
        table {
            tr {
                td { +"Spectral Class" }
                td { +spectral }
            }
        }
    }
}

private fun TagConsumer<HTMLElement>.detailView(planet: Planet) {
    with(planet) {
        h2 { +name }
        table {
            tr {
                td { +"Type" }
                td { +type }
            }
        }
    }
}