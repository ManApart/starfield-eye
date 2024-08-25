package views

import components.toggle
import doRouting
import inMemoryStorage
import kotlinx.html.*
import kotlinx.html.js.onClickFunction
import org.w3c.dom.HTMLElement
import persistMemory
import views.lifeSigns.lifeSignsView


fun TagConsumer<HTMLElement>.navButtons(navClass: String = "header-nav") {
    div(navClass) {
        id = "nav"

        div("nav-section") {
            id = "about-nav"
            button {
                id = "about-button"
                +"About"
                title = "What this site does"
                onClickFunction = { aboutView() }
            }
            button {
                id = "manual-button"
                +"Manual"
                title = "How to use this site"
                onClickFunction = { manualView() }
            }
            button {
                id = "crew-button"
                +"Crew"
                title = "View Credits"
                onClickFunction = { crewView() }
            }
            button {
                id = "dock-button"
                +"Dock"
                title = "Change Settings"
                onClickFunction = { dockView() }
            }
        }
        div("nav-section") {
            button {
                +"Galaxy"
                onClickFunction = { renderGalaxy() }
            }
            button {
                +"Catalogue"
                onClickFunction = { catalogueView() }
            }
            button {
                +"Life Signs"
                onClickFunction = { lifeSignsView() }
            }
            button {
                +"Outposts"
                onClickFunction = { outpostsPage() }
            }
            button {
                id = "quest-button"
                +"Quests"
                title = "View Quests"
                onClickFunction = { questView() }
            }
            button {
                id = "misc-stat-button"
                +"Stats"
                title = "View Misc Stats"
                onClickFunction = { miscStatView() }
            }
            button {
                id = "perks-button"
                +"Perks"
                title = "View Player Perks"
                onClickFunction = { perkView() }
            }
            button {
                id = "research-button"
                +"Research"
                title = "View Player Research"
                onClickFunction = { researchView() }
            }
            div("toggle-wrapper") {
                +"X-ray"
                title = "Show all data, or only what you've discovered so far"
                toggle(inMemoryStorage::showUndiscovered) {
                    persistMemory()
                    doRouting()
                }
            }
        }
    }
}
