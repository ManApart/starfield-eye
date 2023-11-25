package views

import components.screenshot
import el
import floraReference
import kotlinx.dom.addClass
import kotlinx.dom.removeClass
import kotlinx.html.*
import kotlinx.html.dom.append
import org.w3c.dom.HTMLElement

fun floraView(system: Int, planet: Int) {
    val root = el("flora-view")
    root.innerHTML = ""
    root.removeClass("section-view-box")
    floraReference["$system-$planet"]?.let { planetFlora ->
        root.addClass("section-view-box")
        root.append {
            h2 { +"Flora" }

            planetFlora.forEach { flora ->
                with(flora) {
                    div("flora-entry") {
                        p { +name.substring(0, name.indexOf("(")).trim() }

                        screenshot("flora/$name")

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


fun clearFloraFaunaView() {
    el<HTMLElement?>("flora-view")?.let {
        it.removeClass("section-view-box")
        it.innerHTML = ""
    }
    el<HTMLElement?>("fauna-view")?.let {
        it.removeClass("section-view-box")
        it.innerHTML = ""
    }
}