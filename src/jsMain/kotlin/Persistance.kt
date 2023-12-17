import LocalForage.config
import kotlinx.browser.document
import kotlinx.browser.window
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
    val planetUserInfo: MutableMap<String, PlanetInfo> = mutableMapOf(),
    val discoveredStars: MutableSet<Int> = mutableSetOf(),
    val connectionSettings: GameConnectionSettings = GameConnectionSettings(),
    var quests: List<Quest> = listOf(),
    var stats: MiscStats = MiscStats(),
    var perks: MutableMap<String, Int> = mutableMapOf(),
    var showUndiscovered: Boolean? = true,
    var outpostResourceView: Boolean? = false,
    var paintBackgroundStars: Boolean? = null,
) {
    fun planetInfo(uniqueId: String) = planetUserInfo.getOrPut(uniqueId) { PlanetInfo(uniqueId) }

    fun isDiscovered(system: Int) =
        if (inMemoryStorage.showUndiscovered == true) true else inMemoryStorage.discoveredStars.contains(system)
}

val planetSearchOptions: PlanetSearchOptions = PlanetSearchOptions()
val lifeSignsSearchOptions: PlanetSearchOptions = PlanetSearchOptions()
val missionSearchOptions: MissionSearchOptions = MissionSearchOptions()
var inMemoryStorage = InMemoryStorage()
var starDivs: Map<String, HTMLElement> = mapOf()
var planetDivs: Map<String, HTMLElement> = mapOf()
var floraDivs: Map<String, HTMLElement> = mapOf()
var faunaDivs: Map<String, HTMLElement> = mapOf()
var pictureStorage: MutableMap<String, String> = mutableMapOf()


fun loadAll(progress: HTMLElement): Promise<*> {
    progress.innerText = "Loading User Info"
    return loadMemory().then {
        progress.innerText = "Loading Galaxy Data"
        loadGalaxy().then {
            progress.innerText = "Loading Missions"
            loadMissionReference().then {
                progress.innerText = "Loading Flora"
                loadFloraReference().then {
                    progress.innerText = "Loading Fauna"
                    loadFaunaReference().then {
                        progress.innerText = "Loading User Pictures"
                        loadPictures().then {
                            return@then
                        }
                    }
                }
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

fun loadFloraReference(): Promise<*> {
    return loadJson("flora-wiki-data.json").then { json ->
        floraReference = jsonMapper.decodeFromString<List<FloraWikiData>>(json)
            .filter { it.planetId != null }
            .groupBy { it.planetId!! }
        println("Read ${floraReference.keys.size} flora from json")
    }
}

fun loadFaunaReference(): Promise<*> {
    return loadJson("fauna-wiki-data.json").then { json ->
        faunaReference = jsonMapper.decodeFromString<List<FaunaWikiData>>(json)
            .filter { it.planetId != null }
            .groupBy { it.planetId!! }
        println("Read ${faunaReference.keys.size} fauna from json")
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

fun persistPictures() {
    LocalForage.setItem("pictures", jsonMapper.encodeToString(pictureStorage))
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

fun loadPictures(): Promise<*> {
    return LocalForage.getItem("pictures").then { persisted ->
        if (persisted != null && persisted != undefined) {
            try {
                pictureStorage = jsonMapper.decodeFromString(persisted as String)
            } catch (e: Exception) {
                println("Failed to parse pictures. Try deleting user data and rebuilding it.")
            }
        }
    }
}

fun exportPlayerInfo() = exportData(jsonMapper.encodeToString(inMemoryStorage), "StarfieldEye.json")

fun exportPictures() = exportData(jsonMapper.encodeToString(pictureStorage), "StarfieldPictures.json")

private fun exportData(data: String, fileName: String) {
    val download = document.createElement("a") as HTMLElement
    download.setAttribute("href", "data:text/plain;charset=utf-8,$data")
    download.setAttribute("download", fileName)
    document.body?.append(download)
    download.click()
    document.body?.removeChild(download)
}

fun importPlayerInfo(status: HTMLElement) {
    importData { data ->
        try {
            jsonMapper.decodeFromString<InMemoryStorage>(data).also { inMemoryStorage = it }
            println("Imported ${inMemoryStorage.planetUserInfo.size} user info pieces")
            persistMemory()
            status.innerText = "Player Info Imported"
        } catch (e: Exception) {
            println("Import failed")
            status.innerText = "Import failed"
        }
    }
}

fun importPictures(status: HTMLElement) {
    importData { data ->
        try {
            jsonMapper.decodeFromString<Map<String, String>>(data).also { pictureStorage = it.toMutableMap() }
            println("Imported ${pictureStorage.keys.size} pictures")
            persistPictures()
            status.innerText = "Pictures Imported"
        } catch (e: Exception) {
            println("Import failed")
            status.innerText = "Import failed"
        }
    }
}

private fun importData(then: (String) -> Unit) {
    val fileInput = document.createElement("input") as HTMLInputElement
    fileInput.apply {
        type = "file"
        accept = "*.json"
        addEventListener("change", { e: Event ->
            if (files != null) {
                val file = files!![0]!!
                val reader = FileReader()
                reader.onloadend = {
                    then(reader.result as String)
                }
                reader.readAsText(file)
            }
        })
        dispatchEvent(MouseEvent("click"))
    }
}

fun deleteUserData() {
    if (window.confirm("Are you sure you want to delete data? Make sure you've exported a backup first!")) {
        inMemoryStorage = InMemoryStorage()
        pictureStorage = mutableMapOf()
        persistMemory()
    }
}

fun savePicture(key: String, data: String) {
    pictureStorage[key] = data
    persistPictures()
}

fun deletePicture(key: String) {
    pictureStorage.remove(key)
    persistPictures()
}

fun loadSampleData(status: HTMLElement) {
    try {
        status.innerText = "Loading Player Info"
        loadJson("sample/StarfieldEye.json").then { playerJson ->
            jsonMapper.decodeFromString<InMemoryStorage>(playerJson).also { inMemoryStorage = it }
            println("Imported ${inMemoryStorage.planetUserInfo.size} user info pieces")
            status.innerText = "Loading Pictures"
            loadJson("sample/StarfieldPictures.json").then { pictureJson ->
                jsonMapper.decodeFromString<Map<String, String>>(pictureJson)
                    .also { pictureStorage = it.toMutableMap() }
                println("Imported ${pictureStorage.keys.size} pictures")
                persistMemory()
                persistPictures()
                status.innerText = "Loaded sample data!"
            }
        }
    } catch (e: Exception) {
        status.innerText = "Loading sample data failed"
    }

}