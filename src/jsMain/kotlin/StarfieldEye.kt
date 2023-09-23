import kotlinx.browser.document
import kotlinx.browser.window
import org.w3c.dom.HTMLInputElement
import org.w3c.dom.HTMLTextAreaElement
import org.w3c.dom.events.KeyboardEvent
import views.catalogueView
import views.navigateOrrery
import views.renderGalaxy
import views.systemView

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

    window.addEventListener("keyup", { event ->
        val key = (event as KeyboardEvent)
        if (document.activeElement !is HTMLTextAreaElement || document.activeElement !is HTMLInputElement) {
            navigateOrrery(key)
        }
    })

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
        else -> renderGalaxy()
    }
}