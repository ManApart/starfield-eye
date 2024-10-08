package components

import ResourceType
import doRouting
import el
import inMemoryStorage
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

fun TagConsumer<HTMLElement>.resourceSquare(resource: ResourceType, additionalClass: String? = null) {
    val classList = if (additionalClass != null){
        "resource $additionalClass"
    } else "resource"
    div(classList) {
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
        val color = if(inMemoryStorage.showUndiscovered != false || checkBoxes.get().contains(i)) "#" +resource.color else "var(--menu-gray)"

        style = "background-color: $color"
        div("resource-hover-check") {
            checkBox(i, checkBoxes) {
                el("resource-${resource.name}").style.backgroundColor = if(it || inMemoryStorage.showUndiscovered != false) "#" +resource.color else "var(--menu-gray)"
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
    openPopup(root)
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
