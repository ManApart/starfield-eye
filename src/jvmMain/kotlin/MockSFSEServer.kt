import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.compression.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.plugins.cors.routing.*
import io.ktor.server.request.*

fun main() {
    val usedPort = System.getenv("PORT")?.toInt() ?: 55555
    embeddedServer(Netty, port = usedPort) {
        install(ContentNegotiation) {
            json()
        }
        install(CORS) {
            allowMethod(HttpMethod.Get)
            allowMethod(HttpMethod.Post)
            allowMethod(HttpMethod.Delete)
            anyHost()
        }
        install(Compression) {
            gzip()
        }
        routing {
            post("/console") {
                call.respondText(mockResponse(call.receiveText()))
            }
        }
    }.start(wait = true)
}

private fun mockResponse(command: String): String {
//    println("Command: $command")
    return when (command) {
        "GetSFSEVersion" -> """
            |GetSFSEVersion
            |Command: GetSFSEVersion
            |SFSE version: 0.1.3, release idx 4, runtime 01070210
        """.trimMargin()
        "GetPlayerHomeSpaceShip" -> """
            |GetPlayerHomeSpaceShip
            |Command: GetPlayerHomeSpaceShip
            |Player Home Ship >> 0.00
        """.trimMargin()
        "sqo" -> """
            |sqo
            |== One Small Step ==
            |( Instance: 1 )
            |5 Follow Supervisor Lin COMPLETED
            |10 Get the Cutter COMPLETED
            |20 Collect Mineral Deposits COMPLETED
            |25 Return to Supervisor Lin COMPLETED
            |30 Follow Supervisor Lin COMPLETED
            |40 Explore the Cavern COMPLETED
            |42 Break Up the Deposits COMPLETED
            |43 Take the Strange Object COMPLETED
            |45 Talk to Heller and Lin COMPLETED
            |50 Follow Lin COMPLETED
            |60 Equip a Helmet COMPLETED
            | 
            |== Companion - Barrett ==
            |( Instance: 1 )
            |900 Talk with Barrett DISPLAYED
 
        """.trimMargin()
        else -> command
    }
}