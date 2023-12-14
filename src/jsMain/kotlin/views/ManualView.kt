package views

import components.linkableH2
import kotlinx.html.*
import org.w3c.dom.HTMLElement
import replaceElement
import updateUrl

fun manualView(section: String? = null) {
    updateUrl("manual", section)
    replaceElement {
        div {
            id = "manual-view"
            navButtons()
            div {
                id = "sections"
                div("section-view-box") {
                    id = "how-to-use"
                    linkableH2("Overview")
                    div("accent-line") { +"The winks and the nods" }

                    p { +"The Eye has a ton of features. If you're wondering how to use a specific page, read through its relevant section below." }

                    p {
                        +"Please report any issues as "
                        a(
                            href = "https://github.com/ManApart/starfield-eye/issues",
                            target = "_blank"
                        ) { +"github issues." }
                    }

                }
                div("section-view-box") {
                    linkableH2("Galaxy View")
                    div("accent-line") { +"Blinks in the blackest sea" }

                    p { +"Search the galaxy by name of the star or planets to see the system on the map." }
                    p { +"Click a system to view that system's star, planets, and resources" }
                    p { +"User data is stored on your browser. Use Export to save any user data you've added (outposts, labels, notes etc). Use Import to load your data back into the site (or onto another browser)." }
                }
                div("section-view-box") {
                    linkableH2("Catalogue View")
                    div("accent-line") { +"Time dances its years forward" }
                    p { +"The catalogue gives you access to a powerful search. Here you can search for systems and planets using a large number of criteria." }
                    p { +"Comma separated, you can search for things like planet names, available resources (by symbol or name), planet outposts, and more. Comma separated terms are considered an \"AND\", so you can filter for planets with a warm temperature that include iron." }
                    p { +"Search results display the system and any matching planets. By hovering/tapping a result, you'll see a detailed view of that star or planet. You can then view it's system to see more information about that system" }
                    p { +"If X-Ray is off, you'll still see entries, but details will be hidden unless they have been scanned etc" }
                }
                div("section-view-box") {
                    linkableH2("Life Signs View")
                    div("accent-line") { +"" }
                    p { +"This view works like the catalogue view but for flora and fauna instead of planets." }
                    p { +"Comma separated, you can search for things like creature names, available resource dropped, and most of its misc stats. Comma separated terms are considered an \"AND\", so you can filter for creatures that drop adhesive and are domesticable." }
                    p { +"Search results display the system, planet and any matching flora or fauna. By hovering/tapping a result, you'll see a detailed view of that creature. You can then view it's system to see more information about that system" }
                    p { +"If X-Ray is off, you'll still see entries, but details will be hidden unless they have been scanned etc" }
                }
                div("section-view-box") {
                    linkableH2("System View")
                    div("accent-line") { +"Do the meet and greets later" }
                    p { +"Clicking on a system sends you to the system view. This view includes an interactive orrery that lets you browse the various bodies of the system." }
                    p { +"Navigate by clicking the star, planets, or moons, or use the arrow keys." }
                    p { +"You can add your own data to any planet, or moon. This includes labels that can be searched for, adding your outposts names (that can be searched by name or by searching for \"outpost\", or even notes for a specific planet." }
                    p { +"Data is saved locally to the browser and can be imported or exported from the Galaxy View." }
                    p { +"Fast travel is in super alpha, but I'm releasing it in it's wip state. If you're connected to the game and have your in game starmap open, you can use this view and click the \"travel\" button. If everything works, your ship will set course to the planet you picked." }
                }
                div("section-view-box") {
                    linkableH2("Outposts View")
                    div("accent-line") { +"" }
                    p { +"The outpost view is meant to be your one stop shop while becoming a Shipping Magnate. If coupled with the advanced feature that allows you to fast travel, this page can allow you to quickly jump through each of your outposts, whether that's to set up shipping lines, harvest resources, or just enjoy the views." }
                    p { +"Outposts are added in the system view of the relevant system. You can use the catalogue or galaxy map to find the relevant system and planet and then add the outpost. Once it's added you can edit it there or in the outpost view." }
                    p { +"Outposts can have their name changed, notes added, and a representative picture uploaded." }
                    p { +"Outposts can be updated to show what organic and inorganic resources are extracted/created there. The dropdowns only present resources that are present on that planet." }
                    p { +"The 'By Resource' view sorts all your outposts by what resources they produce, so that you can quickly scan all your outposts and find the one you want to visit. With the advanced 'fast travel' feature, you can even chart your course to that outpost in a single click" }
                }
                div("section-view-box") {
                    linkableH2("Quests View")
                    div("accent-line") { +"" }
                    p { +"View all your quests and their current objective. You can search by quest name or stage, and toggle hiding completed quests or not." }
                    p { +"Use the buttons at the top to filter quests by general type." }
                }
                div("section-view-box") {
                    linkableH2("Stats View")
                    div("accent-line") { +"" }
                    p { +"View most of the stats from the in game stats page, including your progress towards achievements for relevant stats." }
                }
                div("section-view-box") {
                    linkableH2("Connecting to your Game")
                    div("accent-line") { +"And the darkness gets lit, just a candle more" }
                    p { +"Thanks to the incredible work of Stonegdi, The Eye can connect to your game and grab all sorts of data." }
                    p { +"I want to favor speed over stability, so things may break as we go. I have a long personal wishlist of things to add as I figure things out or functionality is added to tooling. In the near term I hope to make a stats page and make the map interactively. Ideally you'll be able to search the site for a planet, and then click it to chart the course in the in game map." }
                }
            }
        }
    }
}

private fun TagConsumer<HTMLElement>.linkableH2(text: String) {
    linkableH2("manual", text)
}