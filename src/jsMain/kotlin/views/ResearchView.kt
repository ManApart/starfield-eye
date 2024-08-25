package views

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
                //adjust icon size
                div { id = "research-network-canvas" }
            }
        }
    }
    buildNetwork()
}

private fun buildNetwork() {
    val lookup = mutableMapOf<Int, ResearchProject>()
    val reverseLookup = mutableMapOf<ResearchProject, Int>()
    val projects = researchProjects.values.flatten().associateBy { it.id }
    val nodes = projects.values.mapIndexed { i, project ->
        lookup[i] = project
        reverseLookup[project] = i
        Node(i, project.id, project.category.pic)
    }.toTypedArray()

    val edges = projects.values.flatMapIndexed { i, project ->
        project.prerequisites.entries.map { (name, rank) ->
            val from = reverseLookup[projects["$name $rank"]]!!
            Edge(from, i)
        }
    }.toTypedArray()

    buildNetwork(el("research-network-canvas"), lookup, nodes, edges)
}


private fun buildNetwork(container: HTMLElement, lookup: Map<Int, ResearchProject>, nodes: Array<Node>, edges: Array<Edge>) {
    val visData = VisData
    val visNet = Vis
    val options = getOptions()
    val data = js("{nodes: new visData.DataSet(nodes), edges: new visData.DataSet(edges)}")
    val network = js("new visNet.Network(container, data, options)") as Vis.Network
    network.on("selectNode") { event ->
        val selectedNode = (event["nodes"] as Array<Number>).first()
        lookup[selectedNode]?.let {
            println("Clicked ${it.id}")
        }
    }
}

private fun getOptions(): dynamic {
    return js(
        """{
        nodes: {
      borderWidth: 4,
      size: 30,
      color: {
        border: "#222222",
        background: "#596365",
      },
      font: { color: "#222222" },
    },
    edges: {
      color: "#596365",
    }
    }"""
    )
}
