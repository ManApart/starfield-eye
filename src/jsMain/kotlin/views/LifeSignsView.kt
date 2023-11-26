package views

import Planet
import el
import faunaReference
import floraReference
import galaxy
import getPlanets
import kotlinx.browser.window
import kotlinx.dom.addClass
import kotlinx.dom.removeClass
import kotlinx.html.*
import kotlinx.html.js.onClickFunction
import kotlinx.html.js.onKeyUpFunction
import kotlinx.html.js.onMouseOverFunction
import lifeSignsSearchOptions
import org.w3c.dom.HTMLElement
import org.w3c.dom.HTMLInputElement
import planetDivs
import replaceElement
import searchPlanets
import starDivs

fun lifeSignsView() {
    window.history.pushState(null, "null", "#lifesigns")
    replaceElement {
        div {
            id = "life-signs-view"
            div {
                id = "nav"
                button {
                    +"Back to Galaxy"
                    onClickFunction = {
                        renderGalaxy()
                    }
                }
                input(classes = "search") {
                    id = "search"
                    placeholder = "Filter: Name, Resources etc. Comma separated"
                    value = lifeSignsSearchOptions.searchText
                    onKeyUpFunction = {
                        lifeSignsSearchOptions.searchText = el<HTMLInputElement>("search").value
//                        searchPlanets()
                    }
                }
            }

            div("section-wrapper") {
                div("section-view-box") {
                    id = "life-sign-list"
                    lifeSignList()
                }
                div { id = "flora-view" }
                div { id = "fauna-view" }
            }
        }
    }
//    saveHtmlRefs()
//    searchPlanets()
}

private fun TagConsumer<HTMLElement>.lifeSignList() {
    val systems = galaxy.systems.values.map { system ->
        system.star.name to system.planets.values.map {
            it to Pair(
                floraReference[it.uniqueId] ?: listOf(), faunaReference[it.uniqueId] ?: listOf()
            )
        }
    }.filter { (_, planets) ->
        planets.any { it.second.first.isNotEmpty() || it.second.second.isNotEmpty() }
    }
    systems.forEach { (system, planets) ->
        div("system-catalogue-item") {
            id = system
            span {
                +system
                onClickFunction = {
                    clearFloraFaunaView()
                }
                onMouseOverFunction = {
                    clearFloraFaunaView()
                }
            }
        }
        planets.filter { it.second.first.isNotEmpty() || it.second.second.isNotEmpty() }.forEach { (planet, wikiData) ->
            div("planet-catalogue-item") {
                id = planet.uniqueId
                span {
                    +planet.name
                    onClickFunction = {
                        clearFloraFaunaView()
                    }
                    onMouseOverFunction = {
                        clearFloraFaunaView()
                    }
                }
            }
            wikiData.first.forEach { flora ->
                div("life-sign-catalogue-item") {
                    id = "${planet.uniqueId}-${flora.name}"
                    span {
                        +flora.name
                        onClickFunction = {
                            floraView(flora)
                            clearFaunaView()
                        }
                        onMouseOverFunction = {
                            floraView(flora)
                            clearFaunaView()
                        }
                    }
                }
            }
            wikiData.second.forEach { fauna ->
                div("life-sign-catalogue-item") {
                    id = "${planet.uniqueId}-${fauna.name}"
                    span {
                        +fauna.name
                        onClickFunction = {
                            faunaView(fauna)
                            clearFloraView()
                        }
                        onMouseOverFunction = {
                            faunaView(fauna)
                            clearFloraView()
                        }
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

fun filterLife(shown: List<Planet>) {
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