package views

import ResearchCategory
import ResearchProject
import Vis
import VisData
import el
import kotlinx.html.*
import kotlinx.html.js.div
import kotlinx.html.js.onClickFunction
import loadSampleData
import org.w3c.dom.HTMLElement
import replaceElement
import researchProjects
import updateUrl

class Node(
    @JsName("id") val id: Int,
    @JsName("label") val label: String,
    @JsName("image") var image: String,
    @JsName("shape") val shape: String = "circularImage"
)

data class Edge(
    @JsName("from") val from: Int,
    @JsName("to") val to: Int,
    @JsName("dashes") val dashes: Boolean = false,
    @JsName("arrows") val arrows: String? = undefined
) {
    override fun equals(other: Any?): Boolean {
        return other is Edge &&
                ((from == other.from && to == other.to) ||
                        (from == other.to && to == other.from))
    }

    override fun hashCode(): Int {
        return from + to
    }
}

private val currentCategory = ResearchCategory.PHARMACOLOGY

//TODO - filter by category
//Highlight current category
//Highlight current project
//Track  User progress
//Mark complete marks skill complete/incomplete
//Marking in/complete marks parents/children

fun researchView(section: String? = null) {
    updateUrl("research", section)
    replaceElement {
        div {
            id = "research-view"
            navButtons()
            div("research") {
                div("research-accent") {
                    id = "research-title"
                    +"Research Laboratory"
                }

                div("research-section") {
                    id = "categories"
                    researchProjects.keys.forEach { category ->
                        div("research-category") {
                            img(classes = "research-pic") {
                                src = category.pic
                            }
                            onClickFunction = {
                                el("research-title").innerText = "Research Laboratory - ${category.prettyName}"
                                displayCategory(category)
                            }
                        }
                    }
                }

                div("research-section") {
                    id = "projects"
                    h3 { +"Research Projects" }
                }

                div("research-section") {
                    id = "skills"
                    h3 { +"Required Skills" }
                }

                div("research-section") {
                    id = "pre-reqs"
                    h3 { +"Required Research" }
                }
                div("research-section") {
                    id = "materials"
                    h3 { +"Required Materials" }
                }
                div("research-section") {
                    id = "description"
                }
            }
        }
    }
    displayCategory(currentCategory)
}

private fun displayCategory(category: ResearchCategory) {
    replaceElement("projects") {
        div("research-section") {
            id = "projects"
            h3 { +"Research Projects" }
            researchProjects[category]!!.forEach { project ->
                div("research-project") {
                    +project.id
                    onClickFunction = { displayProject(project)}
                }
            }
        }
    }
    displayProject(researchProjects[category]!!.first())
}

private fun displayProject(project: ResearchProject) {
    replaceElement("skills") {
        h3 { +"Required Skills" }
        if (project.perks.isEmpty()){
            div("research-perk") { +"None" }
        }
        project.perks.forEach { perk ->
            div("research-perk") {
                +"${perk.key} ${perk.value}"
            }
        }
    }
    replaceElement("pre-reqs") {
        h3 { +"Required Research" }
        if (project.prerequisites.isEmpty()){
            div("research-perk") { +"None" }
        }
        project.prerequisites.forEach { req ->
            div("research-perk") {
                +"${req.key} ${req.value}"
            }
        }
    }
    replaceElement("materials") {
        h3 { +"Required Materials" }
        if (project.materials.isEmpty()){
            div("research-perk") { +"None" }
        }
        project.materials.forEach { material ->
            div("research-perk") {
                +"${material.name} ${material.count}"
            }
        }
    }
    replaceElement("description") {
        if (project.description != "") {
            h3 { +"Description" }
            div("research-perk") {
                unsafe {
                    +project.description
                }
            }
        }
    }
}
