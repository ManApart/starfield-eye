package views

import el
import exportPlayerInfo
import galaxy
import getPlanets
import inMemoryStorage
import kotlinx.browser.document
import kotlinx.browser.window
import kotlinx.dom.addClass
import kotlinx.dom.removeClass
import kotlinx.html.*
import kotlinx.html.dom.append
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
                id = "export-button"
                +"Export"
                title = "Download user entered data"
                onClickFunction = { exportPlayerInfo() }
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
        }

        div {
            id = "galaxy"
            systems.values.forEach { system ->
                val x = 95 - (((system.pos.x - summary.minX) / summary.distX) * 90 + 2)
                val y = ((system.pos.y - summary.minY) / summary.distY) * 90 + 2
                div("galaxy-system") {
                    id = system.star.name
                    style = "top: ${y}%; left: ${x}vw;"
                    div("system-circle") { }
                    div("system-name") { +system.star.name }
                    onClickFunction = {
                        systemView(system)
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
