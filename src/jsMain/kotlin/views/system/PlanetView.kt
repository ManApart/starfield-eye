package views.system

import Planet
import PlanetInfo
import PlanetScan
import StarSystem
import components.checkBox
import components.screenshot
import doRouting
import inMemoryStorage
import kotlinx.html.*
import kotlinx.html.js.a
import kotlinx.html.js.onClickFunction
import org.w3c.dom.HTMLElement
import persistMemory
import replaceElement

fun TagConsumer<HTMLElement>.detailView(system: StarSystem, planet: Planet, linkToSystem: Boolean) {
    with(planet) {
        val info = inMemoryStorage.planetInfo(uniqueId)
        h2 { +name }

        screenshot("planets/${uniqueId}", imageUrl)

        if (linkToSystem) {
            button {
                +"View System"
                onClickFunction = { systemView(system, planet.id) }
            }
        }
        button {
            id = "travel-button"
            +"Travel"
            title = "Set course to ${planet.name} (requires docking)"
            onClickFunction = { attemptTravel(planet.name) }
        }
        a("https://starfieldwiki.net/wiki/Starfield:${name.replace(" ", "_")}", target = "_blank") {
            id = "wiki-link"
            +"View on Wiki"
        }

        checkBox("Initial Scan", info.scan::initialScan) {
            replaceElement("${planet.uniqueId}-details") {
                detailsTable(system, planet, info)
            }
            persistMemory()
        }
        checkBox("Landed On", info.scan::landed) {
            persistMemory()
        }

        div {
            id = "${planet.uniqueId}-details"
            detailsTable(system, planet, info)
        }

        div { id = "user-info" }
    }
}

private fun TagConsumer<HTMLElement>.detailsTable(system: StarSystem, planet: Planet, info: PlanetInfo) {
    with(planet) {
        if (inMemoryStorage.showUndiscovered != false || info.scan.initialScan) {
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
                traitsRow(info.scan, traits)
                organicResourceRow(organicResources)
                inorganicResourceRow(inorganicResources, info.scan::resources)
                outPostsRow(info.outPosts)
            }
        }
    }
}

private fun TagConsumer<HTMLElement>.traitsRow(scan: PlanetScan, traits: List<String>) {
    if (traits.isNotEmpty()) {
        tr {
            td { +"Traits" }
            td {
                traits.forEachIndexed { i, trait ->
                    checkBox(i, trait, scan::traits) {
                        persistMemory()
                    }
                }
            }
        }
    }
}