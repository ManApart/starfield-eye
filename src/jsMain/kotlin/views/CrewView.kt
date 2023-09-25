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
                    id = "how-to-use"
                    h2 { +"How to Use" }

                    h3 { +"About" }
                    div("accent-line")
                    p { +"The Eye is a free, searchable map for Starfield." }
                    p { +"The Eye is open source, ad free, and doesn't track you or require a login." }

                    h3 { +"Galaxy View" }
                    div("accent-line")
                    p { +"Search the galaxy by name of the star or planets to see the system on the map." }
                    p { +"Click a system to view that system's star, planets, and resources" }
                    p { +"User data is stored on your browser. Use Export to save any user data you've added (outposts, labels, notes etc). Use Import to load your data back into the site (or onto another browser)." }

                    h3 { +"Catalogue View" }
                    div("accent-line")
                    p { +"The catalogue gives you access to a powerful search. Here you can search for systems and planets using a large number of criteria." }
                    p { +"Comma separated, you can search for things like planet names, available resources (by symbol or name), planet outposts, and more. Comma separated terms are considered an \"AND\", so you can filter for planets with a warm temperature that include iron." }
                    p { +"Search results display the system and any matching planets. By hovering/tapping a result, you'll see a detailed view of that star or planet. You can then view it's system to see more information about that system" }

                    h3 { +"System View" }
                    div("accent-line")
                    p { +"Clicking on a system sends you to the system view. This view includes an interactive orrery that lets you browse the various bodies of the system." }
                    p { +"Navigate by clicking the star, planets, or moons, or use the arrow keys." }
                    p { +"You can add your own data to any planet, or moon. This includes labels that can be searched for, adding your outposts names (that can be searched by name or by searching for \"outpost\", or even notes for a specific planet." }
                    p { +"Data is saved locally to the browser and can be imported or exported from the Galaxy View." }
                }
                div("section-view-box") {
                    id = "crew-list"
                    h2 { +"Crew" }
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
                div("section-view-box") {
                    id = "directory"
                    h2 { +"Directory" }
                    p { a("https://starfieldwiki.net/wiki/Home", target = "_blank") { +"Starfield Wiki" } }
                    p { a("https://inara.cz/starfield", target = "_blank") { +"Starfield Inara" } }
                    p { a("https://starfield.lukium.ai/locations", target = "_blank") { +"Starfield Locations" } }
                }
            }

        }
    }
    readyStars()
}