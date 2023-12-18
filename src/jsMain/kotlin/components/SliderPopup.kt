package components

import el
import kotlinx.dom.removeClass
import kotlinx.html.*
import kotlinx.html.dom.append
import kotlinx.html.js.onChangeFunction
import mouseX
import mouseY
import org.w3c.dom.HTMLInputElement

fun sliderPopup(steps: IntRange, current: Int, onChange: (Int) -> Unit){
    val root = el("popup")
    root.removeClass("hidden")
    root.style.top = "${mouseY}px"
    root.style.left = "${mouseX}px"
    root.innerHTML = ""
    root.append {
        dataList {
            id = "ticks"
            steps.forEach { option { value = "$it" } }
        }
        input(type = InputType.range) {
            id = "slider-popup"
            list = "ticks"
            min = steps.first.toString()
            max = steps.last.toString()
            value = current.toString()
            step = "1"
            onChangeFunction = {
                val newValue =
                    el<HTMLInputElement>("slider-popup").value.toIntOrNull()
                        ?: 0
                onChange(newValue)
            }
        }
    }
    popupClickListener()


}