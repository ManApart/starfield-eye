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

                researchProjects.keys.forEach { category ->
                    div("research-category") {
                        img(classes ="research-pic") {
                            src = category.pic

                        }
                        h2 { +category.prettyName }
                        +"Available projects: ${researchProjects[category]!!.size}"
                    }
                }
            }
        }
    }
}
