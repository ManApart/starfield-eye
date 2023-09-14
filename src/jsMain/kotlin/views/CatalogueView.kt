package views

import Planet
import getPlanets
import kotlinx.browser.document
import kotlinx.browser.window
import kotlinx.dom.addClass
import kotlinx.dom.removeClass
import kotlinx.html.div
import kotlinx.html.dom.append
import kotlinx.html.h3
import kotlinx.html.id
import kotlinx.html.input
import kotlinx.html.js.onClickFunction
import kotlinx.html.js.onKeyUpFunction
import org.w3c.dom.HTMLInputElement
import planetDivs
import planetSearchOptions
import searchPlanets

fun catalogueView() {
    window.history.pushState(null, "null", "#catalogue")
    val root = el("root")
    root.innerHTML = ""
    root.append {
        div {
            id = "catalogue-view"
            backgroundStars()
            h3 {
                +"Back to Galaxy"
                onClickFunction = {
                    renderGalaxy()
                }
            }
            div {
                id = "search-span"
                input {
                    id = "search"
                    placeholder = "Filter: Name, Resources etc. Comma separated"
                    value = planetSearchOptions.searchText
                    onKeyUpFunction = {
                        planetSearchOptions.searchText = (document.getElementById("search") as HTMLInputElement).value
                        searchPlanets()
                    }
                }
            }

            div {
                id = "planet-list"

            }
        }
    }
    buildPlanets()
}

fun buildPlanets() {
    val parent = el("planet-list")
    parent.append {
        getPlanets().forEach { planet ->
            div("planet-catalogue-item") {
                id = planet.uniqueId
                +planet.name
            }
        }
    }
    planetDivs = getPlanets().associate { it.uniqueId to el(it.uniqueId) }
}

//TODO Also filter stars

fun filterPlanets(shown: List<Planet>) {
    val shownMap = shown.associateBy { it.uniqueId }
    val (shownHtml, hiddenHtml) = planetDivs.entries.partition { (id, _) -> shownMap.containsKey(id) }
    shownHtml.forEach { (_, html) ->
        html.addClass("visible-block")
        html.removeClass("hidden")
    }
    hiddenHtml.forEach { (_, html) ->
        html.addClass("hidden")
        html.removeClass("visible-block")
    }
}