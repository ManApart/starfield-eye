
import LocalForage.config
import kotlinx.browser.document
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import org.w3c.dom.HTMLElement
import org.w3c.dom.HTMLInputElement
import org.w3c.dom.events.Event
import org.w3c.dom.events.MouseEvent
import org.w3c.files.FileReader
import org.w3c.files.get
import org.w3c.xhr.JSON
import org.w3c.xhr.XMLHttpRequest
import org.w3c.xhr.XMLHttpRequestResponseType
import kotlin.js.Promise

val jsonMapper = kotlinx.serialization.json.Json { ignoreUnknownKeys = true }

@Serializable
data class InMemoryStorage(
    val planetSearchOptions: PlanetSearchOptions = PlanetSearchOptions(),
    val planetUserInfo: MutableMap<String, PlanetInfo> = mutableMapOf(),
    val connectionSettings: GameConnectionSettings = GameConnectionSettings(),
    var quests: List<Quest> = listOf(),
    var stats: MiscStats = MiscStats(),
)

var inMemoryStorage = InMemoryStorage()
var starDivs: Map<String, HTMLElement> = mapOf()
var planetDivs: Map<String, HTMLElement> = mapOf()


fun loadAll(): Promise<*> {
    return loadMemory().then {
        loadGalaxy().then {
            loadMissionReference().then {
                return@then
            }
        }
    }
}

fun loadGalaxy(): Promise<*> {
    return loadJson("data.json").then { json ->
        galaxy = jsonMapper.decodeFromString(json)
        println("Read ${galaxy.systems.keys.size} systems from json")
    }
}
fun loadMissionReference(): Promise<*> {
    return loadJson("mission-wiki-data.json").then { json ->
        missionReference = jsonMapper.decodeFromString<List<MissionWikiData>>(json).associateBy { it.name }
        println("Read ${missionReference.keys.size} missions from json")
    }
}

private fun loadJson(url: String): Promise<String> {
    return Promise { resolve, _ ->
        XMLHttpRequest().apply {
            open("GET", url)
            responseType = XMLHttpRequestResponseType.JSON
            onerror = { println("Failed to get Json") }
            onload = {
                resolve(JSON.stringify(response))
            }
            send()
        }
    }
}


fun createDB() {
    config(LocalForageConfig("starfield-eye"))
}

fun persistMemory() {
    LocalForage.setItem("memory", jsonMapper.encodeToString(inMemoryStorage))
}

fun loadMemory(): Promise<*> {
    return LocalForage.getItem("memory").then { persisted ->
        if (persisted != null && persisted != undefined) {
            try {
                inMemoryStorage = jsonMapper.decodeFromString(persisted as String)
            } catch (e: Exception) {
                println("Failed to parse memory. Try deleting user data and rebuilding it.")
            }
        }
    }
}

fun exportPlayerInfo() {
    val download = document.createElement("a") as HTMLElement
    download.setAttribute("href", "data:text/plain;charset=utf-8," + jsonMapper.encodeToString(inMemoryStorage))
    download.setAttribute("download", "StarfieldEye.json")
    document.body?.append(download)
    download.click()
    document.body?.removeChild(download)
}

fun importPlayerInfo() {
    val fileInput = document.createElement("input") as HTMLInputElement
    fileInput.apply {
        type = "file"
        accept = "*.json"
        addEventListener("change", { e: Event ->
            if (files != null) {
                val file = files!![0]!!
                val reader = FileReader()
                reader.onloadend = {
                    jsonMapper.decodeFromString<InMemoryStorage>(reader.result as String).also { inMemoryStorage = it }
                    println("Imported ${inMemoryStorage.planetUserInfo.size} user info pieces")
                }
                reader.readAsText(file)
            }
        })
        dispatchEvent(MouseEvent("click"))
    }
}