import kotlinx.serialization.Serializable

@Serializable
data class PlanetInfo(
    val labels: MutableSet<Label> = mutableSetOf(),
    var notes: String = "",
    val outPosts: MutableSet<String> = mutableSetOf()
)