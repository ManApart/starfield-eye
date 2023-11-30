import kotlinx.serialization.Serializable

@Serializable
data class PlanetInfo(
    val planetId: String,
    val labels: MutableSet<Label> = mutableSetOf(),
    var notes: String = "",
    val outPosts: MutableList<Outpost> = mutableListOf()
) {
    fun addOutpost(name: String) {
        val id = (outPosts.maxOfOrNull { it.id } ?: 0) + 1
        outPosts.add(Outpost(id, name))
    }
}

@Serializable
data class Outpost(
    val id: Int,
    var name: String = "",
    val organicResources: MutableSet<String> = mutableSetOf(),
    val inorganicResources: MutableSet<ResourceType> = mutableSetOf(),
    var notes: String = "",
)

@Serializable
data class PlanetScan(
    var traits: Set<Int>? = null,
    var resources: Set<Int>? = null,
    var lifeScans: Map<ScanName, PercentScanned>
)

typealias ScanName = String
typealias PercentScanned = String