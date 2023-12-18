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

fun showStringPicker(choices: Set<String>, result: (String) -> Unit) {
    val root = el("popup")
    root.removeClass("hidden")
    root.style.top = "${mouseY}px"
    root.style.left = "${mouseX}px"
    root.innerHTML = ""
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