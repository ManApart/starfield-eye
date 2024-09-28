package views

import Planet
import PointOfInterest
import StarSystem
import el
import kotlinx.dom.addClass
import kotlinx.dom.removeClass
import kotlinx.html.*
import kotlinx.html.dom.append
import kotlinx.html.js.onClickFunction
import org.w3c.dom.HTMLElement
import poiReference
import views.system.attemptTravel

fun pointOfInterestView(system: StarSystem, planetId: Int? = null) {
    val planet = planetId?.let { system.planets[it] }
    val planetHtmlId = planet?.id ?: "none"
    val genericRoot = el<HTMLElement?>("poi-view")
    val root = genericRoot ?: el("poi-view-${system.star.name}-$planetHtmlId")
    root.innerHTML = ""
    val poiList = poiReference[system.star.name]?.filter { it.planet == planet?.name }?.toList() ?: listOf()
//    println("${system.star.name} with planet ${planet?.name} has poi: $poiList")

    if (poiList.isNotEmpty()) {
        root.addClass("section-view-box")
        root.append {
            h2 { +"Points of Interest" }
            poiList.forEach {
                poiView(system, planet, it)
            }
        }
    }
}

private fun TagConsumer<HTMLElement>.poiView(system: StarSystem, planet: Planet?, poi: PointOfInterest) {
    div {
        id = "poi-list-${system.star.name}-${planet?.name}"
        h3 { +poi.name }
        //TODO - show an image for the type
        p { +poi.type.name }
        p { +poi.description }
        button {
            +"Travel"
            title = "Set course to planet. In Future hopefully direct to poi"
            onClickFunction = { attemptTravel(planet?.name ?: system.star.name) }
        }
        button {
            a("https://starfieldwiki.net${poi.wikiLink}", target = "_blank") {
                +"Wiki"
            }
            title = "View on the wiki"

        }
        hr { }
    }
}
