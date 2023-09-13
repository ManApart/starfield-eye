package views

import inMemoryStorage
import kotlinx.browser.window
import kotlinx.html.div
import kotlinx.html.dom.append
import kotlinx.html.h3
import kotlinx.html.id
import kotlinx.html.js.onClickFunction

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
        }
        div {
            id = "planet-list"
            inMemoryStorage.galaxy.systems.values.forEach { system ->
                system.planets.values.forEach { planet ->
                    div("planet-catalogue-item"){
                        +planet.name
                    }
                }
            }
        }
    }
}