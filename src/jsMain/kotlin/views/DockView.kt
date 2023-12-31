package views

import components.linkableH2
import components.toggle
import deleteUserData
import docking.healthCheck
import docking.poll
import el
import exportPictures
import exportPlayerInfo
import importPictures
import importPlayerInfo
import inMemoryStorage
import keyPressedHook
import kotlinx.browser.window
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.html.*
import kotlinx.html.js.onClickFunction
import loadSampleData
import missionReference
import org.w3c.dom.HTMLDivElement
import org.w3c.dom.HTMLElement
import org.w3c.dom.HTMLInputElement
import org.w3c.dom.events.KeyboardEvent
import pageIsVisible
import persistMemory
import pollHook
import replaceElement
import updateUrl

fun dockView(section: String? = null) {
    updateUrl("dock", section)
    replaceElement {
        div {
            id = "dock-view"
            navButtons()
            div {
                id = "sections"
                connectToGame()
                manageData()
                settings()
                prepareToDock()
            }
        }
    }
    pollHook = ::receivePoll
    keyPressedHook = ::submitForDocking
    if (inMemoryStorage.connectionSettings.pollData) pollData()
}

private fun DIV.connectToGame() {
    div("section-view-box") {
        id = "connect-to-game"
        h2 { +"Dock" }
        div("accent-line") { +"You two will be foot to foot" }

        p { +"Unlock additional functionality by \"docking\" this site to your game. Data gleaned from the game will persist in the site even after the game is ended, and can be exported/imported into other browsers." }

        p { +"Note that docking functionality is beta and under heavy construction. Things that require docking will likely be broken and often not yet be styled. Proceed at your own risk. " }

        p {
            +"Docking setup is fairly complicated. See the "
            a(href = "#dock/docking-install") { +"instructions below." }
        }

        p { +"Once installed, enter your host and port and attempt to dock. The docking status should update after you attempt to dock." }

        input(classes = "connection-input") {
            id = "dock-host"
            placeholder = "Host (default: \"localhost\")"
            value = inMemoryStorage.connectionSettings.host
            title =
                "Host to connect to. Should match the settings in your Console Api Ini. Almost always either localhost or 127.0.0.1"
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
            title =
                "How often to check the game for more data. Lower is more responsive but may affect game performance. 0 Prevents polling but still checks on page load."
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

private fun TagConsumer<HTMLElement>.manageData() {
    div("section-view-box") {
        id = "manage-data"
        linkableH2("Manage Data")
        div("accent-line") { +"The mind's long con: forgetfulness" }

        p { +"User data is only stored on the browser. (I don't track anything you do)." }
        p { +"You can export data here to either back it up, or import it into another browser, or from the github site to the locally hosted one." }
        p { +"I recommend using the thumbnail image when uploading screenshots from photo mode. While you can upload any size you want, the backup will be much smaller if you use the thumbnails, and they're generally still as large as the displayed image on the site." }
        p { +"If you're having issues with the site, it's possible that deleting your user data may fix them. Make sure to export your data first so you have a backup." }

        div {
            button {
                +"Load Sample Data"
                onClickFunction = { loadSampleData(el("load-status")) }
            }
        }
        div {
            button {
                id = "import-button"
                +"Import Data"
                title = "Import saved user data"
                onClickFunction = { importPlayerInfo(el("load-status")) }
            }
            button {
                id = "export-button"
                +"Export Data"
                title = "Download user entered data"
                onClickFunction = { exportPlayerInfo() }
            }
            button {
                id = "delete-button"
                +"Delete Data"
                title = "Delete user entered data"
                onClickFunction = { deleteUserData() }
            }
        }
        div {
            button {
                id = "import-images-button"
                +"Import Images"
                title = "Import saved user images"
                onClickFunction = { importPictures(el("load-status")) }
            }
            button {
                id = "export-images-button"
                +"Export Images"
                title = "Download user uploaded images"
                onClickFunction = { exportPictures() }
            }
        }
        p {
            id = "load-status"
        }
    }
}

private fun TagConsumer<HTMLElement>.settings() {
    div("section-view-box") {
        id = "settings"
        linkableH2("Change Settings")
        div("accent-line") { +"Do my ears play a false tune?" }

        p { +"Manage site wide settings." }
        p { +"Showing stars defaults to on in desktop and off in mobile in order to provide a more responsive experience." }

        div("toggle-wrapper") {
            +"Show Stars"
            title = "Force background stars on or off"
            toggle(inMemoryStorage::paintBackgroundStars) {
                genStars()
                persistMemory()
            }
        }
        button {
            id = "reset-stars"
            +"Reset Stars"
            title = "Show stars depending on if device is mobile"
            onClickFunction = {
                inMemoryStorage.paintBackgroundStars = null
                genStars()
                persistMemory()
            }
        }
    }
}

private fun TagConsumer<HTMLElement>.prepareToDock() {
    div("section-view-box") {
        id = "docking-install"
        linkableH2("Preparing to Dock")
        div("accent-line") { +"Burning helium out there" }

        h3 { +"Installing Mods" }
        p { +"Docking requires that you have two mods installed. First, you need to have Console API and Web Application so we can get data from the game. Second, you need to install my companion app mod that polls data from the game. (Once things are more polished I'll release it on Nexus, but for now or to get bleeding edge changes, just grab it from github in the link below. The mod will likely not be released until after modding tools are available.)" }
        p { +"If you'll only access the site from the same computer that the game is running on, you can skip self hosting the site. If you'd like to dock your phone as well, you'll need to self host this site." }

        h4 { +"Installing" }
        ul {
            li {
                a(
                    "https://www.nexusmods.com/starfield/mods/106",
                    target = "_blank"
                ) { +"Install SFSE" }
            }
            li {
                a(
                    "https://www.nexusmods.com/starfield/mods/4280",
                    target = "_blank"
                ) { +"Install Console API and Web Application" }
                ul {
                    li {
                        +"Open "
                        code { +"sfse_plugin_console_api.ini " }
                    }
                    li {
                        +"Set "
                        code { +"bDisableCORS=true" }
                    }
                    li {
                        +"OPTIONAL: If self hosting the app"
                        ul {
                            li {
                                code { +"bEnableWebConsole=true" }
                            }
                            li {
                                code { +"bDisableStaticFiles=false" }
                            }
                            li {
                                +"Set your host to your local IP address; something like: "
                                code { +"host=192.168.0.100" }
                            }
                        }
                    }
                }
            }
            li {
                a(
                    "https://github.com/ManApart/starfield-eye/blob/master/mod/Data/starfield-eye-poll.txt",
                    target = "_blank"
                ) { +"Download the Companion App bat 'starfield-eye-poll.txt'" }
            }
            li { +"Place 'starfield-eye-poll.txt' in your Data folder" }
        }
        h4 { +"Self Hosting (Optional)" }
        p { +"Host the site from the companion app mod. Only needed for things like your phone to get live access." }
        ul {
            li {
                +"Download the latest version of the mod (starfield-eye.zip) from the "
                a("https://github.com/ManApart/starfield-eye/releases", target = "_blank") { +"Releases Page" }
            }
            li { +"Install and enable the mod. Make sure it's loaded after than Console API and Web Application." }
            li {
                +"Access the site from the local url and port, something like: "
                code { +"192.168.0.100:55555/starfield-eye.html" }
            }
            li {
                +"Dock using the IP and port in "
                code { +"sfse_plugin_console_api.ini " }
            }
            li {
                +"Note:"
                ul {
                    li { +"The local site will only be up when the game is running" }
                    li { +"Data fetched while the site is up should persist once you close the game" }
                    li { +"You can also import and export data to share across browsers without self hosting the site." }

                }
            }
        }

    }

}

private fun submitForDocking(event: KeyboardEvent) {
    if (event.key == "Enter") attemptConnection()
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
                val data = poll(missionReference)
                if (data.quests.isNotEmpty()) inMemoryStorage.quests = data.quests
                data.stats?.let { inMemoryStorage.stats = it }
                persistMemory()
                pollHook(data.success)
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


private fun TagConsumer<HTMLElement>.linkableH2(text: String) {
    linkableH2("dock", text)
}