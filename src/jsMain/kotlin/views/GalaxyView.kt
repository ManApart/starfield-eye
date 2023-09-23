package views

import exportPlayerInfo
import galaxy
import inMemoryStorage
import kotlinx.browser.document
import kotlinx.browser.window
import kotlinx.html.*
import kotlinx.html.dom.append
import kotlinx.html.js.onClickFunction
import org.w3c.dom.HTMLElement

fun renderGalaxy() {
    window.history.pushState(null, "null", "#galaxy")
    val systems = galaxy.systems
    val summary = galaxy.summary
    val root = el("root")

    root.innerHTML = ""
    root.append {
        div {
            id = "nav"
            backgroundStars()
            button {
                +"Catalogue"
                onClickFunction = { catalogueView()}
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
        }
        div {
            id = "galaxy"
            systems.values.forEach { system ->
                val x = 95-(((system.pos.x - summary.minX) / summary.distX) * 90 + 2)
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
    readyStars()
}

fun el(id: String) = document.getElementById(id) as HTMLElement
fun <T> el(id: String) = document.getElementById(id) as T