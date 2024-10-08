package views.system

import Outpost
import ResourceType
import StarSystem
import components.*
import docking.setCourse
import el
import inMemoryStorage
import keyPressedHook
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.html.*
import kotlinx.html.div
import kotlinx.html.js.onClickFunction
import kotlinx.html.tr
import org.w3c.dom.HTMLElement
import org.w3c.dom.HTMLInputElement
import persistMemory
import replaceElement
import updateUrl
import views.*
import views.lifeSigns.faunaView
import views.lifeSigns.floraView
import kotlin.reflect.KProperty0


var currentSystem: StarSystem? = null

fun systemView(system: StarSystem, planetId: Int = 0) {
    currentSystem = system
    updateUrl(system, planetId)
    replaceElement {
        div {
            id = "system-view"
            navButtons()

            div("section-wrapper") {
                orrery(system)
                div("section-view-box") { id = "detail-view" }
                div { id = "outpost-view" }
                div { id = "poi-view" }
                div { id = "flora-view" }
                div { id = "fauna-view" }
            }
        }
    }
    detailView(system, planetId)
    pointOfInterestView(system, planetId)
    outpostsView(system, planetId)
    floraView(system.star.id, planetId)
    faunaView(system.star.id, planetId)

    val planetType = when {
        planetId == 0 -> "star"
        system.planetChildren.keys.contains(planetId) -> "planet"
        else -> "moon"
    }
    setSelected(planetType, planetId)
    keyPressedHook = ::navigateOrrery
}

private fun updateUrl(system: StarSystem, planetId: Int) {
    updateUrl("system/${system.star.id}/$planetId")
}

fun detailView(system: StarSystem, planetId: Int, updateUrl: Boolean = true, linkToSystem: Boolean = false) {
    if (updateUrl) updateUrl(system, planetId)
    replaceElement("detail-view", "section-view-box") {
        if (planetId == 0) detailView(system.star, system, linkToSystem) else detailView(
            system,
            system.planets[planetId]!!,
            linkToSystem
        )
    }
    if (planetId != 0) {
        system.planets[planetId]?.let { userInfo(it) }
    }
}

fun outpostsView(system: StarSystem, planetId: Int) {
    if (planetId != 0) {
        val planet = system.planets[planetId]!!
        val info = inMemoryStorage.planetInfo(planet.uniqueId)
        outpostsView(planet, info)
    }
}

fun TagConsumer<HTMLElement>.organicResourceRow(resources: Set<String>) {
    tr {
        td("resource-td") { +"Organic Resources" }
        td("resource-value-td") {
            if (resources.isEmpty()) {
                +"None"
            } else {
                +resources.joinToString()
            }
        }
    }
}

fun TagConsumer<HTMLElement>.inorganicResourceRow(
    resources: Set<ResourceType>,
    checkBoxes: KProperty0<MutableSet<Int>>? = null
) {
    tr {
        td("resource-td") {
            +"Inorganic Resources"
            if (checkBoxes != null) {
                button(classes = "table-button") {
                    +"Done"
                    title = "Mark all resources scanned"
                    onClickFunction = {
                        val props = checkBoxes.get()
                        resources.forEachIndexed { i, resource ->
                            props.add(i)
                            el<HTMLInputElement>("resources-$i-checkbox").checked = true
                            el("resource-${resource.name}").style.backgroundColor = "#" + resource.color
                        }
                        persistMemory()
                    }
                }
            }
        }
        td("resource-value-td") {
            if (resources.isEmpty()) {
                +"None"
            } else {
                resourceSquares(resources, checkBoxes)
            }
        }
    }
}

fun TagConsumer<HTMLElement>.outPostsRow(outposts: List<Outpost>) {
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

fun attemptTravel(destination: String) {
    CoroutineScope(Dispatchers.Default).launch {
        setCourse(destination)
    }
}
