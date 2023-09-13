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
        div {
            id = "system-view"
            backgroundStars()
            h3 {
                +"Back to Galaxy"
                onClickFunction = {
                    renderGalaxy()
                }
            }

            div {
                id = "system-view-parts"
                orrery(system)
                div("system-view-box") { id = "detail-view" }
            }
        }
    }
    detailView(system, planetId)
}

private fun updateUrl(system: StarSystem, planetId: Int) {
    window.history.pushState(null, "null", "#system/${system.star.id}/$planetId")
}

private fun TagConsumer<HTMLElement>.orrery(system: StarSystem) {
    div("system-view-box") {
        id = "orrery"

        h2 {
            id = "system-title"
            +"Designation ${system.star.name}"
        }

        div("system-star-circle") {
            onClickFunction = { detailView(system, 0) }
        }
        system.planetChildren.entries.forEach { (planetId, moons) ->
            div("planet-row") {
                id = "planet-$planetId"
                div("system-planet-circle") {
                    onClickFunction = { detailView(system, planetId) }
                    onMouseOverFunction = { detailView(system, planetId) }
                }
                if (moons.isNotEmpty()) {
                    moons.forEach { moonId ->
                        div("system-moon-circle") {
                            id = "moon-$moonId"
                            onClickFunction = { detailView(system, moonId) }
                            onMouseOverFunction = { detailView(system, moonId) }
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
        if (planetId == 0) detailView(system.star, system) else detailView(system.planets[planetId]!!)
    }
}

private fun TagConsumer<HTMLElement>.detailView(star: Star, system: StarSystem) {
    with(star) {
        h2 { +name }
        table {
            listOf(
                "Spectral Class" to spectral,
                "Catalogue Id" to catalogueId,
                "Mass" to mass,
                "Radius" to radius,
                "Magnitude" to magnitude,
                "Temperature" to temp,
                "Planets" to system.planetChildren.size,
                "Moons" to system.planetChildren.values.sumOf { it.size },
                "Outposts" to "",
                "Resources" to system.planets.values.flatMap { it.resources }.toSet()

                ).forEach { (title, data) ->
                tr {
                    td { +title }
                    td { +data.toString() }
                }
            }
        }
    }
}

private fun TagConsumer<HTMLElement>.detailView(planet: Planet) {
    with(planet) {
        h2 { +name }
        table {
            listOf(
                "Type" to type,
                "Body Type" to bodyType,
                "Class" to planetClass,
                "Mass" to mass,
                "Radius" to radius,
                "Density" to density,
                "Gravity" to gravity,
                "Temperature" to heat,
                "Atmosphere" to "",
                "Magnetosphere" to magneticField,
                "Biomes" to biomes.joinToString(),
                "Life" to life,
                "Fauna" to "",
                "Flora" to "",
                "Water" to "",
                "Year" to year,
                "Day" to day,
                "Asteroids" to asteroids,
                "Rings" to rings,
                "Resources" to if(resources.isEmpty()) "None" else resources.joinToString(),

                ).forEach { (title, data) ->
                tr {
                    td { +title }
                    td { +data.toString() }
                }
            }
        }
    }
}