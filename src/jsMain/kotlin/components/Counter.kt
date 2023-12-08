package components

import el
import kotlinx.html.*
import kotlinx.html.js.onChangeFunction
import org.w3c.dom.HTMLElement
import org.w3c.dom.HTMLInputElement
import kotlin.reflect.KMutableProperty0

fun TagConsumer<HTMLElement>.counter(property: KMutableProperty0<Int>, onChange: (Int) -> Unit = {}) {
    input(InputType.number, classes = "counter") {
        id = "${property.name}-counter"
        value = property.get().toString()
        onChangeFunction = {
            val newVal = el<HTMLInputElement>(this.id).valueAsNumber.toInt()
            property.set(newVal)
            onChange(newVal)
        }
    }
}
fun TagConsumer<HTMLElement>.counter(name: String, getValue: () -> Int, onChange: (Int) -> Unit = {}) {
    input(InputType.number, classes = "counter") {
        id = "${name}-counter"
        value = getValue().toString()
        onChangeFunction = {
            val newVal = el<HTMLInputElement>(this.id).valueAsNumber.toInt()
            onChange(newVal)
        }
    }
}