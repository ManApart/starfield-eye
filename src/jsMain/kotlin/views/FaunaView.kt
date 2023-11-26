package views

import FaunaWikiData
import components.screenshot
import el
import faunaReference
import floraReference
import galaxy
import kotlinx.dom.addClass
import kotlinx.dom.removeClass
import kotlinx.html.*
import kotlinx.html.dom.append
import kotlinx.html.js.onClickFunction
import org.w3c.dom.HTMLElement

fun faunaView(system: Int, planet: Int) {
    val root = el("fauna-view")
    root.innerHTML = ""
    root.removeClass("section-view-box")
    faunaReference["$system-$planet"]?.let { planetFauna ->
        root.addClass("section-view-box")
        root.append {
            h2 { +"Fauna" }

            planetFauna.forEach { fauna ->
                display(fauna)
            }

        }
    }
}

fun faunaView(fauna: FaunaWikiData) {
    val root = el("fauna-view")
    root.innerHTML = ""
    root.removeClass("section-view-box")
    root.addClass("section-view-box")
    root.append {
        h2 { +"Fauna" }
        display(fauna, true)
    }
}

private fun TagConsumer<HTMLElement>.display(fauna: FaunaWikiData, linkToSystem: Boolean = false) {
    with(fauna) {
        div("fauna-entry") {
            p { +name.substring(0, name.indexOf("(")).trim() }

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

            table("detail-view-table") {
                (listOf(
                    "Biomes" to biomes.joinToString(),
                    "Resource" to resource,
                    "Temperament" to temperament.name.lowercase().capitalize(),
                    "Abilities" to abilities.joinToString(),
                ) + other.entries.map { it.key to it.value }.sortedBy { it.first })
                    .filter { (_, data) -> data.isNotBlank() && data != "0" }
                    .forEach { (title, data) ->
                        tr {
                            td { +title }
                            td { +data }
                        }
                    }
            }
        }
    }
}