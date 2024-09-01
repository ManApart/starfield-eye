package components

import kotlinx.html.TagConsumer
import kotlinx.html.a
import kotlinx.html.id
import kotlinx.html.img
import kotlinx.html.js.a
import kotlinx.html.js.h2
import org.w3c.dom.HTMLElement

fun TagConsumer<HTMLElement>.wikiLink(page: String) {
    a("https://starfieldwiki.net/wiki/Starfield:$page", target = "_blank", classes = "a-button") {
        +"Wiki"
    }
}

fun TagConsumer<HTMLElement>.linkableH2(page: String, text: String) {
    h2 {
        val idText = text.replace(" ", "_").lowercase()
        id = idText
        a("#$page/$idText") { +text }
    }
}

fun TagConsumer<HTMLElement>.externalLink(text: String, url: String) {
    a {
        href = url
        target = "_blank"
        +text
        img(classes = "link-pic") {
            src = "./images/link.svg"
        }
    }
}
