package components

import el
import kotlinx.browser.window
import kotlinx.dom.addClass
import kotlinx.dom.removeClass
import kotlinx.html.div
import kotlinx.html.dom.append
import kotlinx.html.js.onClickFunction
import mouseX
import mouseY
import org.w3c.dom.HTMLElement
import kotlin.math.min

fun showStringPicker(choices: Set<String>, result: (String) -> Unit) {
    val root = el("popup")
    openPopup(root)
    root.append {
        println("Popup for $choices")
        choices.forEach { choice ->
            div("popup-choice") {
                +choice
                onClickFunction = {
                    closePopup()
                    result(choice)
                }
            }
        }
    }
    popupClickListener()
}

fun openPopup(root: HTMLElement, width: Int = 0) {
    root.removeClass("hidden")
    root.innerHTML = ""

    val maxX = window.outerWidth - width
    val x = min(mouseX.toInt(), maxX)
    root.style.top = "${mouseY}px"
    root.style.left = "${x}px"
}

private var popupClickCount = 0
fun popupClickListener() {
    window.onclick = { e ->
        popupClickCount++
        if (popupClickCount > 1 && e.target != el("popup")) {
            closePopup()
        }
    }
}

fun closePopup() {
    val root = el("popup")
    root.addClass("hidden")
    root.style.display = "hidden"
    root.innerHTML = ""
    window.onclick = {}
    popupClickCount = 0
}