package views

import el
import exportPlayerInfo
import docking.getQuests
import docking.healthCheck
import docking.poll
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
import org.w3c.dom.HTMLDivElement
import persistMemory
import pollHook

fun dockView() {
    window.history.pushState(null, "null", "#dock")
    val root = el("root")
    root.innerHTML = ""
    root.append {
        div {
            id = "dock-view"
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
                connectToGame()
                manageData()
            }
        }
    }
    pollHook = ::receivePoll
}

private fun DIV.connectToGame() {
    div("section-view-box") {
        id = "connect-to-game"
        h2 { +"Dock" }
        div("accent-line") { +"You two will be foot to foot" }

        p { +"Unlock additional functionality by \"docking\" this site to your game. Data gleaned from the game will persist in the site even after the game is ended, and can be exported/imported into other browsers." }

        p { +"Note that docking functionality is beta and under heavy construction. Things that require docking will likely be broken and often not yet be styled. Proceed at your own risk. " }

        p {
            +"Docking requires that you have"
            a(
                "https://www.nexusmods.com/starfield/mods/4280",
                target = "_blank"
            ) { +" Console API and Web Application " }
            +"installed and working."
        }
        p { +"Once installed, enter your host and port and attempt to dock. The docking status should update after you attempt to dock." }

        input(classes = "connection-input") {
            id = "dock-host"
            placeholder = "Host (default: \"localhost\")"
            value = inMemoryStorage.connectionSettings.host
            title = "Host to connect to. Should match the settings in your Console Api Ini. Almost always either localhost or 127.0.0.1"
        }
        input(classes = "connection-input") {
            id = "dock-port"
            placeholder = "Port (default: \"55555\")"
            title = "Port to connect to. Should match the settings in your Console Api Ini"
            value = inMemoryStorage.connectionSettings.port
        }
        input(classes = "connection-input") {
            id = "dock-poll-rate"
            placeholder = "Poll rate (default: \"10\" seconds)"
            title = "How often to check the game for more data. Lower is more responsive but may affect game performance. 0 Prevents polling but still checks on page load."
            value = inMemoryStorage.connectionSettings.pollRateInSeconds.toString()
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
            onClickFunction = {
                inMemoryStorage.connectionSettings.pollData = false
                setStatusDiv("Status: Undocked")
            }
        }
    }
}

private fun DIV.manageData() {
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
}

private fun attemptConnection() {
    with(inMemoryStorage.connectionSettings) {
        host = el<HTMLInputElement>("dock-host").value
        port = el<HTMLInputElement>("dock-port").value
        pollRateInSeconds = el<HTMLInputElement>("dock-poll-rate").value.toIntOrNull() ?: 10
        pollData = true
    }
    persistMemory()
    setStatusDiv("Status: Docking")
    CoroutineScope(Dispatchers.Default).launch {
        if (healthCheck()) {
            setStatusDiv("Status: Docked")
            pollData()
        } else {
            setStatusDiv("Status: Docking Aborted. Please check console and follow installation instructions.")
        }
    }
}

fun pollData() {
    if (pageIsVisible && inMemoryStorage.connectionSettings.pollData) {
        CoroutineScope(Dispatchers.Default).launch {
            if (inMemoryStorage.connectionSettings.pollRateInSeconds != 0) {
                window.setTimeout({
                    pollData()
                }, inMemoryStorage.connectionSettings.pollRateInSeconds * 1000)
            }
            try {
                val data = poll()
                if (data.quests.isNotEmpty()) inMemoryStorage.quests = data.quests
                persistMemory()
                pollHook(true)
            } catch (e: Error) {
                pollHook(false)
            }
        }
    }
}

private fun receivePoll(success: Boolean) {
    if (success) {
        setStatusDiv("Status: Docked")
    } else {
        setStatusDiv("Status: Docking Aborted. Please check console and follow installation instructions.")
    }
}

private fun setStatusDiv(message: String) {
    el<HTMLDivElement?>("dock-status")?.textContent = message
}