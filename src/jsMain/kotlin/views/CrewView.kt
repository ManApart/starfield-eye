package views

import el
import kotlinx.browser.window
import kotlinx.html.*
import kotlinx.html.dom.append
import kotlinx.html.js.onClickFunction
import org.w3c.dom.HTMLElement

fun crewView() {
    window.history.pushState(null, "null", "#crew")
    val root = el("root")
    root.innerHTML = ""
    root.append {
        div {
            id = "crew-view"
            backgroundStars()
            div {
                id = "nav"
                button {
                    +"Back to Galaxy"
                    onClickFunction = {
                        renderGalaxy()
                    }
                }
            }
            div {
                id = "sections"
                div("section-view-box") {
                    id = "crew-list"
                    h2 { +"Crew" }
                    div("accent-line") { +"Go catch a smile" }
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
                    p {+"Quotes from Vladimir Sall, who runs The Eye in Starfield. The Eye is used to gaze at the heavens and \"see what's out there\""}
                }
                div("section-view-box") {
                    id = "directory"
                    h2 { +"Directory" }

                    div("accent-line") { +"More than just the you and I" }

                    p { a("https://starfieldwiki.net/wiki/Home", target = "_blank") { +"Starfield Wiki" } }
                    p { a("https://inara.cz/starfield", target = "_blank") { +"Starfield Inara" } }
                    p { a("https://starfield.lukium.ai/locations", target = "_blank") { +"Starfield Locations" } }
                }
            }

        }
    }
    readyStars()
}