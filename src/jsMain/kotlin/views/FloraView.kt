package views

import el
import floraReference
import kotlinx.dom.addClass
import kotlinx.dom.removeClass
import kotlinx.html.*
import kotlinx.html.dom.append

fun floraView(system: Int, planet: Int) {
    println("id $system $planet")
    val root = el("flora-view")
    root.innerHTML = ""
    root.removeClass("section-view-box")
    floraReference["$system-$planet"]?.let { planetFlora ->
        println("Found Flora")
        root.addClass("section-view-box")
        root.append {
            h2 { +"Flora" }

            planetFlora.forEach { flora ->
                with(flora) {
                    div("flora-entry") {
                        p { +name.substring(0, name.indexOf("(")).trim() }
                        table("detail-view-table") {

                            (listOf(
                                "Biomes" to biomes.joinToString(),
                                "Resource" to resource,
                            ) + other.entries.map { it.key to it.value })
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