import kotlinx.html.*
import kotlin.random.Random

private val rand = Random(0)
fun DIV.backgroundStars() {
    div("stars") {
        (0..2).forEach { layer ->
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
                        style = "top: ${y}%; left: ${x}vw; width: ${size}px; height: ${size}px; background-color: $color"
                    }
                }
            }
        }
    }
}