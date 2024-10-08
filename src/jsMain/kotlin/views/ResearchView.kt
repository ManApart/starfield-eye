package views

import ProjectState
import ResearchCategory
import ResearchProject
import ResourceType
import components.externalLink
import components.resourceSquare
import el
import getProjectState
import inMemoryStorage
import kotlinx.dom.addClass
import kotlinx.dom.removeClass
import kotlinx.html.*
import kotlinx.html.js.div
import kotlinx.html.js.onClickFunction
import org.w3c.dom.HTMLElement
import replaceElement
import researchProjects
import updateState
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
//Link to skill on wiki, or perks page (scroll to category)
//Link to Project on Wiki
//Link to materials on wiki, or catalog
//Mark Material as tracked
//Track User progress
//Mark complete marks skill complete/incomplete
//Marking in/complete marks parents/children
//Page for all tracked materials

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

private fun displayCategory(category: ResearchCategory, currentProject: ResearchProject = researchProjects[category]!!.first()) {
    replaceElement("projects", "research-section") {
        h2 { +"Research Projects" }
        researchProjects[category]!!.forEach { project ->
            val styles = if (researchProjects[category]?.first() == project) {
                "research-project project-active"
            } else "research-project"
            div(styles) {
                id = project.id
                span {
                    +project.id
                    onClickFunction = { displayProject(category, project) }
                }
                span("project-state") {
                    when (project.getProjectState()) {
                        ProjectState.COMPLETED -> {
                            completePic()
                            +"Completed"
                        }

                        ProjectState.BLOCKED -> {
                            blockedPic()
                            +"Blocked"
                        }

                        ProjectState.NONE -> +"None"
                    }
                    onClickFunction = {
                        val newState = when (project.getProjectState()) {
                            ProjectState.COMPLETED -> ProjectState.NONE
                            ProjectState.NONE -> ProjectState.COMPLETED
                            ProjectState.BLOCKED -> ProjectState.BLOCKED
                        }
                        project.updateState(newState)
                        displayCategory(category)
                    }
                }
            }
        }
    }
    displayProject(category, currentProject)
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
        project.perks.forEach { (perk, _) ->
            div("research-perk") {
                span { id = "perk-req-${perk}" }
            }
        }
    }
    project.perks.forEach { (perkName, perkLevel) -> setPerkReqs(perkName, perkLevel, project) }

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
            val resource = ResourceType.entries.firstOrNull { it.matches(material.name) }
            div("research-perk") {
                if (resource != null) {
                    resourceSquare(resource, "research-resource")
                } else {
                    a("https://starfieldwiki.net${material.url}", classes = "misc-resource") {
                        target = "_blank"
                        img {
                            src = "./images/research/misc.svg"
                            title = material.name
                        }
                    }
                }
                + "${material.name} x${material.count}"
                a("#catalogue/${material.name}", classes="search-icon") {
                    img {
                        src = "./images/research/search.svg"
                    }
                }
            }
        }
    }
    replaceElement("description", "research-section") {
        h2 { +"Description" }
        div("research-perk") {
            if (project.description != "") {
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

private fun setPerkReqs(perkName: String, perkLevel: Int, project: ResearchProject) {
    replaceElement("perk-req-$perkName") {
        span {
            id = "perk-req-${perkName}"
            if (inMemoryStorage.perkLevel(perkName) >= perkLevel) completePic() else blockedPic()
            +"$perkName $perkLevel"
            externalLink(" ", "https://starfieldwiki.net/wiki/Starfield:$perkName")
            onClickFunction = {
                if (inMemoryStorage.perkLevel(perkName) >= perkLevel) {
                    inMemoryStorage.perks[perkName] = perkLevel - 1
                } else {
                    inMemoryStorage.perks[perkName] = perkLevel
                }
                setPerkReqs(perkName, perkLevel, project)
                displayCategory(currentCategory, project)
            }
        }
    }
}


private fun TagConsumer<HTMLElement>.completePic() {
    img(classes = "link-pic") {
        src = "./images/research/complete.svg"
    }
}

private fun TagConsumer<HTMLElement>.blockedPic() {
    img(classes = "link-pic") {
        src = "./images/research/blocked.svg"
    }
}
