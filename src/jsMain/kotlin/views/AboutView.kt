package views

import components.linkableH2
import kotlinx.html.*
import org.w3c.dom.HTMLElement
import replaceElement
import updateUrl

fun aboutView(section: String? = null) {
    updateUrl("about", section)
    replaceElement {
        div {
            id = "about-view"
            navButtons()
            div {
                id = "sections"
                div("section-view-box") {
                    id = "how-to-use"
                    linkableH2("About")
                    div("accent-line") { +"The winks and the nods" }

                    p { +"Starfield Eye is an Ad Free Companion App for Starfield." }

                    p { +"Open it on a second monitor while playing, or on your phone to plan your next adventure." }

                    p { +"The Crew page also links to similar sites made by others, so you can pick the one that works best for you." }

                    p {
                        +"Please report any issues as "
                        a(
                            href = "https://github.com/ManApart/starfield-eye/issues",
                            target = "_blank"
                        ) { +"github issues." }
                    }

                }

                div("section-view-box") {
                    linkableH2("Instant Features")
                    div("accent-line") { +"Overdrawn from Lady Luck three times over" }
                    p { +"These features work right now without any setup required. Try them out today!" }

                    h3 { +"Player Progress" }
                    ul {
                        li { +"Complete your own codex of the game's many flora, fauna, and planets" }
                        li { +"Upload your own screenshots for flora, fauna, planets, and outposts" }
                        li { +"Use X-Ray to view all of Starfield's data, or turn it off and only get access to what you've discovered already in game" }
                        li { +"Toggle between full knowledge and discovered data at any time" }
                        li { +"Mark stars discovered, planets landed on, and fauna/flora scan percent" }
                        li { +"Track what you've scanned down to the specific resources on a planet, or mark an entire planet 100% surveyed" }
                        li { +"Track your progress towards visiting every star system, landing on each planet, or even 100% surveying the game" }
                    }

                    h3 { +"Planets" }
                    ul {
                        li {
                            +"Search for planets based on any combination of resources, traits, features, name and more"
                            ul {
                                li { +"Easily find a planet for your outpost that contains Aluminum, Iron, and a mountain view" }
                            }
                        }
                        li { +"Highlight a given system on the galaxy map to more easily find it in game" }
                        li { +"Browse system by system with an Orrery that matches the in game UI" }
                        li { +"Bookmark your favorite planets or the ones you want to explore" }
                        li { +"Combine bookmarks, search, and the advanced travel feature to create travel routes and lists of quick travel options" }
                    }
                    h3 { +"Life Signs" }
                    ul {
                        li { +"Search for plants or creatures that produce a specific resource and are domesticable" }
                        li { +"Search on traits like biome, temperament, abilities, and other misc stats" }
                        li { +"Use the site as your own personal pokedex, and scan them all!" }
                    }

                    h3 { +"Outposts" }
                    ul {
                        li { +"Track each of your outposts with a name and an uploaded screenshot" }
                        li { +"Add your organic and inorganic produced resources so you can view which outpost produces what resource" }
                        li { +"View all outposts sorted by what resource they produce" }
                    }
                    h3 { +"Data" }
                    ul {
                        li { +"No login required" }
                        li { +"No personal data tracked or sent to any server" }
                        li { +"Everything stored locally in your browser" }
                        li { +"Export data to back it up or share it between devices" }
                    }
                    h3 { +"And More" }
                    ul {
                        li {
                            +"Planets, Outposts, Flor and Fauna can all have screenshots uploaded"
                            ul {
                                li { +"Use screenshots to easily tell outposts apart on the outposts page" }
                                li { +"Take a screenshot of each creature you've scanned, so you can look at it as you view its stats on your phone later" }
                                li { +"Uploaded screenshots can be separately exported and backed up" }
                            }
                        }
                        li { +"Planets, Flora, Fauna, and Resources all link to their respective wiki pages so that you can read more about them" }
                    }
                }
                div("section-view-box") {
                    linkableH2("Advanced Features")
                    div("accent-line") { +"Looking to trade names to captain" }
                    p {
                        +"These features require you to Dock to work. You'll need to go through the setup on the "
                        a("#dock/preparing_to_dock") { +"Dock page," }
                        +" which requires that you have SFSE installed."
                    }

                    h3 { +"Fast Travel" }
                    ul {
                        li {
                            +"Chart a course to any planet or outpost with one button click"
                            ul {
                                li { +"This opens your in game map and charts a course" }
                            }
                        }
                    }
                    h3 { +"Quest Tracker" }
                    ul {
                        li { +"Track all your active and complete quests" }
                        li { +"Filter quests by type (main, faction, activity etc)" }
                        li { +"Search quests by name or stage" }
                        li { +"View your current objective" }
                    }
                    h3 { +"Stat Tracking" }
                    ul {
                        li { +"Track most of the in game stat pages stats" }
                        li { +"View your stats updating in near real time, or on your phone on the go" }
                        li { +"For many relevant stats, see your progress towards unlocking a related achievement" }
                    }
                }

                div("section-view-box") {
                    linkableH2("Planned Features")
                    div("accent-line") { +"Turn the lights off when you leave" }
                    p { +"While there are no guarantees, these are the features I'm hoping to build out once we have proper modding tools" }

                    ul {
                        li { +"Travel directly to outposts instead of routing to a planet" }
                        li { +"Automatically updating player progress on scanning / landing on a planet" }
                        li { +"Surfacing more in game data" }
                        li { +"Extract game data in order to make app info more accurate" }
                    }
                }
            }
        }
    }
}

private fun TagConsumer<HTMLElement>.linkableH2(text: String) {
    linkableH2("about", text)
}