import java.io.File

fun main() {
    File("./raw-data/inorganic-resources.csv").readLines().map {
        val parts = it.split(",")
        parts[3].replace("-", "") to parts[0]
    }
        .sortedBy { it.first }
        .joinToString("\n") { (id, name) ->
            "$id(\"$name\"),"
        }
        .let { println(it) }
}