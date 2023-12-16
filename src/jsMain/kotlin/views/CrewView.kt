package views

import kotlinx.html.*
import replaceElement
import updateUrl

fun crewView() {
    updateUrl("crew")
    replaceElement {
        div {
            id = "crew-view"
            navButtons()
            div {
                id = "sections"
                div("section-view-box") {
                    id = "crew-list"
                    h2 { +"Crew" }
                    div("accent-line") { +"Go catch a smile" }
                    ul {
                        li {
                            +"Site by "
                            a("https://github.com/ManApart/starfield-eye", target = "_blank") { +"ManApart" }
                        }
                        li { +"Star and Planet data from Starfield Game Files" }
                        li { +"Fonts and Icons from Starfield Game Files" }
                        li {
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
                        li {
                            +"Resource Data, Flora and Fauna Data, Planet Data and more from "
                            a("https://starfieldwiki.net/wiki/Home", target = "_blank") { +"Starfield Wiki" }
                        }
                        li {
                            +"Optional Live Game Connection (Docking) by "
                            a("https://www.nexusmods.com/starfield/mods/4280", target = "_blank") { +"stonegdi" }
                        }
                        li { +"Quotes from Vladimir Sall, who runs The Eye in Starfield. The Eye is used to gaze at the heavens and \"see what's out there\"" }
                    }
                }
                div("section-view-box") {
                    id = "directory"
                    h2 { +"Directory" }

                    div("accent-line") { +"More than just the you and I" }
                    ul {
                        li { a("https://manapart.github.io/starfield-mod-manager-site/index.html", target = "_blank") { +"My CLI Mod Manager" } }
                        li { a("https://starfieldwiki.net/wiki/Home", target = "_blank") { +"Starfield Wiki" } }
                        li { a("https://inara.cz/starfield", target = "_blank") { +"Starfield Inara" } }
                        li { a("https://starfield.lukium.ai/locations", target = "_blank") { +"Starfield Locations" } }
                        li { a("https://starfieldradio.com/", target = "_blank") { +"Starfield Radio" } }
                        li { a("https://www.nexusmods.com/starfield/mods/7689", target = "_blank") { +"Starfield External Map Helper" } }
                    }
                }
            }

        }
    }
}