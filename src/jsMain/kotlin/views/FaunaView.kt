package views

import components.screenshot
import el
import faunaReference
import floraReference
import kotlinx.dom.addClass
import kotlinx.dom.removeClass
import kotlinx.html.*
import kotlinx.html.dom.append

fun faunaView(system: Int, planet: Int) {
    val root = el("fauna-view")
    root.innerHTML = ""
    root.removeClass("section-view-box")
    faunaReference["$system-$planet"]?.let { planetFlora ->
        root.addClass("section-view-box")
        root.append {
            h2 { +"Fauna" }

            planetFlora.forEach { flora ->
                with(flora) {
                    div("fauna-entry") {
                        p { +name.substring(0, name.indexOf("(")).trim() }

                        screenshot("fauna/$name")

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

        }
    }
}