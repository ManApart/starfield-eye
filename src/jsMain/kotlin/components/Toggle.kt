package components

import el
import kotlinx.html.*
import kotlinx.html.js.onChangeFunction
import org.w3c.dom.HTMLElement
import org.w3c.dom.HTMLInputElement
import kotlin.reflect.KMutableProperty0

fun TagConsumer<HTMLElement>.toggle(property: KMutableProperty0<Boolean?>, onChange: (Boolean?) -> Unit = {}) {
    div("toggle-switch") {
        input(InputType.checkBox) {
            id = "toggle-switch-${property.name}"
            checked = property.get() ?: false
            onChangeFunction = {
                val newVal = el<HTMLInputElement>(this.id).checked
                property.set(newVal)
                onChange(newVal)

            }
        }
        label("toggle-slider" ) {
            htmlFor = "toggle-switch-${property.name}"
            +"Toggle"
        }
    }
}