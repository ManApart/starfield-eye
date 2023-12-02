package views

import kotlinx.html.*
import replaceElement
import updateUrl

fun aboutView(addHistory: Boolean = true) {
    updateUrl("about", addHistory)
    replaceElement {
        div {
            id = "about-view"
            navButtons()
            div {
                id = "sections"
                div("section-view-box") {
                    id = "how-to-use"
                    h2 { +"About" }
                    div("accent-line") { +"The winks and the nods" }

                    p { +"Starfield Eye is an Ad Free, Searchable Starmap for Starfield." }

                    p { +"Open it on a second monitor while playing, or on your phone to plan your next adventure." }

                    p { +"Search for planets based on any combination of resources, traits, features, name and more. Easily find a planet for your outpost that contains Aluminum, Iron, and a mountain view." }

                    p { +"Browse system by system with an Orrery that matches the in game UI." }

                    p { +"Save your favorite planets or the ones you want to explore. Add the name of your outposts and then see them all by searching \"outpost\". All data is stored locally on your browser, so no login is required and nothing is tracked, but you can import and export data to back it up or share it between devices." }

                    p { +"The Crew page also links to similar sites made by others, so you can pick the one that works best for you." }

                    p {
                        +"Please report any issues as "
                        a(href = "https://github.com/ManApart/starfield-eye/issues", target = "_blank") { +"github issues." }
                    }

                }
                div("section-view-box") {
                    h2 { +"Galaxy View" }
                    div("accent-line") { +"Blinks in the blackest sea" }

                    p { +"Search the galaxy by name of the star or planets to see the system on the map." }
                    p { +"Click a system to view that system's star, planets, and resources" }
                    p { +"User data is stored on your browser. Use Export to save any user data you've added (outposts, labels, notes etc). Use Import to load your data back into the site (or onto another browser)." }
                }
                div("section-view-box") {
                    h2 { +"Catalogue View" }
                    div("accent-line") { +"Time dances its years forward" }
                    p { +"The catalogue gives you access to a powerful search. Here you can search for systems and planets using a large number of criteria." }
                    p { +"Comma separated, you can search for things like planet names, available resources (by symbol or name), planet outposts, and more. Comma separated terms are considered an \"AND\", so you can filter for planets with a warm temperature that include iron." }
                    p { +"Search results display the system and any matching planets. By hovering/tapping a result, you'll see a detailed view of that star or planet. You can then view it's system to see more information about that system" }
                }
                div("section-view-box") {
                    h2 { +"System View" }
                    div("accent-line") { +"Do the meet and greets later" }
                    p { +"Clicking on a system sends you to the system view. This view includes an interactive orrery that lets you browse the various bodies of the system." }
                    p { +"Navigate by clicking the star, planets, or moons, or use the arrow keys." }
                    p { +"You can add your own data to any planet, or moon. This includes labels that can be searched for, adding your outposts names (that can be searched by name or by searching for \"outpost\", or even notes for a specific planet." }
                    p { +"Data is saved locally to the browser and can be imported or exported from the Galaxy View." }
                    p { +"Fast travel is in super alpha, but I'm releasing it in it's wip state. If you're connected to the game and have your in game starmap open, you can use this view and click the \"travel\" button. If everything works, your ship will set course to the planet you picked." }

                }
                div("section-view-box") {
                    h2 { +"Connecting to your Game" }
                    div("accent-line") { +"And the darkness gets lit, just a candle more" }
                    p { +"Thanks to the incredible work of Stonegdi, The Eye can connect to your game and grab all sorts of data. This transforms the site from an interactive map into (hopefully) a full fledged companion app." }
                    p { +"I want to favor speed over stability, so things may break as we go. I have a long personal wishlist of things to add as I figure things out or functionality is added to tooling. In the near term I hope to make a stats page and make the map interactively. Ideally you'll be able to search the site for a planet, and then click it to chart the course in the in game map." }

                }
            }
        }
    }
}