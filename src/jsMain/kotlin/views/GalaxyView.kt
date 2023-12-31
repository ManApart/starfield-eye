package views

import el
import galaxy
import inMemoryStorage
import kotlinx.dom.addClass
import kotlinx.dom.removeClass
import kotlinx.html.*
import kotlinx.html.js.onClickFunction
import kotlinx.html.js.onKeyUpFunction
import org.w3c.dom.HTMLElement
import org.w3c.dom.HTMLInputElement
import replaceElement
import starDivs
import updateUrl
import views.system.systemView

private var searchText = ""

fun renderGalaxy() {
    updateUrl("galaxy")

    replaceElement {
        galaxy()
        input(classes = "search") {
            id = "galaxy-search"
            placeholder = "Highlight star by name of star or planets"
            value = searchText
            onKeyUpFunction = {
                searchText = el<HTMLInputElement>("galaxy-search").value.lowercase()
                highlightStar(searchText)
            }
        }
        navButtons("galaxy-nav")
    }
    saveHtmlRefs()
    highlightStar(searchText)
}

private fun TagConsumer<HTMLElement>.galaxy() {
    val systems = galaxy.systems
    val summary = galaxy.summary
    div {
        id = "galaxy-wrapper"
        div {
            id = "galaxy"
            systems.values.forEach { system ->
                val x = 95 - (((system.pos.x - summary.minX) / summary.distX) * 90 + 2)
                val y = ((system.pos.y - summary.minY) / summary.distY) * 90 + 2
                val discovered = inMemoryStorage.isDiscovered(system.star.id)
                val dimmed = if(discovered) "" else "dimmed-star"
                div("galaxy-system $dimmed") {
                    id = system.star.name
                    style = "top: ${y}%; left: ${x}%;"

                    val offset = starOffsets[system.star.name]

                    div("system-circle") {
                        onClickFunction = { systemView(system) }
                    }
                    div("system-name") {
                        +system.star.name
                        onClickFunction = { systemView(system) }
                        offset?.let { (offsetX, offsetY) ->
                            style = "top: ${offsetY}px; left: ${offsetX}px"

                        }
                    }
                    if (offset != null && starLines.contains(system.star.name)) {
                        val (offsetX, offsetY) = offset
                        val lineX = (offsetX / 3).let { if (it != 0) it - 4 else it }
                        val lineY = (offsetY / 3).let { if (it != 0) it + 4 else it }
                        val topAdjust = (offsetY / 10).let { if (offsetY > 0) it * -1 + 15 else it }

                        div {
                            unsafe {
                                +"""<svg width="10" height="10" class="star-line" style="top: ${topAdjust}px;">
                                |<line x1="0" y1="0" x2="$lineX" y2="$lineY" stroke="white"/>
                                |</svg>""".trimMargin()
                            }
                        }
                    }
                }
            }
        }
    }
}

private fun saveHtmlRefs() {
    val systems = galaxy.systems.values
    starDivs = systems.associate { it.star.id.toString() to el(it.star.name) }
}

private fun highlightStar(searchText: String) {
    val (shown, hidden) = galaxy.systems.values.partition { system ->
        inMemoryStorage.isDiscovered(system.star.id) &&
                (system.star.name.lowercase().contains(searchText)
                || system.planets.values.any { it.name.lowercase().contains(searchText) })
    }

    shown.forEach {
        starDivs[it.star.id.toString()]?.apply {
            removeClass("dimmed-star")
        }
    }
    hidden.forEach {
        starDivs[it.star.id.toString()]?.apply {
            addClass("dimmed-star")
        }
    }
}

private val starOffsets = mapOf(
    "Aranae" to Pair(-30, 0),
    "Alpha Ternion" to Pair(-60, -30),
    "Alpha Centauri" to Pair(60, -30),
    "Alpha Andraste" to Pair(-45, -30),
    "Alpha Marae" to Pair(30, -40),
    "Alpha Tirna" to Pair(-45, -30),
    "Arcturus" to Pair(0, -30),
    "Apollo" to Pair(-10, 0),
    "Artemis" to Pair(35, 0),
    "Beta Andraste" to Pair(-45, 0),
    "Beta Tirna" to Pair(-45, 0),
    "Beta Marae" to Pair(-30, -60),
    "Bannoc Prime" to Pair(-60, -30),
    "Bannoc Secondus" to Pair(0, 30),
    "Barnard's Star" to Pair(55, -10),
    "Bel" to Pair(10, 0),
    "Bohr" to Pair(0, -30),
    "Bessel" to Pair(20, -35),
    "Copernicus" to Pair(0, 10),
    "Charybdis" to Pair(0, -40),
    "Cheyenne" to Pair(30, -40),
    "Canis" to Pair(-10, -30),
    "Carinae" to Pair(0, -50),
    "Copernicus Minor" to Pair(0, -40),
    "Enlil" to Pair(0, -30),
    "Eta Cassiopeiae" to Pair(40, -60),
    "Feynman" to Pair(-30, -30),
    "Foucault" to Pair(0, -30),
    "Gamma Vulpes" to Pair(0, -30),
    "Groombridge 1830" to Pair(0, -50),
    "Hawking" to Pair(0, -30),
    "Haldeman" to Pair(0, -30),
    "Kapteyn's Star" to Pair(-40, 0),
    "Katydid" to Pair(0, -30),
    "Lantana" to Pair(0, -30),
    "Minerva" to Pair(-35, -15),
    "Maheo" to Pair(-30, -40),
    "McClure" to Pair(30, -30),
    "Narion" to Pair(0, -30),
    "Olympus" to Pair(35, 0),
    "Oborum Prime" to Pair(0, -30),
    "Oborum Secondus" to Pair(50, -45),
    "Porrima" to Pair(0, -30),
    "Piazzi" to Pair(-40, -35),
    "Proxima Ternion" to Pair(60, -30),
    "Procyon A" to Pair(0, -45),
    "Procyon B" to Pair(60, -30),
    "Sirius" to Pair(20, 0),
    "Tau Ceti" to Pair(0, 20),
    "Valo" to Pair(-20, 0),
    "Van Maanen's Star" to Pair(80, 0),
    "Xi Ophiuchi" to Pair(0, 10),
    "Zeta Ophiuchi" to Pair(40, 0),
)

private val starLines = listOf(
    "Alpha Ternion",
    "Alpha Centauri",
    "Alpha Marae",
    "Bannoc Secondus",
    "Bannoc Prime",
    "Beta Marae",
    "Bel",
    "Bessel",
    "Copernicus Minor",
    "Cheyenne",
    "Copernicus",
    "Carinae",
    "Eta Cassiopeiae",
    "Groombridge 1830",
    "Maheo",
    "Oborum Secondus",
    "Proxima Ternion",
    "Procyon A",
    "Procyon B",
    "Piazzi",
    "Tau Ceti",
    "Van Maanen's Star",
    "Xi Ophiuchi",
)