package views.system

import Planet
import PlanetInfo
import PlanetScan
import StarSystem
import components.checkBox
import components.screenshot
import components.wikiLink
import doRouting
import faunaReference
import floraReference
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
        wikiLink(name.replace(" ", "_"))

        checkBox("Initial Scan", info.scan::initialScan) {
            replaceElement("${planet.uniqueId}-details") {
                detailsTable(system, planet, info)
            }
            discoverParents()
            persistMemory()
        }
        checkBox("Landed On", info.scan::landed) {
            discoverParents()
            persistMemory()
        }

        button {
            +"Surveyed"
            title = "Mark this planet and its life signs 100% scanned"
            onClickFunction = {
                completeSurvey(info)
                doRouting()
                persistMemory()
            }
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
                traitsRow(planet, info.scan, traits)
                organicResourceRow(organicResources)
                inorganicResourceRow(inorganicResources, info.scan::resources)
                outPostsRow(info.outPosts)
            }
        }
    }
}

private fun TagConsumer<HTMLElement>.traitsRow(planet: Planet, scan: PlanetScan, traits: List<String>) {
    if (traits.isNotEmpty()) {
        tr {
            td { +"Traits" }
            td {
                traits.forEachIndexed { i, trait ->
                    checkBox(i, trait, scan::traits) {
                        planet.discoverParents()
                        persistMemory()
                    }
                }
            }
        }
    }
}

fun Planet.discoverParents() {
    inMemoryStorage.discoveredStars.add(starId)
}

fun Planet.landAndDiscover(info: PlanetInfo) {
    info.scan.landed = true
    discoverParents()
}

fun Planet.completeSurvey(info: PlanetInfo) {
    val scan = info.scan
    scan.initialScan = true
    landAndDiscover(info)
    inorganicResources.forEachIndexed { i, _ -> scan.resources.add(i) }
    traits.forEachIndexed { i, _ -> scan.traits.add(i) }
    faunaReference[uniqueId]?.forEach { scan.lifeScans[it.name] = 100 }
    floraReference[uniqueId]?.forEach { scan.lifeScans[it.name] = 100 }
}