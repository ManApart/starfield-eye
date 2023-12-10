package views.system

import Star
import StarSystem
import components.checkBox
import components.wikiLink
import inMemoryStorage
import kotlinx.html.*
import kotlinx.html.js.a
import kotlinx.html.js.onClickFunction
import org.w3c.dom.HTMLElement
import persistMemory
import replaceElement


fun TagConsumer<HTMLElement>.detailView(star: Star, system: StarSystem, linkToSystem: Boolean) {
    with(star) {
        h2 { +name }
        if (linkToSystem) {
            button {
                +"View System"
                onClickFunction = { systemView(system, 0) }
            }
        }
        wikiLink(name.replace(" ", "_"))

        checkBox(star.id, "Discovered", inMemoryStorage::discoveredStars) {
            replaceElement("${star.id}-details") {
                detailsTable(star, system)
            }
            persistMemory()
        }

        div {
            id = "${star.id}-details"
            detailsTable(star, system)
        }
    }
}

private fun TagConsumer<HTMLElement>.detailsTable(star: Star, system: StarSystem) {
    if (inMemoryStorage.showUndiscovered != false || inMemoryStorage.discoveredStars.contains(star.id)) {
        with(star) {
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
                    }
                )
                    .filter { (_, data) -> data.toString().isNotBlank() && data.toString() != "0" }
                    .forEach { (title, data) ->
                        tr {
                            td { +title }
                            td { +data.toString() }
                        }
                    }
                organicResourceRow(system.planets.flatMap { it.value.organicResources }.sorted().toSet())
                inorganicResourceRow(system.planets.values.flatMap { it.inorganicResources }.sortedBy { it.name }
                    .toSet())
            }
        }
    }
}