package views

import FloraWikiData
import components.screenshot
import el
import floraReference
import galaxy
import kotlinx.dom.addClass
import kotlinx.dom.removeClass
import kotlinx.html.*
import kotlinx.html.dom.append
import kotlinx.html.js.a
import kotlinx.html.js.onClickFunction
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
                display(flora)
            }

        }
    }
}

fun floraView(flora: FloraWikiData) {
    val root = el("flora-view")
    root.innerHTML = ""
    root.removeClass("section-view-box")
    root.addClass("section-view-box")
    root.append {
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