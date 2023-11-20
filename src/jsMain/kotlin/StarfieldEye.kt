import kotlinx.browser.document
import kotlinx.browser.window
import org.w3c.dom.HTMLElement
import org.w3c.dom.HTMLInputElement
import org.w3c.dom.HTMLTextAreaElement
import org.w3c.dom.events.KeyboardEvent
import views.*

var galaxy: Galaxy = Galaxy()
var pageIsVisible = true
var pollHook: (Boolean) -> Unit = {}
var keyPressedHook: (KeyboardEvent) -> Unit = {}
var missionReference: Map<String, MissionWikiData> = mapOf()
var floraReference: Map<String, List<FloraWikiData>> = mapOf()
var faunaReference: Map<String, List<FaunaWikiData>> = mapOf()
var mouseX = 0.0
var mouseY = 0.0


fun getPlanets(): List<Planet> {
    return galaxy.systems.values.flatMap { it.planets.values }
}

fun main() {
    window.onload = {
        createDB()
        loadAll().then {
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
    when {
        windowHash.startsWith("#about") -> {
            aboutView()
        }

        windowHash.startsWith("#catalogue") -> {
            catalogueView()
        }

        windowHash.startsWith("#crew") -> {
            crewView()
        }

        windowHash.startsWith("#dock") -> {
            dockView()
        }

        windowHash.startsWith("#quests") -> {
            questView()
        }

        windowHash.startsWith("#outposts") -> {
            outpostsPage()
        }

        windowHash.startsWith("#misc-stats") -> {
            miscStatView()
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
}


fun el(id: String) = document.getElementById(id) as HTMLElement
fun <T> el(id: String) = document.getElementById(id) as T