package views.lifeSigns

import FaunaWikiData
import components.counter
import components.screenshot
import components.wikiLink
import el
import faunaReference
import galaxy
import inMemoryStorage
import kotlinx.html.*
import kotlinx.html.js.onClickFunction
import org.w3c.dom.HTMLElement
import org.w3c.dom.HTMLInputElement
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
            p { +name }

            screenshot("fauna/$name")

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

            if (fauna.planetId != null) {
                span { +"Scanned" }
                span("counter-span") {
                    val info = inMemoryStorage.planetInfo(fauna.planetId)
                    val scanPercent = info.scan.lifeScans[fauna.name] ?: 0
                    counter("${fauna.uniqueId}-scan", { scanPercent }) {
                        val newVal = min(100, max(0, it))
                        if (newVal != 0) galaxy.planets[fauna.planetId]?.landAndDiscover(info)
                        info.scan.lifeScans[fauna.name] = newVal
                        replaceElement("${fauna.uniqueId}-details") {
                            detailsTable(fauna)
                        }
                        persistMemory()
                    }
                    +"%"
                }
                button {
                    +"Scan"
                    onClickFunction = {
                        val info = inMemoryStorage.planetInfo(fauna.planetId)
                        galaxy.planets[fauna.planetId]?.landAndDiscover(info)
                        el<HTMLInputElement>("${fauna.uniqueId}-scan-counter").valueAsNumber = 100.0
                        info.scan.lifeScans[fauna.name] = 100
                        replaceElement("${fauna.uniqueId}-details") {
                            detailsTable(fauna)
                        }
                        persistMemory()
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
