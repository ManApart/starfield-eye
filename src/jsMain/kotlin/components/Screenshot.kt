package components

import deletePicture
import el
import kotlinx.browser.document
import kotlinx.dom.addClass
import kotlinx.dom.removeClass
import kotlinx.html.*
import kotlinx.html.js.onClickFunction
import org.w3c.dom.*
import org.w3c.dom.events.Event
import org.w3c.dom.events.MouseEvent
import org.w3c.files.FileReader
import org.w3c.files.get
import pictureStorage
import savePicture

fun TagConsumer<HTMLElement>.screenshot(key: String, fallbackUrl: String? = null) {
    div("screenshot") {
        val imageUrl = (pictureStorage[key] ?: fallbackUrl)

//         val deleteClasses = "screenshot-delete-button hidden"
         val deleteClasses = "screenshot-delete-button" + (" hidden".takeIf { pictureStorage[key] == null } ?: "")
        button(classes = deleteClasses) {
            id = "$key-delete-button"
            +"Del"
            onClickFunction = {
                el<HTMLInputElement>(key).src = fallbackUrl ?: ""
                if (fallbackUrl == null) {
                    el<HTMLButtonElement?>("$key-button")?.removeClass("hidden")
                }
                el<HTMLButtonElement?>("$key-delete-button")?.addClass("hidden")
                deletePicture(key)
            }
        }
        img(classes = "hidden".takeIf { imageUrl == null }) {
            id = key
            alt = ""
            imageUrl?.let { src = it }
            onClickFunction = { uploadPicture(key) }
        }
        button(classes = "hidden".takeIf { imageUrl != null }) {
            id = "$key-button"
            +"Upload Picture"
            onClickFunction = {
                uploadPicture(key)
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
                    scaleImage(img, pic) {
                        img.removeClass("hidden")
                        savePicture(key, img.src)
                        if (img.src.isNotBlank()) {
                            el<HTMLButtonElement?>("$key-button")?.addClass("hidden")
                            el<HTMLButtonElement?>("$key-delete-button")?.removeClass("hidden")
                        }
                    }
                    null
                }
                reader.readAsDataURL(file)
            }
        })
        dispatchEvent(MouseEvent("click"))
    }
}

fun scaleImage(img: HTMLImageElement, data: String, then: () -> Unit) {
    val image = Image()
    image.src = data
    image.onload = {
        val can = (document.createElement("canvas") as HTMLCanvasElement).apply {
            width = img.naturalWidth
            height = img.naturalHeight
        }
        val ctx = can.getContext("2d") as CanvasRenderingContext2D
        ctx.drawImage(image, 0.0, 0.0, img.naturalWidth.toDouble(), img.naturalHeight.toDouble())
        img.src = ctx.canvas.toDataURL("image/jpeg", .85)
        then()
        null
    }
}