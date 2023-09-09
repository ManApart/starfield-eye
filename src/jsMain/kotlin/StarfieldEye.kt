import kotlinx.browser.window
import views.renderGalaxy


fun main() {
    window.onload = {
        loadAll().then { renderGalaxy() }
    }

}