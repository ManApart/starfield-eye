package docking

class RawPollResponse(private val commandMap: Map<String, List<String>>) {
    fun getQuests(): List<String> {
        return commandMap["sqo"] ?: listOf()
    }

    fun getString(command: String): String {
        return commandMap[command]?.first() ?: ""
    }

    fun getInt(command: String): Int {
        val raw = commandMap[command]?.first()
        return raw?.split(" ")?.last()?.toFloatOrNull()?.toInt() ?: 0
//            .also { println("$command returned \"$raw\"") }
    }

    fun hasMiscStats(): Boolean {
        return listOf(
            "most credits carried",
            "locations discovered",
            "days passed",
            "credits found",
        ).any { getMiscStatInt(it) != 0 }
    }

    fun getMiscStatString(statName: String): String {
        return getString("getpcmiscstat \"$statName\"")
    }

    fun getMiscStatInt(statName: String): Int {
        return getInt("getpcmiscstat \"$statName\"")
    }
}