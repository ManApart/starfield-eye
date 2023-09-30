package views

import el
import exportPlayerInfo
import healthCheck
import importPlayerInfo
import inMemoryStorage
import kotlinx.browser.window
import kotlinx.html.*
import kotlinx.html.dom.append
import kotlinx.html.js.onClickFunction
import org.w3c.dom.HTMLInputElement
import pageIsVisible
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

const val pollRateSeconds = 1

fun dockView() {
    window.history.pushState(null, "null", "#dock")
    val root = el("root")
    root.innerHTML = ""
    root.append {
        div {
            id = "dock-view"
            backgroundStars()
            div {
                id = "dock-nav"
                button {
                    +"Back to Galaxy"
                    onClickFunction = {
                        renderGalaxy()
                    }
                }
            }
            div {
                id = "sections"
                div("section-view-box") {
                    id = "manage-data"
                    h2 { +"Manage Data" }
                    div("accent-line") { +"The mind's long con: forgetfulness" }

                    p { +"User data is only stored on the browser. (I don't track anything you do)." }
                    p { +"You can export data here to either back it up, or import it into another browser." }

                    button {
                        id = "import-button"
                        +"Import Data"
                        title = "Import saved user data"
                        onClickFunction = { importPlayerInfo() }
                    }
                    button {
                        id = "export-button"
                        +"Export Data"
                        title = "Download user entered data"
                        onClickFunction = { exportPlayerInfo() }
                    }
                }
                div("section-view-box") {
                    id = "connect-to-game"
                    h2 { +"Dock" }
                    div("accent-line") { +"You two will be foot to foot" }

                    p { +"Unlock additional functionality by \"docking\" this site to your game. " }

                    input {
                        id = "dock-host"
                        placeholder = "Host to connect to (likely \"localhost\")"
                        value = inMemoryStorage.connectionSettings.host
                    }
                    input {
                        id = "dock-port"
                        placeholder = "Port to connect to (defaults to \"55555\")"
                        value = inMemoryStorage.connectionSettings.port
                    }

                    div {
                        id = "dock-status"
                        +"Status: Undocked"
                    }

                    button {
                        id = "dock-button"
                        +"Dock"
                        title = "Connect to the game"
                        onClickFunction = { attemptConnection() }
                    }
                    button {
                        id = "undock-button"
                        +"Undock"
                        title = "Stop polling the game"
                        onClickFunction = { inMemoryStorage.connectionSettings.pollData = false }
                    }
                }
            }
        }
    }
    readyStars()
}

fun attemptConnection() {
    with(inMemoryStorage.connectionSettings) {
        host = el<HTMLInputElement>("dock-host").value
        port = el<HTMLInputElement>("dock-port").value
        pollData = true
    }
    if (healthCheck()){
        el("dock-status").textContent = "Status: Docked"
        pollData()
    } else {
        el("dock-status").textContent = "Status: Docking Unsuccessful. Please check console and follow installation instructions."
    }
}

fun pollData() {
    if(pageIsVisible && inMemoryStorage.connectionSettings.pollData){
        CoroutineScope(Dispatchers.Default).launch {
            window.setTimeout({
                pollData()
            }, pollRateSeconds*1000)
            println("Polling data")
            try {

            } catch (e: Exception){
                el("dock-status").textContent = "Status: Docking Unsuccessful. Please check console and follow installation instructions."
            }
        }
    }
}
