package views

import Planet
import el
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
import kotlinx.html.js.onMouseOverFunction
import org.w3c.dom.HTMLElement
import org.w3c.dom.HTMLInputElement
import planetDivs
import searchPlanets
import starDivs

fun catalogueView() {
    window.history.pushState(null, "null", "#catalogue")
    val root = el("root")
    root.innerHTML = ""
    root.append {
        div {
            id = "catalogue-view"
            backgroundStars()
            button {
                +"Back to Galaxy"
                onClickFunction = {
                    renderGalaxy()
                }
            }
            div {
                id = "search-span"
                img("Search Icon", "images/search.svg") { id = "search-icon" }
                input {
                    id = "search"
                    placeholder = "Filter: Name, Resources etc. Comma separated"
                    value = inMemoryStorage.planetSearchOptions.searchText
                    onKeyUpFunction = {
                        inMemoryStorage.planetSearchOptions.searchText = el<HTMLInputElement>("search").value
                        searchPlanets()
                    }
                }
            }

            div("section-wrapper") {
                div("section-view-box") {
                    id = "planet-list"
                    planetList()
                }
                div("section-view-box") {
                    id = "detail-view"
                }
            }
        }
    }
    saveHtmlRefs()
    detailView(galaxy.systems.values.first(), 0, false, true)
    searchPlanets()
    readyStars()
}

private fun TagConsumer<HTMLElement>.planetList() {
    galaxy.systems.values.forEach { system ->
        div("system-catalogue-item") {
            id = system.star.name
            span {
                +system.star.name
                onClickFunction = { detailView(system, 0, false, true) }
                onMouseOverFunction = { detailView(system, 0, false, true) }
            }
        }
        system.planets.values.forEach { planet ->
            div("planet-catalogue-item") {
                id = planet.uniqueId
                span {
                    +planet.name
                    onClickFunction = { detailView(system, planet.id, false, true) }
                    onMouseOverFunction = { detailView(system, planet.id, false, true) }
                }
            }
        }
    }
}

private fun saveHtmlRefs() {
    planetDivs = getPlanets().associate { it.uniqueId to el(it.uniqueId) }
    starDivs = galaxy.systems.values.associate { it.star.id.toString() to el(it.star.name) }
}

fun filterPlanets(shown: List<Planet>) {
    val shownMap = shown.associateBy { it.uniqueId }
    val (shownHtml, hiddenHtml) = planetDivs.entries.partition { (id, _) -> shownMap.containsKey(id) }
    val shownStars = shown.groupBy { it.starId.toString() }.keys
    val (shownStarHtml, hiddenStarHtml) = starDivs.entries.partition { (id, _) -> shownStars.contains(id) }

    (shownStarHtml + shownHtml).forEach { (_, html) ->
        html.addClass("visible-block")
        html.removeClass("hidden")
    }
    (hiddenStarHtml + hiddenHtml).forEach { (_, html) ->
        html.addClass("hidden")
        html.removeClass("visible-block")
    }
}