package views

import el
import isMobile
import kotlinx.html.*
import kotlinx.html.dom.append
import org.w3c.dom.HTMLElement
import kotlin.random.Random

private val rand = Random(0)
private const val layerCount = 3
private var starLayers = listOf<HTMLElement>()

fun genStars() {
    if (!isMobile()) {
        el("stars").append {
            backgroundStars()
        }
        starLayers = (0 until layerCount).mapNotNull { el<HTMLElement?>("star-layer-$it") }
    }
}

private fun TagConsumer<HTMLElement>.backgroundStars() {
    div("stars") {
        (0 until layerCount).forEach { layer ->
            div("star-layer") {
                id = "star-layer-$layer"
                (0..20).forEach {
                    div("star") {
                        val color = when {
                            it % 6 == 0 -> "var(--light-background)"
                            it % 7 == 0 -> "var(--highlight)"
                            else -> "white"
                        }

                        val x = (rand.nextDouble() * 100).toInt()
                        val maxHeight = (layer * 33) + 33
                        val y = (rand.nextDouble() * maxHeight).toInt()
                        val size = 2 + (rand.nextDouble() * (4 * layer)).toInt()
                        style =
                            "top: ${y}%; left: ${x}vw; width: ${size}px; height: ${size}px; background-color: $color"
                    }
                }
            }
        }
    }
}

fun panStars(x: Float, y: Float) {
    val layerOffset = 5
    starLayers.forEachIndexed { i, layer ->
        val offsetX = (1 - x) * (1 + i * layerOffset)
        val offsetY = (1 - y) * (1 + i * layerOffset)
        layer.style.top = "$offsetY%"
        layer.style.left = "$offsetX%"
    }

}