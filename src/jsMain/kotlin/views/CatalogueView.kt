package views

import Planet
import el
import galaxy
import getPlanets
import kotlinx.dom.addClass
import kotlinx.dom.removeClass
import kotlinx.html.*
import kotlinx.html.js.onClickFunction
import kotlinx.html.js.onKeyUpFunction
import kotlinx.html.js.onMouseOverFunction
import org.w3c.dom.HTMLElement
import org.w3c.dom.HTMLInputElement
import planetDivs
import planetSearchOptions
import replaceElement
import searchPlanets
import starDivs
import updateUrl
import views.lifeSigns.clearFloraFaunaView
import views.system.detailView

fun catalogueView() {
    updateUrl("catalogue")
    replaceElement {
        div {
            id = "catalogue-view"
            div {
                id = "nav"
                navButtons()
                input(classes = "search") {
                    id = "search"
                    placeholder = "Filter: Name, Resources etc. Comma separated"
                    value = planetSearchOptions.searchText
                    onKeyUpFunction = {
                        planetSearchOptions.searchText = el<HTMLInputElement>("search").value
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
                div { id = "outpost-view" }
            }
        }
    }
    saveHtmlRefs()
    detailView(galaxy.systems.values.first(), 0, false, true)
    searchPlanets()
}

private fun TagConsumer<HTMLElement>.planetList() {
    galaxy.systems.values.forEach { system ->
        div("system-catalogue-item") {
            id = system.star.name
            span {
                +system.star.name
                onClickFunction = {
                    detailView(system, 0, false, true)
                    clearOutpostsView()
                    clearFloraFaunaView()
                }
                onMouseOverFunction = {
                    detailView(system, 0, false, true)
                    clearOutpostsView()
                    clearFloraFaunaView()
                }
            }
        }
        system.planets.values.forEach { planet ->
            div("planet-catalogue-item") {
                id = planet.uniqueId
                span {
                    +planet.name
                    onClickFunction = {
                        detailView(system, planet.id, false, true)
                        views.system.outpostsView(system, planet.id)
                    }
                    onMouseOverFunction = {
                        detailView(system, planet.id, false, true)
                        views.system.outpostsView(system, planet.id)
                    }
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