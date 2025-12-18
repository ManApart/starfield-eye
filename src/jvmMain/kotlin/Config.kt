import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import java.io.File

@Serializable
data class Config(val botCreds: BotCreds)

@Serializable
data class BotCreds(val name: String, val pass: String, val cookie: String)

fun readConfig() = File("./config.json").readText().let { jsonMapper.decodeFromString<Config>(it) }
