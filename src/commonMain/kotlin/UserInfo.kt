import kotlinx.serialization.Serializable

@Serializable
data class PlanetInfo(
    val labels: MutableSet<Label> = mutableSetOf(),
    var notes: String = "",
    val outPosts: MutableSet<String> = mutableSetOf()
)

@Serializable
data class PlanetScan(
    var traits: Set<Int>? = null,
    var resources: Set<Int>? = null,
    var lifeScans: Map<ScanName, PercentScanned>
)

typealias ScanName = String
typealias PercentScanned = String