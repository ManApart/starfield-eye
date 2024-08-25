package views

import el
import kotlinx.html.*
import kotlinx.html.js.onClickFunction
import loadSampleData
import replaceElement
import researchProjects
import updateUrl

fun researchView(section: String? = null) {
    updateUrl("research", section)
    replaceElement {
        div {
            id = "research-view"
            navButtons()
            div("research") {
                div("research-accent") { +"Research Laboratory" }

                //TODO - filter by category
                //Display as graph
                //Track  User progress
                //Mark complete marks skill complete/incomplete
                //Marking in/complete marks parents/children
                researchProjects.entries.forEach { (category, projects) ->
                    div {
                        h3 { +category }
                        img { src = "./images/research/${category.lowercase()}.svg" }
                        projects.forEach { project ->
                            p { +"${project.name} ${project.rank}" }
                        }
                    }
                }

            }
        }
    }

}
