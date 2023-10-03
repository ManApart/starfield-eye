
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.compression.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.plugins.cors.routing.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

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
            |
        """.trimMargin()
        "GetPlayerHomeSpaceShip" -> """
            |GetPlayerHomeSpaceShip
            |Command: GetPlayerHomeSpaceShip
            |Player Home Ship >> 0.00
            |
        """.trimMargin()
        "bat starfield-eye-poll" -> exampleMockResponseData
        "sqo" -> """
        |sqo
        |Command: sqo
        |$questData
        """.trimMargin()
        else -> command
    }
}

private val questData = """
    == One Small Step ==
    ( Instance: 1 )
    5 Follow Supervisor Lin COMPLETED
    10 Get the Cutter COMPLETED
    20 Collect Mineral Deposits COMPLETED
    25 Return to Supervisor Lin COMPLETED
    30 Follow Supervisor Lin COMPLETED
    40 Explore the Cavern COMPLETED
    42 Break Up the Deposits COMPLETED
    43 Take the Strange Object COMPLETED
    45 Talk to Heller and Lin COMPLETED
    50 Follow Lin COMPLETED
    60 Equip a Helmet COMPLETED
     
    == Companion - Barrett ==
    ( Instance: 1 )
    900 Talk with Barrett DISPLAYED
     
    == One Small Step ==
    ( Instance: 1 )
    65 Meet with the Client COMPLETED
    70 Hold off the Pirates COMPLETED
    75 (Optional) Grab a Weapon COMPLETED
    80 Talk to Barrett COMPLETED
    85 Take the Watch COMPLETED
    90 Board the Ship COMPLETED
    100 Take off from Vectera COMPLETED
    101 Learn to Fly COMPLETED
    102 (Optional) Power Up All Systems to Skip Tutorial COMPLETED
    104 Allocate Power to Thrusters COMPLETED
    103 Allocate Power to Engines COMPLETED
    105 Allocate Power to Shields COMPLETED
    106 Allocate Power to a Weapon COMPLETED
    110 Deal with the Crimson Fleet COMPLETED
    115 Loot a Ship COMPLETED
    120 Travel to Kreet COMPLETED
    117 Repair the Hull COMPLETED
    125 Land at the Kreet Research Base COMPLETED
     
    == Handles various systems for Followers ==
    ( Instance: 1 )
    100 Retrieve waiting followers DORMANT
     
    ==  ==
    ( Instance: 4 )
    100 Evacuate the Survivalist COMPLETED

""".trimIndent()