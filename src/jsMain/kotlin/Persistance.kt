import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.decodeFromJsonElement
import org.w3c.xhr.JSON
import org.w3c.xhr.XMLHttpRequest
import org.w3c.xhr.XMLHttpRequestResponseType
import kotlin.js.Promise

val jsonMapper = kotlinx.serialization.json.Json { ignoreUnknownKeys = true }

@Serializable
data class InMemoryStorage(
    var systems: Map<String, StarSystem> = mapOf()
)

var inMemoryStorage = InMemoryStorage()

fun loadAll(): Promise<*>{
    return loadMemory().then {
        if (inMemoryStorage.systems.isEmpty()){
            loadGalaxy()
        }
    }
}


fun loadMemory(): Promise<*> {
    return LocalForage.getItem("memory").then { persisted ->
        if (persisted != null && persisted != undefined) {
            inMemoryStorage.systems = jsonMapper.decodeFromString(persisted as String)
            println("Read ${inMemoryStorage.systems.keys.size} systems from storage")
        }
    }
}


fun loadGalaxy(): Promise<*> {
    return loadJson("data.json").then { json ->
        inMemoryStorage.systems = jsonMapper.decodeFromString(json)
        println("Read ${inMemoryStorage.systems.keys.size} systems from json")
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