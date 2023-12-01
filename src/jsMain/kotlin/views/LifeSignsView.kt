package views

import FaunaWikiData
import FloraWikiData
import Planet
import el
import faunaDivs
import faunaReference
import floraDivs
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
import searchLifeSigns
import searchPlanets
import starDivs

fun lifeSignsView() {
    window.history.pushState(null, "null", "#lifesigns")
    replaceElement {
        div {
            id = "life-signs-view"
            div {
                id = "nav"
                navButtons()
                input(classes = "search") {
                    id = "search"
                    placeholder = "Filter: Name, Resources etc. Comma separated"
                    value = lifeSignsSearchOptions.searchText
                    onKeyUpFunction = {
                        lifeSignsSearchOptions.searchText = el<HTMLInputElement>("search").value
                        searchLifeSigns()
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
    saveHtmlRefs()
    searchLifeSigns()
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
                    id = flora.uniqueId
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
                    id = fauna.uniqueId
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
    val planets = getPlanets()
    starDivs = galaxy.systems.values.map { it.star.id.toString() to el<HTMLElement?>(it.star.name) }.filter { it.second != null }.toMap() as Map<String, HTMLElement>
    planetDivs = getPlanets().map { it.uniqueId to el<HTMLElement?>(it.uniqueId) }.filter { it.second != null }.toMap() as Map<String, HTMLElement>
    floraDivs = planets.mapNotNull { floraReference[it.uniqueId] }.flatten().associate { it.uniqueId to el(it.uniqueId) }
    faunaDivs = planets.mapNotNull { faunaReference[it.uniqueId] }.flatten().associate { it.uniqueId to el(it.uniqueId) }
}

fun filterLife(shownFlora: List<FloraWikiData>, shownFauna: List<FaunaWikiData>) {
    val shownPlanetIds = (shownFauna.mapNotNull { it.planetId } + shownFlora.mapNotNull { it.planetId }).toSet()
    val shownStarIds = (shownFauna.mapNotNull { it.planetId?.split("-")?.first() } + shownFlora.mapNotNull { it.planetId?.split("-")?.first() }).toSet()
    val shownFloraIds = shownFlora.map { it.uniqueId }
    val shownFaunaIds = shownFauna.map { it.uniqueId }

    val (shownStarHtml, hiddenStarHtml) = starDivs.entries.partition { (id, _) -> shownStarIds.contains(id) }
    val (shownPlanetHtml, hiddenPlanetHtml) = planetDivs.entries.partition { (id, _) -> shownPlanetIds.contains(id) }
    val (shownFloraHtml, hiddenFloraHtml) = floraDivs.entries.partition { (id, _) -> shownFloraIds.contains(id) }
    val (shownFaunaHtml, hiddenFaunaHtml) = faunaDivs.entries.partition { (id, _) -> shownFaunaIds.contains(id) }

    (shownStarHtml + shownPlanetHtml + shownFloraHtml + shownFaunaHtml).forEach { (_, html) ->
        html.addClass("visible-block")
        html.removeClass("hidden")
    }
    (hiddenStarHtml + hiddenPlanetHtml + hiddenFloraHtml + hiddenFaunaHtml).forEach { (_, html) ->
        html.addClass("hidden")
        html.removeClass("visible-block")
    }
}