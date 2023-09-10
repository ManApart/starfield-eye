package views

import StarSystem
import kotlinx.html.*
import kotlinx.html.dom.append
import kotlinx.html.js.onClickFunction

fun systemView(system: StarSystem) {
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