package views

import kotlinx.browser.window
import kotlinx.html.*
import kotlinx.html.dom.append
import kotlinx.html.js.onClickFunction

fun crewView() {
    window.history.pushState(null, "null", "#crew")
    val root = el("root")
    root.innerHTML = ""
    root.append {
        div {
            id = "crew-view"
            backgroundStars()
            button {
                +"Back to Galaxy"
                onClickFunction = {
                    renderGalaxy()
                }
            }
            div("section-view-box") {
                id = "crew-list"
                p { +"Go catch a smile" }
                p {
                    +"Site by "
                    a("https://github.com/ManApart/starfield-eye", target = "_blank") { +"ManApart" }
                }
                p { +"Star and Planet data from Starfield Game Files" }
                p { +"Fonts and Icons from Starfield Game Files" }
                p {
                    +"Resource Data from "
                    a(
                        "https://docs.google.com/spreadsheets/d/1seE2vzP_8Whs43C-6CXpoHPyJMFGoUH4TkSzeJqMHm4/edit#gid=231618918",
                        target = "_blank"
                    ) { +"Google Docs" }
                    +" originally pulled from "
                    a(
                        "https://hardcoregamer.com/db/starfield-all-locations-systems-planets-moons/",
                        target = "_blank"
                    ) { +"Hardcore Gamer" }
                }
                p {
                    +"Inorganic Resource Data from "
                    a("https://starfieldwiki.net/wiki/Starfield:Resources", target = "_blank") { +"Starfield Wiki" }
                }
            }
        }
    }
    readyStars()
}