package views.lifeSigns

import FaunaWikiData
import components.counter
import components.screenshot
import components.wikiLink
import faunaReference
import galaxy
import inMemoryStorage
import kotlinx.html.*
import kotlinx.html.js.onClickFunction
import org.w3c.dom.HTMLElement
import persistMemory
import replaceElement
import views.system.landAndDiscover
import views.system.systemView
import kotlin.math.max
import kotlin.math.min

fun faunaView(system: Int, planet: Int) {
    val fauna = faunaReference["$system-$planet"]
    val classes = if (fauna != null) "section-view-box" else ""
    replaceElement("fauna-view", classes) {
        fauna?.let { planetFauna ->
            h2 { +"Fauna" }

            planetFauna.forEach { fauna ->
                display(fauna)

            }
        }
    }
}

fun faunaView(fauna: FaunaWikiData) {
    replaceElement("fauna-view", "section-view-box") {
        h2 { +"Fauna" }
        display(fauna, true)
    }
}

private fun TagConsumer<HTMLElement>.display(fauna: FaunaWikiData, linkToSystem: Boolean = false) {
    with(fauna) {
        div("fauna-entry") {
            p { +name}

            screenshot("fauna/$name", fauna.imageUrl)

            if (linkToSystem) {
                fauna.planetId?.let { id -> id.split("-").map { it.toInt() } }?.let { id ->
                    button {
                        +"View System"
                        onClickFunction = {
                            val system = galaxy.systems[id.first()]!!
                            systemView(system, id.last())
                        }
                    }
                }
            }
            wikiLink(name.replace(" ", "_"))

            table("scan-progress-table") {
                if (fauna.planetId != null) {
                    tr {
                        td { +"Scanned %" }
                        td {
                            val scanPercent = inMemoryStorage.planetInfo(fauna.planetId).scan.lifeScans[fauna.name] ?: 0
                            counter("${fauna.uniqueId}-scan", { scanPercent }) {
                                val newVal = min(100, max(0, it))
                                val info = inMemoryStorage.planetInfoAndSave(fauna.planetId)
                                if (newVal != 0) galaxy.planets[fauna.planetId]?.landAndDiscover(info)
                                info.scan.lifeScans[fauna.name] = newVal
                                replaceElement("${fauna.uniqueId}-details") {
                                    detailsTable(fauna)
                                }
                                persistMemory()
                            }
                        }
                    }
                }
            }
            div {
                id = "${fauna.uniqueId}-details"
                detailsTable(fauna)
            }
        }
    }
}

private fun TagConsumer<HTMLElement>.detailsTable(fauna: FaunaWikiData) {
    with(fauna) {
        table("detail-view-table") {
            (listOf(
                "Biomes" to biomes.joinToString(),
                "Resource" to resource,
                "Temperament" to temperament.name.lowercase().capitalize(),
                "Abilities" to abilities.joinToString(),
            ) + other.entries.map { it.key to it.value }.sortedBy { it.first })
                .filter { (_, data) -> data.isNotBlank() && data != "0" }
                .let { data ->
                    if (inMemoryStorage.showUndiscovered != false) data else {
                        val percent =
                            (fauna.planetId?.let { inMemoryStorage.planetInfo(it).scan.lifeScans[fauna.name] }
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