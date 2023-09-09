package views

import inMemoryStorage
import kotlinx.browser.document
import kotlinx.html.div
import kotlinx.html.dom.append
import kotlinx.html.id
import kotlinx.html.js.onClickFunction
import kotlinx.html.style
import org.w3c.dom.HTMLElement

fun renderGalaxy() {
    val systems = inMemoryStorage.systems
    println("Found ${systems.keys.size} systems")
    val root = el("root")
    println("root")
    val maxX = systems.values.maxOf { it.pos.x }
    val maxY = systems.values.maxOf { it.pos.y }
    val maxZ = systems.values.maxOf { it.pos.z }
    root.innerHTML = ""
    root.append {
        div {
            id = "galaxy"
            systems.values.forEach { system ->
                val x = system.pos.x / maxX
                val y = system.pos.y / maxY
                div("galaxy-system") {
                    id = system.star.name
                    style = "top: ${y}%; left: ${x}vw;"
                    div("star-circle") { }
                    div("star-name") { +system.star.name }
                    onClickFunction = {
                        println("Clicked ${system.star.name}")
                    }
                }
            }
        }
    }
}

fun el(id: String) = document.getElementById(id) as HTMLElement
fun <T> el(id: String) = document.getElementById(id) as T