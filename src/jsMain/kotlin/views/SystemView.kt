package views

import Planet
import ResourceType
import Star
import StarSystem
import exportPlayerInfo
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

            button(classes = "nav-button") {
                id = "export-button"
                +"Export"
                title = "Download user entered data"
                onClickFunction = { exportPlayerInfo() }
            }

            div("section-wrapper") {
                orrery(system)
                div("section-view-box") { id = "detail-view" }
            }
        }
    }
    detailView(system, planetId)
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
            id = "current-hover"
            +system.star.name
        }

        div("system-star-circle") {
            onClickFunction = { detailView(system, 0) }
            onMouseOverFunction = { el("current-hover").innerText = system.star.name }
        }
        system.planetChildren.entries.forEach { (planetId, moons) ->
            div("planet-row") {
                id = "planet-$planetId"
                div("system-planet-circle") {
                    title = "Stuff"
                    onClickFunction = { detailView(system, planetId) }
                    onMouseOverFunction = { el("current-hover").innerText = system.planets[planetId]?.name ?: "" }
                }
                if (moons.isNotEmpty()) {
                    moons.forEach { moonId ->
                        div("system-moon-circle") {
                            id = "moon-$moonId"
                            onClickFunction = { detailView(system, moonId) }
                            onMouseOverFunction = { el("current-hover").innerText = system.planets[planetId]?.name ?: "" }
                        }
                    }
                }
            }
        }
    }
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

        if (planetId == 0) detailView(system.star, system) else detailView(system.planets[planetId]!!)
    }
    if (planetId != 0) {
        system.planets[planetId]?.let { userInfo(it) }
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
            ).forEach { (title, data) ->
                tr {
                    td { +title }
                    td { +data.toString() }
                }
            }
            resourceRow(system.planets.values.flatMap { it.resources }.toSet().toList())
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

                ).forEach { (title, data) ->
                tr {
                    td { +title }
                    td { +data.toString() }
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
