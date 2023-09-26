package views

import el
import exportPlayerInfo
import galaxy
import getPlanets
import importPlayerInfo
import inMemoryStorage
import kotlinx.browser.document
import kotlinx.browser.window
import kotlinx.dom.addClass
import kotlinx.dom.removeClass
import kotlinx.html.*
import kotlinx.html.dom.append
import kotlinx.html.js.html
import kotlinx.html.js.onClickFunction
import kotlinx.html.js.onKeyUpFunction
import org.w3c.dom.HTMLElement
import org.w3c.dom.HTMLInputElement
import planetDivs
import searchPlanets
import starDivs

fun renderGalaxy() {
    window.history.pushState(null, "null", "#galaxy")
    val systems = galaxy.systems
    val summary = galaxy.summary
    val root = el("root")

    root.innerHTML = ""
    root.append {
        div { backgroundStars() }
        div {
            id = "nav"
            button {
                +"Catalogue"
                onClickFunction = { catalogueView() }
            }
            button {
                id = "crew-button"
                +"Crew"
                title = "View crew"
                onClickFunction = { crewView() }
            }
            input(classes = "search") {
                id = "galaxy-search"
                placeholder = "Highlight star by name of star or planets"
                onKeyUpFunction = {
                    highlightStar(el<HTMLInputElement>("galaxy-search").value.lowercase())
                }
            }
            button {
                id = "export-button"
                +"Export"
                title = "Download user entered data"
                onClickFunction = { exportPlayerInfo() }
            }
            button {
                id = "import-button"
                +"Import"
                title = "Import saved user data"
                onClickFunction = { importPlayerInfo() }
            }
        }

        div {
            id = "galaxy"
            systems.values.forEach { system ->
                val x = 95 - (((system.pos.x - summary.minX) / summary.distX) * 90 + 2)
                val y = ((system.pos.y - summary.minY) / summary.distY) * 90 + 2
                div("galaxy-system") {
                    id = system.star.name
                    style = "top: ${y}%; left: ${x}%;"

                    val offset = starOffsets[system.star.name]

                    div("system-circle") { onClickFunction = { systemView(system) } }
                    div("system-name") {
                        +system.star.name
                        onClickFunction = { systemView(system) }
                        offset?.let { (offsetX, offsetY) ->
                            style = "top: ${offsetY}px; left: ${offsetX}px"

                        }
                    }
                    if (offset != null && starLines.contains(system.star.name)) {
                        val (offsetX, offsetY) = offset
                        unsafe {
                            val lineX = (offsetX / 3).let { if (it != 0) it - 4 else it }
                            val lineY = (offsetY / 3).let { if (it != 0) it + 4 else it }
                            val topAdjust = (offsetY/10).let { if (offsetY > 0) it * -1 + 15 else it}
                            +"""<svg width="10" height="10" class="star-line" style="top: ${topAdjust}px">
                                |<line x1="0" y1="0" x2="$lineX" y2="$lineY" stroke="white"/>
                                |</svg>""".trimMargin()
                        }
                    }
                }
            }
        }
    }
    saveHtmlRefs()
    readyStars()
}

private fun saveHtmlRefs() {
    starDivs = galaxy.systems.values.associate { it.star.id.toString() to el(it.star.name) }
}

private fun highlightStar(searchText: String) {
    val (shown, hidden) = galaxy.systems.values.partition { system ->
        system.star.name.lowercase().contains(searchText)
                || system.planets.values.any { it.name.lowercase().contains(searchText) }
    }

    shown.forEach {
        starDivs[it.star.id.toString()]?.apply {
            addClass("visible-block")
            removeClass("hidden")
        }
    }
    hidden.forEach {
        starDivs[it.star.id.toString()]?.apply {
            addClass("hidden")
            removeClass("visible-block")
        }
    }
}

private val starOffsets = mapOf(
    "Alpha Ternion" to Pair(-40, -30),
    "Beta Ternion" to Pair(60, -30),
    "Arcturus" to Pair(0, -30),
    "Bohr" to Pair(0, -30),
    "Copernicus" to Pair(0, 10),
    "Copernicus Minor" to Pair(0, -40),
    "Enlil" to Pair(0, -30),
    "Feynman" to Pair(0, -30),
    "Foucault" to Pair(0, -30),
    "Gamma Vulpes" to Pair(0, -30),
    "Hawking" to Pair(0, -30),
)

private val starLines = listOf(
    "Copernicus Minor",
    "Copernicus",
)