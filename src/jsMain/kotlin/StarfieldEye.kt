import kotlinx.browser.document
import kotlinx.browser.window
import org.w3c.dom.HTMLElement
import org.w3c.dom.HTMLInputElement
import org.w3c.dom.HTMLTextAreaElement
import org.w3c.dom.events.KeyboardEvent
import views.*

var galaxy: Galaxy = Galaxy()

fun getPlanets(): List<Planet> {
    return galaxy.systems.values.flatMap { it.planets.values }
}

fun main() {
    window.onload = {
        createDB()
        loadAll().then { doRouting() }
    }
    window.addEventListener("popstate", { e ->
        doRouting()
    })

    window.addEventListener("keyup", { e ->
        val event = (e as KeyboardEvent)
        if (document.activeElement !is HTMLTextAreaElement && document.activeElement !is HTMLInputElement && event.key in listOf("ArrowRight", "ArrowLeft", "ArrowUp", "ArrowDown")) {
            navigateOrrery(event)
        }
    })
    window.addEventListener("keydown", { e ->
        val event = (e as KeyboardEvent)
        if (document.activeElement !is HTMLTextAreaElement && document.activeElement !is HTMLInputElement && event.key in listOf("ArrowRight", "ArrowLeft", "ArrowUp", "ArrowDown")) {
            event.preventDefault()
        }
    })

    document.onmousemove = { e ->
        val x = e.clientX / window.innerWidth.toFloat()
        val y = e.clientY / window.innerHeight.toFloat()
        panStars(x, y)
    }

}


fun doRouting() {
    doRouting(window.location.hash)
}

fun doRouting(windowHash: String) {
    when {
        windowHash.startsWith("#system/") -> {
            val parts = windowHash.replace("#system/", "").split("/")
            if (parts.size == 2){
                val system = galaxy.systems[parts.first().toInt()]!!
                val planet = parts.last().toIntOrNull() ?: 0
                systemView(system, planet)
            }
        }
        windowHash.startsWith("#catalogue") -> {
            catalogueView()
        }
        windowHash.startsWith("#crew") -> {
            crewView()
        }
        else -> renderGalaxy()
    }
}


fun el(id: String) = document.getElementById(id) as HTMLElement
fun <T> el(id: String) = document.getElementById(id) as T