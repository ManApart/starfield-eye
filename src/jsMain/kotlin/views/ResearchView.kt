package views

import el
import kotlinx.html.*
import kotlinx.html.js.onClickFunction
import loadSampleData
import replaceElement
import updateUrl

fun researchView(section: String? = null) {
    updateUrl("research", section)
    replaceElement {
        div {
            id = "about-view"
            navButtons()
            div {
                id = "sections"
                div("section-view-box") {
                    id = "how-to-use"
                    div("accent-line") { +"The winks and the nods" }

                    p { +"Starfield Eye is an Ad Free Companion App for Starfield." }

                    p { +"Open it on a second monitor while playing, or on your phone to plan your next adventure." }

                    p {
                        span {
                            id = "load-sample-data-text"
                            +"If you'd like to explore with sample data, click this button: "
                        }
                        button {
                            +"Load Sample Data"
                            onClickFunction = { loadSampleData(el("load-sample-data-text")) }
                        }
                    }

                    p { +"The Crew page also links to similar sites made by others, so you can pick the one that works best for you." }

                    p {
                        +"Please report any issues as "
                        a(
                            href = "https://github.com/ManApart/starfield-eye/issues",
                            target = "_blank"
                        ) { +"github issues." }
                    }

                }
            }
        }
    }

}
