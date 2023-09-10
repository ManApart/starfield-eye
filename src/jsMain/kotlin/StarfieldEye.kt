import kotlinx.browser.window
import views.renderGalaxy

//TODO - url for star and for planet

fun main() {
    window.onload = {
        loadAll().then { renderGalaxy() }
    }

}