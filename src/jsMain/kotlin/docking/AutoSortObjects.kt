package docking

import kotlinx.serialization.Serializable

private typealias Id = String

enum class ChestType(val id: Int) { KEYWORD(0), EXACT(1), DRAIN(2) }

@Serializable
data class Autosort(
    var sortQuestId: String? = null,
    var drainChests: List<DrainChest> = listOf(),
    var keywordChests: List<KeywordChest> = listOf(),
    var exactChests: List<ExactChest> = listOf(),
    var keywords: Map<Id, Keyword> = mapOf(),
)

@Serializable
data class DrainChest(val id: String, val name: String)

@Serializable
data class KeywordChest(val id: String, val name: String, val location: String, val keywordIds: MutableSet<String>)

@Serializable
data class ExactChest(val id: String, val name: String, val location: String)

@Serializable
data class Keyword(val id: String, val group: String, val name: String)
