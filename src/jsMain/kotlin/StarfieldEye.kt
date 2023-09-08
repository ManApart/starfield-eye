import kotlinx.browser.document
import kotlinx.browser.window
import kotlinx.html.dom.append
import kotlinx.html.js.div


//TODO - add star parallax background
fun main() {
    window.onload = {
        document.body!!.append {
            div { +"The Eye" }
        }
        loadAll().then { renderGalaxy() }
    }

}