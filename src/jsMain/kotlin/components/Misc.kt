package components

import kotlinx.browser.window
import kotlinx.html.TagConsumer
import kotlinx.html.id
import kotlinx.html.js.a
import kotlinx.html.js.h2
import org.w3c.dom.HTMLElement
import kotlin.math.max

fun TagConsumer<HTMLElement>.wikiLink(page: String) {
    a("https://starfieldwiki.net/wiki/Starfield:$page", target = "_blank", classes = "a-button") {
        +"Wiki"
    }
}

fun TagConsumer<HTMLElement>.linkableH2(text: String) {
    val page = window.location.href.let {url ->
        val start = url.indexOf("#") + 1
        val end = url.indexOf( "/", start).takeIf { it > 0 } ?: url.length
        url.substring(start, end)
    }
    h2 {
        val idText = text.replace(" ", "_").lowercase()
        id = idText
        a("#$page/$idText") { +text }
    }
}