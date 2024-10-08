import kotlinx.browser.document
import kotlinx.browser.window
import kotlinx.dom.addClass
import kotlinx.dom.createElement
import kotlinx.html.TagConsumer
import kotlinx.html.dom.append
import org.w3c.dom.HTMLElement
import org.w3c.dom.HTMLInputElement
import org.w3c.dom.HTMLTextAreaElement
import org.w3c.dom.events.KeyboardEvent
import views.*
import views.lifeSigns.lifeSignsView
import views.system.systemView

var galaxy: Galaxy = Galaxy()
var pageIsVisible = true
var pollHook: (Boolean) -> Unit = {}
var keyPressedHook: (KeyboardEvent) -> Unit = {}
var missionReference: Map<String, MissionWikiData> = mapOf()
var floraReference: Map<String, List<FloraWikiData>> = mapOf()
var faunaReference: Map<String, List<FaunaWikiData>> = mapOf()
var poiReference: Map<String?, List<PointOfInterest>> = mapOf()
var perks: Map<String, Perk> = mapOf()
var researchProjects: Map<ResearchCategory, List<ResearchProject>> = mapOf()
var mouseX = 0.0
var mouseY = 0.0


fun getPlanets(): List<Planet> {
    return galaxy.systems.values.flatMap { it.planets.values }
}

fun main() {
    window.onload = {
        createDB()
        loadAll(el("root")).then {
            doRouting()
            pollData()
        }
        genStars()
    }
    window.addEventListener("popstate", { e ->
        doRouting()
    })

    window.addEventListener("keyup", { e ->
        val event = (e as KeyboardEvent)
        if (document.activeElement !is HTMLTextAreaElement && document.activeElement !is HTMLInputElement && event.key in listOf(
                "ArrowRight",
                "ArrowLeft",
                "ArrowUp",
                "ArrowDown"
            )
        ) {
            keyPressedHook(event)
        } else if (document.activeElement is HTMLInputElement && event.key == "Enter") {
            keyPressedHook(event)
        }
    })
    window.addEventListener("keydown", { e ->
        val event = (e as KeyboardEvent)
        if (document.activeElement !is HTMLTextAreaElement && document.activeElement !is HTMLInputElement && event.key in listOf(
                "ArrowRight",
                "ArrowLeft",
                "ArrowUp",
                "ArrowDown"
            )
        ) {
            event.preventDefault()
        }
    })

    document.onmousemove = { e ->
        mouseX = e.pageX
        mouseY = e.pageY
        val x = e.clientX / window.innerWidth.toFloat()
        val y = e.clientY / window.innerHeight.toFloat()
        panStars(x, y)
    }

    window.addEventListener("visibilitychange", {
        val state = js("document.visibilityState") as String
        pageIsVisible = (state == "visible")
        if (pageIsVisible) pollData()
    })
}


fun doRouting() {
    doRouting(window.location.hash)
}

fun doRouting(windowHash: String) {
    pollHook = {}
    keyPressedHook = {}
    val section = windowHash.split("/").takeIf { it.size == 2 }?.last()
    section?.let { println("Section: $it") }
    when {
        windowHash.startsWith("#about") -> aboutView(section)
        windowHash.startsWith("#manual") -> manualView(section)
        windowHash.startsWith("#lifesigns") -> lifeSignsView()
        windowHash.startsWith("#crew") -> crewView()
        windowHash.startsWith("#dock") -> dockView(section)
        windowHash.startsWith("#quests") -> questView()
        windowHash.startsWith("#outposts") -> outpostsPage()
        windowHash.startsWith("#misc-stats") -> miscStatView()
        windowHash.startsWith("#perks") -> perkView(section)
        windowHash.startsWith("#research") -> researchView(section)
        windowHash.startsWith("#catalogue") -> {
            val parts = windowHash.replace("#catalogue", "").split("/").filter { it.isNotEmpty() }
            val text = parts.firstOrNull()?.replace("%20", " ") ?: planetSearchOptions.searchText
            catalogueView(text)
        }

        windowHash.startsWith("#system/") -> {
            val parts = windowHash.replace("#system/", "").split("/")
            if (parts.size == 2) {
                val system = galaxy.systems[parts.first().toInt()]!!
                val planet = parts.last().toIntOrNull() ?: 0
                systemView(system, planet)
            }
        }

        else -> renderGalaxy()
    }
    section?.let { el<HTMLElement?>(it)?.scrollIntoView() }
}

fun updateUrl(path: String, section: String? = null) {
    val pathName = path.split("/").first().capitalize()
    val newPath = path + (section?.let { "/$it" } ?: "")
    if (!window.location.href.endsWith("#$newPath")) {
        window.history.pushState(null, "", "#$newPath")
    }
    document.title = "The Eye: $pathName"
}

fun el(id: String) = document.getElementById(id) as HTMLElement
fun <T> el(id: String) = document.getElementById(id) as T

fun replaceElement(id: String = "root", rootClasses: String? = null, newHtml: TagConsumer<HTMLElement>.() -> Unit) {
    val root = el<HTMLElement?>(id)
    if (root != null) {
        val newRoot = document.createElement("div") {
            this.id = id
            rootClasses?.split(" ")?.forEach {
                this.addClass(it)
            }
        }
        newRoot.append {
            newHtml()
        }
        root.replaceWith(newRoot)
    }
}

fun isMobile(): Boolean {
    val screenRatio = window.screen.width / window.screen.height.toDouble()
    return screenRatio < 3 / 4.0
}
