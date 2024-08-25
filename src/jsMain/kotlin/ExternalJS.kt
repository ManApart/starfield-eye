import kotlin.js.Json
import kotlin.js.Promise

@JsModule("localforage")
@JsNonModule
external object LocalForage {
    fun setItem(key: String, value: Any): Promise<*>
    fun getItem(key: String): Promise<Any?>
    fun config(config: LocalForageConfig)
}

data class LocalForageConfig(val name: String)

@JsModule("vis-network")
@JsNonModule
@JsName("vis")
external object Vis {
    class Network {
        fun on(event: String, handler: (Json) -> Unit)
    }
}

@JsModule("vis-data")
@JsNonModule
@JsName("vis")
external object VisData
