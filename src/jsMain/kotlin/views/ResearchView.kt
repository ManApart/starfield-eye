package views

import ResearchCategory
import ResearchProject
import Vis
import VisData
import el
import kotlinx.dom.addClass
import kotlinx.dom.removeClass
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

//TODO
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
                            id = category.name
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
                div("research-wrapper") {

                    div("research-section") {
                        id = "projects"
                        h2 { +"Research Projects" }
                    }

                    div("research-section") {
                        id = "skills"
                        h2 { +"Required Skills" }
                    }

                    div("research-section") {
                        id = "pre-reqs"
                        h2 { +"Required Research" }
                    }
                    div("research-section") {
                        id = "materials"
                        h2 { +"Required Materials" }
                    }
                    div("research-section") {
                        id = "description"
                    }
                }
            }
        }
    }
    displayCategory(currentCategory)
}

private fun displayCategory(category: ResearchCategory) {
    replaceElement("projects", "research-section") {
        h2 { +"Research Projects" }
        researchProjects[category]!!.forEach { project ->
            val styles = if (researchProjects[category]?.first() == project){
                "research-project project-active"
            } else "research-project"
            div(styles) {
                id = project.id
                +project.id
                onClickFunction = { displayProject(category, project) }
            }
        }
    }
    displayProject(category, researchProjects[category]!!.first())
    ResearchCategory.entries.forEach {
        el<HTMLElement?>(it.name)?.removeClass("category-active")
    }
    el(category.name).addClass("category-active")
}

private fun displayProject(category: ResearchCategory, project: ResearchProject) {
    replaceElement("skills", "research-section") {
        h2 { +"Required Skills" }
        if (project.perks.isEmpty()) {
            div("research-perk") { +"None" }
        }
        project.perks.forEach { perk ->
            div("research-perk") {
                +"${perk.key} ${perk.value}"
            }
        }
    }
    replaceElement("pre-reqs", "research-section") {
        h2 { +"Required Research" }
        if (project.prerequisites.isEmpty()) {
            div("research-perk") { +"None" }
        }
        project.prerequisites.forEach { req ->
            div("research-perk") {
                +"${req.key} ${req.value}"
            }
        }
    }
    replaceElement("materials", "research-section") {
        h2 { +"Required Materials" }
        if (project.materials.isEmpty()) {
            div("research-perk") { +"None" }
        }
        project.materials.forEach { material ->
            div("research-perk") {
                +"${material.name} ${material.count}"
            }
        }
    }
    replaceElement("description", "research-section") {
        if (project.description != "") {
            h2 { +"Description" }
            div("research-perk") {
                unsafe {
                    +project.description
                }
            }
        }
    }
    researchProjects[category]?.forEach {
        el<HTMLElement?>(it.id)?.removeClass("project-active")
    }
    el(project.id).addClass("project-active")
}
