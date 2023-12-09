package views.lifeSigns

import FloraWikiData
import components.counter
import components.screenshot
import el
import floraReference
import galaxy
import inMemoryStorage
import kotlinx.dom.removeClass
import kotlinx.html.*
import kotlinx.html.js.onClickFunction
import org.w3c.dom.HTMLElement
import persistMemory
import replaceElement
import views.system.systemView
import kotlin.math.max
import kotlin.math.min

fun floraView(system: Int, planet: Int) {
    val flora = floraReference["$system-$planet"]
    val classes = if (flora != null) "section-view-box" else ""
    replaceElement("flora-view", classes) {
        flora?.let { planetFlora ->
            h2 { +"Flora" }
            planetFlora.forEach { flora ->
                display(flora)

            }
        }
    }
}

fun floraView(flora: FloraWikiData) {
    replaceElement("flora-view", "section-view-box") {
        h2 { +"Flora" }
        display(flora, true)
    }
}

private fun TagConsumer<HTMLElement>.display(flora: FloraWikiData, linkToSystem: Boolean = false) {
    with(flora) {
        div("flora-entry") {
            p { +name }

            screenshot("flora/$name", flora.imageUrl)

            if (linkToSystem) {
                flora.planetId?.let { id -> id.split("-").map { it.toInt() } }?.let { id ->
                    button {
                        +"View System"
                        onClickFunction = {
                            val system = galaxy.systems[id.first()]!!
                            systemView(system, id.last())
                        }
                    }
                }
            }
            a("https://starfieldwiki.net/wiki/Starfield:${name.replace(" ", "_")}", target = "_blank") {
                id = "wiki-link"
                +"View on Wiki"
            }
            table("scan-progress-table") {
                if (flora.planetId != null) {
                    tr {
                        td { +"Scanned" }
                        td {
                            val info = inMemoryStorage.planetInfo(flora.planetId)
                            val scanPercent = info.scan.lifeScans[flora.name] ?: 0
                            counter("${flora.uniqueId}-scan", { scanPercent }) {
                                val newVal = min(100, max(0, it))
                                inMemoryStorage.assurePlanetInfo(flora.planetId, info)
                                info.scan.lifeScans[flora.name] = newVal
                                replaceElement("${flora.uniqueId}-details") {
                                    detailsTable(flora)
                                }
                                persistMemory()
                            }
                            +"%"
                        }
                    }
                }
            }
            div {
                id = "${flora.uniqueId}-details"
                detailsTable(flora)
            }
        }
    }
}

private fun TagConsumer<HTMLElement>.detailsTable(flora: FloraWikiData) {
    with(flora) {
        table("detail-view-table") {
            (listOf(
                "Biomes" to biomes.joinToString(),
                "Resource" to resource,
            ) + other.entries.map { it.key to it.value })
                .filter { (_, data) -> data.isNotBlank() && data != "0" }
                .let { data ->
                    if (inMemoryStorage.showUndiscovered != false) data else {
                        val percent =
                            (planetId?.let { inMemoryStorage.planetInfo(it).scan.lifeScans[flora.name] }
                                ?: 0) / 100.0
                        val totalRows = (data.size * percent).toInt()
                        data.take(totalRows)
                    }
                }
                .forEach { (title, data) ->
                    tr {
                        td { +title }
                        td { +data }
                    }
                }
        }
    }
}


fun clearFloraFaunaView() {
    clearFloraView()
    clearFaunaView()
}

fun clearFloraView() {
    el<HTMLElement?>("flora-view")?.let {
        it.removeClass("section-view-box")
        it.innerHTML = ""
    }
}

fun clearFaunaView() {
    el<HTMLElement?>("fauna-view")?.let {
        it.removeClass("section-view-box")
        it.innerHTML = ""
    }
}