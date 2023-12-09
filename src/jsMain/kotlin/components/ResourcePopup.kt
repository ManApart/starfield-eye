package components

import ResourceType
import doRouting
import el
import kotlinx.browser.window
import kotlinx.dom.addClass
import kotlinx.dom.removeClass
import kotlinx.html.*
import kotlinx.html.dom.append
import kotlinx.html.js.onClickFunction
import mouseX
import mouseY
import org.w3c.dom.HTMLElement
import persistMemory
import kotlin.reflect.KProperty0

fun TagConsumer<HTMLElement>.resourceSquares(
    resources: Set<ResourceType>,
    checkBoxes: KProperty0<MutableSet<Int>>? = null
) {
    if (checkBoxes != null) {
        resources.forEachIndexed { i, resourceType -> resourceSquare(resourceType, i, checkBoxes) }
    } else {
        resources.forEach { resourceSquare(it) }
    }
}

fun TagConsumer<HTMLElement>.resourceSquare(resource: ResourceType) {
    div("resource") {
        style = "background-color: #${resource.color}"
        div("resource-inner") {
            a(
                "https://starfieldwiki.net/wiki/Starfield:${resource.readableName.replace(" ", "_")}",
                target = "_blank"
            ) { +resource.name }
        }
        title = resource.readableName
    }
}

private fun TagConsumer<HTMLElement>.resourceSquare(
    resource: ResourceType,
    i: Int,
    checkBoxes: KProperty0<MutableSet<Int>>
) {
    div("resource") {
        id = "resource-${resource.name}"
        val color = if(checkBoxes.get().contains(i)) "#" +resource.color else "var(--menu-gray)"

        style = "background-color: $color"
        div("resource-hover-check") {
            checkBox(i, checkBoxes) {
                el("resource-${resource.name}").style.backgroundColor = if(it) "#" +resource.color else "var(--menu-gray)"
                persistMemory()
            }
        }
        div("resource-inner") {
            a(
                "https://starfieldwiki.net/wiki/Starfield:${resource.readableName.replace(" ", "_")}",
                target = "_blank"
            ) { +resource.name }
        }
        title = resource.readableName
    }
}


fun showResourcePicker(resources: Set<ResourceType>, result: (ResourceType) -> Unit) {
    val root = el("popup")
    root.removeClass("hidden")
    root.style.top = "${mouseY}px"
    root.style.left = "${mouseX}px"
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