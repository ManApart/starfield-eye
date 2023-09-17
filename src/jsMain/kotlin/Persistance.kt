import LocalForage.config
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import org.w3c.dom.HTMLElement
import org.w3c.xhr.JSON
import org.w3c.xhr.XMLHttpRequest
import org.w3c.xhr.XMLHttpRequestResponseType
import kotlin.js.Promise
import kotlin.js.Promise.Companion.resolve

val jsonMapper = kotlinx.serialization.json.Json { ignoreUnknownKeys = true }

@Serializable
data class InMemoryStorage(
    val planetSearchOptions: PlanetSearchOptions = PlanetSearchOptions(),
    val planetUserInfo: MutableMap<String, PlanetInfo> = mutableMapOf()
)

var inMemoryStorage = InMemoryStorage()
var starDivs: Map<String, HTMLElement> = mapOf()
var planetDivs: Map<String, HTMLElement> = mapOf()



fun loadAll(): Promise<*>{
    return loadMemory().then {
        if (galaxy.systems.isEmpty()){
            return@then loadGalaxy()
        } else resolve("Loaded")
    }
}

fun loadGalaxy(): Promise<*> {
    return loadJson("data.json").then { json ->
        galaxy = jsonMapper.decodeFromString(json)
        println("Read ${galaxy.systems.keys.size} systems from json")
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
            inMemoryStorage = jsonMapper.decodeFromString(persisted as String)
        }
    }
}
