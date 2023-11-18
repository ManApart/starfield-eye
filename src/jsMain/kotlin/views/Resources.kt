package views

import ResourceType
import el
import kotlinx.browser.window
import kotlinx.dom.addClass
import kotlinx.dom.removeClass
import kotlinx.html.TagConsumer
import kotlinx.html.div
import kotlinx.html.dom.append
import kotlinx.html.js.onClickFunction
import kotlinx.html.style
import kotlinx.html.title
import org.w3c.dom.DOMRect
import org.w3c.dom.HTMLElement

fun TagConsumer<HTMLElement>.resourceSquares(resources: Set<ResourceType>) {
    resources.forEach { resource ->
        div("resource") {
            style = "background-color: #${resource.color}"
            div("resource-inner") {
                +resource.name
            }
            title = resource.readableName
        }
    }
}

fun showResourcePicker(resources: Set<ResourceType>, pos: DOMRect, result: (ResourceType) -> Unit) {
    val root = el("popup")
    root.removeClass("hidden")
    root.style.top = "${pos.top}px"
    root.style.left = "${pos.left}px"
    root.innerHTML = ""
    root.append {
        resources.forEach { resource ->
            div("resource") {
                style = "background-color: #${resource.color}"
                div("resource-inner") {
                    +resource.name
                }
                title = resource.readableName
                onClickFunction = {
                    closePopup()
                    result(resource)
                }
            }
        }
    }
    popupClickListener()
}

private var popupClickCount = 0
private fun popupClickListener() {
    window.onclick = { e ->
        popupClickCount++
        if (popupClickCount > 1 && e.target != el("popup")) {
            closePopup()
        }
    }
}

private fun closePopup() {
    val root = el("popup")
    root.addClass("hidden")
    root.style.display = "hidden"
    root.innerHTML = ""
    window.onclick = {}
    popupClickCount = 0
}