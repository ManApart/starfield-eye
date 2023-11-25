package components

import el
import kotlinx.browser.document
import kotlinx.dom.addClass
import kotlinx.dom.removeClass
import kotlinx.html.*
import kotlinx.html.js.onClickFunction
import org.w3c.dom.HTMLElement
import org.w3c.dom.HTMLImageElement
import org.w3c.dom.HTMLInputElement
import org.w3c.dom.events.Event
import org.w3c.dom.events.MouseEvent
import org.w3c.files.FileReader
import org.w3c.files.get
import pictureStorage
import savePicture

fun TagConsumer<HTMLElement>.screenshot(key: String, fallbackUrl: String? = null) {
    div("screenshot") {
        val imageUrl = (pictureStorage[key] ?: fallbackUrl)
        val hidden = if (imageUrl == null) "hidden" else ""
        img(classes = hidden) {
            id = key
            alt = ""
            imageUrl?.let { src = it }

            onClickFunction = {
                uploadPicture(key)
            }
        }
        if (imageUrl == null) {
            button {
                id = "$key-button"
                +"Upload Picture"
                onClickFunction = {
                    uploadPicture(key)
                }
            }
        }
    }
}

private fun uploadPicture(key: String) {
    val img = el<HTMLImageElement>(key)
    val fileInput = document.createElement("input") as HTMLInputElement
    fileInput.apply {
        id = "picture-upload-input"
        type = "file"
        accept = "image/png, image/jpeg"
        addEventListener("change", { _: Event ->
            if (files != null && (files?.length ?: 0) > 0) {
                val file = files!![0]!!
                val reader = FileReader()
                reader.onload = {
                    val pic = reader.result as String
                    img.src = pic
                    img.removeClass("hidden")
                    savePicture(key, pic)
                    if (img.src.isNotBlank()) {
                        val button = el("$key-button")
                        button.addClass("hidden")
                    }
                    true
                }
                reader.readAsDataURL(file)
            }
        })
        dispatchEvent(MouseEvent("click"))
    }
}