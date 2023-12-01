package views

import kotlinx.html.*
import kotlinx.html.js.onClickFunction
import org.w3c.dom.HTMLElement


fun TagConsumer<HTMLElement>.navButtons(navClass: String = "header-nav") {
    div(navClass) {
        id = "nav"

        div {
            id = "about-nav"
            button {
                id = "about-button"
                +"About"
                title = "How this site works"
                onClickFunction = { aboutView() }
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
    }
}
