import kotlinx.browser.document
import kotlinx.browser.window
import kotlinx.html.dom.append
import kotlinx.html.js.div

fun main() {
    window.onload = {
        document.body!!.append {
            div { +"Hello world" }
        }
    }

}