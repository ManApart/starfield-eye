package components

import kotlinx.html.TagConsumer
import kotlinx.html.js.a
import org.w3c.dom.HTMLElement

fun TagConsumer<HTMLElement>.wikiLink(page: String) {
    a("https://starfieldwiki.net/wiki/Starfield:$page", target = "_blank", classes = "a-button") {
        +"Wiki"
    }
}