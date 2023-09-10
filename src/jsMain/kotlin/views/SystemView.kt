package views

import StarSystem
import kotlinx.browser.window
import kotlinx.html.*
import kotlinx.html.dom.append
import kotlinx.html.js.onClickFunction

fun systemView(system: StarSystem, planetId: Int = 0) {
    window.history.pushState(null, "null", "#system/${system.star.id}/$planetId")
    val root = el("root")
    root.innerHTML = ""
    root.append {
        h1 {
            id = "system-title"
            +system.star.name
        }
        div {
            +"Back"
            onClickFunction = {
                renderGalaxy()
            }
        }
        ol {
//            system.planets.forEach {
//                li { +it.value.name }
//            }
        }
    }
}