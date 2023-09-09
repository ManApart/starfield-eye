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
    val maxX = systems.values.maxOf { it.pos.x }
    val maxY = systems.values.maxOf { it.pos.y }
    val maxZ = systems.values.maxOf { it.pos.z }
    val minX = systems.values.minOf { it.pos.x }
    val minY = systems.values.minOf { it.pos.y }
    val minZ = systems.values.minOf { it.pos.z }
    val distX = maxX - minX
    val distY = maxY - minY
    val distZ = maxZ - minZ

    root.innerHTML = ""
    root.append {
        div {
            id = "galaxy"
            systems.values.forEach { system ->
                val x = ((system.pos.x - minX) / distX) * 90 + 2
                val y = ((system.pos.y - minY) / distY) * 90 + 2
                div("galaxy-system") {
                    id = system.star.name
                    style = "top: ${y}%; left: ${x}vw;"
                    div("system-circle") { }
                    div("system-name") { +system.star.name }
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