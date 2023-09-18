import kotlinx.browser.window
import views.catalogueView
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