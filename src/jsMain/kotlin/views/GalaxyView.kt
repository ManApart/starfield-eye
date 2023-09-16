package views

import inMemoryStorage
import kotlinx.browser.document
import kotlinx.browser.window
import kotlinx.html.button
import kotlinx.html.div
import kotlinx.html.dom.append
import kotlinx.html.id
import kotlinx.html.js.onClickFunction
import kotlinx.html.style
import org.w3c.dom.HTMLElement

fun renderGalaxy() {
    window.history.pushState(null, "null", "#galaxy")
    val systems = inMemoryStorage.galaxy.systems
    val summary = inMemoryStorage.galaxy.summary
    val root = el("root")

    root.innerHTML = ""
    root.append {
        div {
            id = "nav"
            button {
                +"Catalogue"
                onClickFunction = { catalogueView()}
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
}

fun el(id: String) = document.getElementById(id) as HTMLElement
fun <T> el(id: String) = document.getElementById(id) as T