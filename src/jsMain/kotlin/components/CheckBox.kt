package components

import el
import inMemoryStorage
import kotlinx.html.*
import kotlinx.html.js.onChangeFunction
import org.w3c.dom.HTMLElement
import org.w3c.dom.HTMLInputElement
import kotlin.reflect.KMutableProperty0
import kotlin.reflect.KProperty0

fun TagConsumer<HTMLElement>.checkBox(name: String, getValue: () -> Boolean, onChange: (Boolean) -> Unit = {}) {
    input(InputType.checkBox, classes = "checkbox") {
        id = "${name}-checkbox"
        checked = getValue()
        onChangeFunction = {
            val newVal = el<HTMLInputElement>(this.id).checked
            onChange(newVal)
        }
    }
}

fun TagConsumer<HTMLElement>.checkBox(property: KMutableProperty0<Boolean>, onChange: (Boolean) -> Unit = {}) {
    input(InputType.checkBox, classes = "checkbox") {
        id = "${property.name}-checkbox"
        checked = property.get()
        onChangeFunction = {
            val newVal = el<HTMLInputElement>(this.id).checked
            property.set(newVal)
            onChange(newVal)
        }
    }
}

fun TagConsumer<HTMLElement>.checkBox(
    name: String,
    property: KMutableProperty0<Boolean>,
    onChange: (Boolean) -> Unit = {}
) {
    span("checkbox-wrapper") {
        checkBox(property, onChange)
        +name
    }
}

fun TagConsumer<HTMLElement>.checkBox(
    index: Int,
    property: KProperty0<MutableSet<Int>>,
    onChange: (Boolean) -> Unit = {}
) {
    input(InputType.checkBox, classes = "checkbox") {
        id = "${property.name}-checkbox"
        checked = property.get().contains(index)
        onChangeFunction = {
            val newVal = el<HTMLInputElement>(this.id).checked
            if (newVal) {
                property.get().add(index)
            } else {
                property.get().remove(index)
            }
            onChange(newVal)
        }
    }
}

fun TagConsumer<HTMLElement>.checkBox(
    index: Int,
    name: String,
    property: KProperty0<MutableSet<Int>>,
    onChange: (Boolean) -> Unit = {}
) {
    span("checkbox-wrapper") {
        checkBox(index, property, onChange)
        +name
    }
}