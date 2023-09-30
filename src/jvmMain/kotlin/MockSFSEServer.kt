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
            |
        """.trimMargin()
        "GetPlayerHomeSpaceShip" -> """
            |GetPlayerHomeSpaceShip
            |Command: GetPlayerHomeSpaceShip
            |Player Home Ship >> 0.00
            |
        """.trimMargin()
        "sqo" -> """
sqo
Command: sqo
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
900 Talk with Barrett COMPLETED
 
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
 
== One Small Step ==
( Instance: 1 )
130 Deal with the Crimson Fleet Captain COMPLETED
132 (Optional) Unlock the Safe COMPLETED
137 Return to the Ship COMPLETED
140 Grav Jump to Jemison COMPLETED
160 Land at New Atlantis COMPLETED
170 Go to the Lodge (MAST District) COMPLETED
 
== Misc Pointer to Ship Services ==
( Instance: 0 )
10 Talk to the Ship Services Technician COMPLETED
 
== Keeping the Peace ==
( Instance: 1 )
10 Speak to Agent Plato COMPLETED
 
== Wrapper for FFNewAtlantis02 ==
( Instance: 1 )
100 Talk to Sergeant Yumi COMPLETED
 
== Wrapper for Botany Quest Line ==
( Instance: 1 )
100 Talk to the scientist by the Tree COMPLETED
 
== Coffee Run ==
( Instance: 1 )
10 Deliver a TerraBrew Cappuccino to Donna Rain COMPLETED
 
== Back to the Grind ==
( Instance: 1 )
100 Apply for a Job at a Ryujin Industries Kiosk COMPLETED
150 Travel to Neon on Volii Alpha in the Volii System COMPLETED
 
== Due in Full ==
( Instance: 1 )
100 Collect Dieter Maliki's debt DISPLAYED
 
== Wrapper for Viewport Quest Line ==
( Instance: 1 )
100 Talk to the Bartender at Viewport COMPLETED
 
== One Small Step ==
( Instance: 1 )
180 Enter the Library COMPLETED
185 Talk to Sarah COMPLETED
187 Listen to the Meeting COMPLETED
190 Place the Artifact COMPLETED
195 Listen to the Meeting COMPLETED
200 Talk to Sarah COMPLETED
118 Allocate Power to Grav Drive COMPLETED
150 Fight Off or Evade the Crimson Fleet Ship COMPLETED
 
== Board and Lodging ==
( Instance: 0 )
10 Talk to Noel about quarters COMPLETED
 
== The Old Neighborhood ==
( Instance: 1 )
10 Talk to Sarah COMPLETED
 
== Board and Lodging ==
( Instance: 0 )
20 Follow Noel COMPLETED
 
== Misc Pointer to Mission Board ==
( Instance: 0 )
10 Check out the Constellation Mission Board DISPLAYED
 
== Misc Pointer to Crafting ==
( Instance: 0 )
10 Craft or Modify an Item COMPLETED
 
== Misc Pointer to Research ==
( Instance: 0 )
10 Complete a Research Project COMPLETED
 
== The Old Neighborhood ==
( Instance: 1 )
20 Talk to Sarah's Contact COMPLETED
 
== Companion - Sarah Morgan ==
( Instance: 0 )
10 Talk with Sarah Morgan COMPLETED
 
== Misc Pointer to Outpost ==
( Instance: 0 )
10 Start an Outpost on a Planet COMPLETED
 
== Misc pointer to Cmdr. Tuala of the Vanguard ==
( Instance: 0 )
100 Speak to Commander Tuala about joining the Vanguard COMPLETED
 
== Supra et Ultra ==
( Instance: 1 )
100 Register for the Vanguard COMPLETED
 
== The Old Neighborhood ==
( Instance: 1 )
30 Ask About Moara in Cydonia COMPLETED
 
== Keeping the Peace ==
( Instance: 1 )
40 Retrieve the package COMPLETED
 
== Supra et Ultra ==
( Instance: 1 )
200 (Optional) Explore the Orientation Hall COMPLETED
300 Proceed to the Vanguard exam COMPLETED
400 Proceed to the next exam COMPLETED
410 Enter the Piloting Simulator COMPLETED
 
== Vanguard Piloting Exam ==
( Instance: 1 )
100 Enter the pilot's seat DORMANT
300 Defeat all targets (0/4) DORMANT
250 Prepare for your next target DORMANT
999 (Optional) Record your score by exiting the simulation DORMANT
350 Restart current tier from the simulation control computer DORMANT
 
== Supra et Ultra ==
( Instance: 1 )
600 Speak to Commander Tuala COMPLETED
650 Follow Commander Tuala COMPLETED
660 Speak to Commander Tuala COMPLETED
 
== Grunt Work ==
( Instance: 1 )
100 Speak to Crew Chief Herath at the New Atlantis Spaceport COMPLETED
 
== Supra et Ultra ==
( Instance: 1 )
301 (Optional) Resupply before entering the exam  COMPLETED
310 Throw all Sector Switches to complete the exam (0/3) COMPLETED
311 (Optional) Bypass the exam to leave your run unfinished COMPLETED
360 Clear the condensers of Leeches COMPLETED
370 Speak to Bea COMPLETED
 
== Grunt Work ==
( Instance: 1 )
200 Make contact with the settlers on Tau Ceti II COMPLETED
 
== Misc Pointer to Cargo ==
( Instance: 0 )
10 Check Out Your Ship's Inventory COMPLETED
 
== Survey Says ==
( Instance: 1 )
10 Board the SSNN ship FAILED
 
== London Landmark Quest ==
( Instance: 1 )
100 Visit the London Landmark on Earth COMPLETED
 
== Grunt Work ==
( Instance: 1 )
250 Investigate the scream COMPLETED
300 Speak to the woman COMPLETED
420 Restore the security system connection COMPLETED
430 Allow Hadrian to analyze the system COMPLETED
605 Tune the livestock tracker to 183.5 COMPLETED
607 Wait for the system to reengage COMPLETED
601 (Optional) Restore power to the "Kill Lanes" (3/3) COMPLETED
500 Kill the Terrormorph COMPLETED
699 Collect the Terrormorph tissue sample COMPLETED
610 (Optional) Tune the livestock tracker to 183.5 COMPLETED
615 (Optional) Lure the Terrormorph to the fuel tanks COMPLETED
700 Return to Hadrian COMPLETED
710 Follow Hadrian COMPLETED
750 Speak to Hadrian COMPLETED
800 Return to Commander Tuala COMPLETED
 
== Deep Cover ==
( Instance: 1 )
30 Proceed to the UC Vigilance COMPLETED
 
== Grunt Work ==
( Instance: 1 )
602 (Optional) Reenable the livestock tracker COMPLETED
 
== Delivering Devils ==
( Instance: 1 )
300 Ask about Percival at the Trade Authority COMPLETED
 
== Destroy the Crimson Fleet Phantom at Narion ==
( Instance: 2 )
10 Go to any planet in the Narion system COMPLETED
 
== Distilling Confidence ==
( Instance: 1 )
50 Access Secure Storage DISPLAYED
 
== Keeping the Peace ==
( Instance: 1 )
50 Deliver the package to Sergeant Yumi COMPLETED
 
== Misc Pointer ==
( Instance: 0 )
100 Speak with Trevor COMPLETED
 
== Red Tape Blues ==
( Instance: 1 )
100 Gather Iron (10/10) COMPLETED
200 Deposit 10 units of Iron COMPLETED
 
== The Old Neighborhood ==
( Instance: 1 )
40 Go to Venus COMPLETED
 
== The Bounty That Got Away ==
( Instance: 1 )
100 Place the Sensor COMPLETED
 
== The Long Haul ==
( Instance: 1 )
100 Accept a Cargo Mission from the Mission Board DISPLAYED
 
== Delivering Devils ==
( Instance: 1 )
400 Speak to the miners at The Sixth Circle COMPLETED
 
== Misc Pointer ==
( Instance: 0 )
100 Speak with Laylah COMPLETED
 
== Delivering Devils ==
( Instance: 1 )
430 Speak to the bartender COMPLETED
501 (Optional) Change Percival's debt amount COMPLETED
500 Pay off Percival's debt COMPLETED
510 (Optional) Speak to the bar patron COMPLETED
600 (Optional) Kill the Spacer leader in the Deep Mines COMPLETED
 
== Quest for spawning and scenes for settlers to sign up for LIST ==
( Instance: 1 )
100 Sign up settler for the LIST DORMANT
 
== Outpost Tutorial (should be a Misc Objective) ==
( Instance: 1 )
10 Use Hand Scanner to place an Outpost Beacon COMPLETED
 
== The Bounty That Got Away ==
( Instance: 1 )
200 Return to the Tracker Agent COMPLETED
 
== Quest for spawning and scenes for settlers to sign up for LIST ==
( Instance: 2 )
100 Sign up settler for the LIST DORMANT
 
== Quest for spawning and scenes for settlers to sign up for LIST ==
( Instance: 3 )
100 Sign up settler for the LIST DORMANT
 
== Mantis ==
( Instance: 1 )
100 Read the Secret Outpost slate COMPLETED
200 Go to the Secret Outpost at Denebola I-b COMPLETED
 
== Delivering Devils ==
( Instance: 1 )
800 (Optional) Contact Cambridge on the comms panel COMPLETED
810 (Optional) Collect an Aqueous Hematite sample COMPLETED
811 (Optional) Collect a Laser Cutter COMPLETED
820 (Optional) Deposit the Hematite in the Thresher COMPLETED
830 (Optional) Wait for the Thresher to complete analysis COMPLETED
835 (Optional) Collect Cambridge's research COMPLETED
840 (Optional) Contact Cambridge COMPLETED
843 (Optional) Find the elevator key COMPLETED
845 (Optional) Return the data to Cambridge COMPLETED
 
== Wanted... Dead not Alive ==
( Instance: 1 )
100 Accept a Bounty Mission from the Mission Board DISPLAYED
 
== Red Tape Blues ==
( Instance: 1 )
250 Speak with Trevor COMPLETED
300 Apply for the Assistant to the Director job COMPLETED
 
== Delivering Devils ==
( Instance: 1 )
850 Use Cambridge's data to clear Percival's debt COMPLETED
502 (Optional) Collect credits from Lou COMPLETED
503 (Optional) Steal the "Cydonia Utility Area Key" from Security COMPLETED
825 (Optional) Start the Thresher COMPLETED
890 Speak to Lou COMPLETED
900 Find Percival COMPLETED
 
== Freight Fright ==
( Instance: 1 )
100 Enter the ship COMPLETED
 
== Quest for spawning and scenes for settlers to sign up for LIST ==
( Instance: 4 )
100 Sign up settler for the LIST DORMANT
 
== Freight Fright ==
( Instance: 1 )
250 Exterminate Heat Leeches COMPLETED
200 Check the cargo COMPLETED
300 Return to Denis COMPLETED
 
== Delivering Devils ==
( Instance: 1 )
920 Follow Percival COMPLETED
942 Meet Hadrian at The Sixth Circle COMPLETED
 
== Survey Cassiopeia IV-a in Eta Cassiopeia ==
( Instance: 3 )
10 Complete survey (100%) COMPLETED
 
== Delivering Devils ==
( Instance: 1 )
950 Follow Hadrian COMPLETED
960 Speak to Hadrian COMPLETED
 
== Eyewitness ==
( Instance: 1 )
100 Meet Hadrian in New Atlantis (MAST District) COMPLETED
 
== Delivering Devils ==
( Instance: 1 )
891 Clear obstructions from the power junction COMPLETED
 
== Quest for spawning and scenes for settlers to sign up for LIST ==
( Instance: 5 )
100 Sign up settler for the LIST DORMANT
 
== Red Tape Blues ==
( Instance: 1 )
350 Speak with Trevor COMPLETED
 
== Taste of Home ==
( Instance: 1 )
20 Deliver Jake's Stout to Sandra Fullerton DISPLAYED
 
== The Old Neighborhood ==
( Instance: 1 )
41 Talk to Sarah COMPLETED
32 (Optional) Check out the Mission Board COMPLETED
42 Examine the Satellite COMPLETED
43 (Optional) Use minimal power to avoid detection COMPLETED
60 Go to Nova Galactic Staryard COMPLETED
61 Talk to Sarah COMPLETED
65 Dock with Nova Galactic Staryard COMPLETED
70 Find Any Clues about Moara COMPLETED
80 Go to Neptune COMPLETED
 
== Top of the L.I.S.T. ==
( Instance: 1 )
100 Survey a Habitable Planet COMPLETED
200 Identify a potential LIST recruit by eavesdropping COMPLETED
250 Sign up settler for the LIST COMPLETED
 
== Quest for spawning and scenes for settlers to sign up for LIST ==
( Instance: 6 )
100 Sign up settler for the LIST COMPLETED
 
== Red Tape Blues ==
( Instance: 1 )
400 Delete the other candidates' applications COMPLETED
500 Speak with Trevor COMPLETED
 
== Red Tape Runaround ==
( Instance: 1 )
100 Report to work for Peter Brennan COMPLETED
300 Speak with Governor Hurst COMPLETED
400 Find the stolen ship COMPLETED
500 Hail the ship COMPLETED
600 Enter the ship COMPLETED
650 (Optional) Destroy Hurst's Ship FAILED
610 Deal with the Pirates COMPLETED
620 (Optional) Search for more clues FAILED
550 Destroy the ship DORMANT
700 Return to Hurst COMPLETED
 
== Misc Pointer ==
( Instance: 0 )
100 (Optional) Show Hurst's note to CDR Woodard COMPLETED
 
== Red Tape Runaround ==
( Instance: 1 )
900 Deliver the package to Peter COMPLETED
1000 Approve the equipment request COMPLETED
 
== Red Tape Reclamation ==
( Instance: 1 )
100 Speak with Trevor COMPLETED
300 Speak with Ship Services COMPLETED
400 Speak with Trevor DISPLAYED
 
== Groundpounder ==
( Instance: 1 )
100 Go to the Altair System COMPLETED
200 Land at Research Outpost U3-09 on Altair II COMPLETED
300 Find Private Mahoney COMPLETED
425 Clear the Spacers outside the infirmary and talk to Lezama COMPLETED
435 Find and rescue Lieutenant Torres COMPLETED
400 Rescue Captain Myeong COMPLETED
475 Talk to Captain Myeong COMPLETED
490 Take your Ship to the Research Camp on Altair II COMPLETED
 
== Diplomatic Immunity ==
( Instance: 0 )
5 Talk to Representative Chisolm FAILED
 
== The Art Dealer ==
( Instance: 1 )
10 Get the art from Zoe Kaminski COMPLETED
 
== Diplomatic Immunity ==
( Instance: 0 )
10 Talk to an Embassy Diplomat FAILED
 
== The Art Dealer ==
( Instance: 1 )
20 Deliver the art to Samson COMPLETED
 
== Groundpounder ==
( Instance: 1 )
500 Eliminate the Spacers at the Research Camp COMPLETED
600 Talk with survivors COMPLETED
700 Talk to Lieutenant Torres COMPLETED
800 Defeat Spacers in Altair I's orbit COMPLETED
850 Defeat Spacers in Altair V's orbit COMPLETED
900 Land on Research Outpost U3-09 COMPLETED
1000 Defeat the Spacer Invasion COMPLETED
1100 Talk to Lieutenant Torres COMPLETED
 
== Clear the Skies ==
( Instance: 1 )
100 Destroy the hostile ships in orbit around Altair II COMPLETED
300 Return to the Strong Arm Worker COMPLETED
 
== The Old Neighborhood ==
( Instance: 1 )
90 Approach the Ship COMPLETED
100 Evade Fire COMPLETED
110 Damage Moara's Ship COMPLETED
120 Dock with Moara's Ship COMPLETED
130 Clear Moara's Ship of Hostiles COMPLETED
140 Get the Key COMPLETED
150 Talk to Moara COMPLETED
155 Take the Artifact COMPLETED
160 Return to the Lodge COMPLETED
 
== Quest for spawning and scenes for settlers to sign up for LIST ==
( Instance: 7 )
100 Sign up settler for the LIST DORMANT
 
== The Old Neighborhood ==
( Instance: 1 )
170 Add the Artifact COMPLETED
180 Wait for Sarah COMPLETED
190 Talk to Sarah COMPLETED
 
== The Empty Nest ==
( Instance: 1 )
10 Talk to Sam Coe COMPLETED
 
== Companion - Sam Coe ==
( Instance: 0 )
10 Talk with Sam Coe COMPLETED
 
== Into the Unknown ==
( Instance: 1 )
10 Talk to Vladimir COMPLETED
 
== Back to Vectera ==
( Instance: 1 )
10 Return to Vectera COMPLETED
 
== Companion - Andreja ==
( Instance: 0 )
10 Talk with Andreja COMPLETED
 
== Eyewitness ==
( Instance: 1 )
350 Proceed to the Cabinet Chambers COMPLETED
 
== Misc Pointer ==
( Instance: 1 )
10 Talk to Sergeant Yumi COMPLETED
 
== Eyewitness ==
( Instance: 1 )
410 Address the Cabinet COMPLETED
425 Listen to the Cabinet COMPLETED
600 Take the NAT to the Spaceport COMPLETED
570 Listen to the guards COMPLETED
575 Collect the EM weapon COMPLETED
581 Equip the EM weapon COMPLETED
580 Incapacitate the attackers COMPLETED
590 Assess the situation at the NAT station COMPLETED
610 Proceed to the Spaceport COMPLETED
650 Eliminate the Terrormorph COMPLETED
690 Approach the officer in charge COMPLETED
800 Eliminate the Terrormorphs COMPLETED
801 (Optional) Speak to the Fireteam COMPLETED
850 Speak to Sergeant Yumi COMPLETED
900 Report to President Abello in the MAST Cabinet Chambers COMPLETED
400 Listen to Hadrian's conversation COMPLETED
500 Proceed to the Operations Post COMPLETED
550 Speak to President Abello COMPLETED
715 (Optional) Access the UC Security contraband store COMPLETED
 
== Friends Like These ==
( Instance: 1 )
100 Speak to Deputy MacIntyre COMPLETED
300 Acquire the Freestar Collective Archival Code COMPLETED
 
== A Tree Grows in New Atlantis ==
( Instance: 1 )
20 Locate the Bio-Sensors COMPLETED
 
== [Misc Objective Pointer] ==
( Instance: 1 )
10 Speak to the Man COMPLETED
 
== Misc Pointer ==
( Instance: 1 )
20 Talk to Sergeant Yumi COMPLETED
 
== Friends Like These ==
( Instance: 1 )
351 Tell Radcliff you agree to her terms COMPLETED
340 Confront Radcliff COMPLETED
350 Follow Radcliff COMPLETED
310 Access the UC listening device COMPLETED
352 Collect the Freestar Collective Archival Code yourself COMPLETED
500 Acquire House Va'ruun's Archival Code COMPLETED
501 Approach the intercom COMPLETED
507 Throw the power switch COMPLETED
510 Approach the intercom COMPLETED
515 Continue deeper into the embassy COMPLETED
519 Throw the power switch COMPLETED
539 Throw the power switch COMPLETED
650 Explore the basement COMPLETED
655 Eliminate the robots COMPLETED
695 Approach the stranger COMPLETED
710 Speak with Ambassador Bal'mor COMPLETED
730 (Optional) Pickpocket the Code Machine key from Bal'mor COMPLETED
790 Collect Bal'mor's Code Machine Key COMPLETED
800 Collect House Va'ruun's code from the machine COMPLETED
900 Return to Deputy MacIntyre (Interstellar Affairs) COMPLETED
925 Report to the Armistice Archives COMPLETED
930 Approach the Monitor Station COMPLETED
950 Deposit the code pieces COMPLETED
960 Collect the Terrormorph data COMPLETED
970 Return the data COMPLETED
980 Give the data to Hadrian COMPLETED
990 Speak to MacIntrye COMPLETED
 
== Wrapper for Well Quest Line ==
( Instance: 1 )
100 Investigate brown-outs in the Well COMPLETED
 
== Misc pointer to Aphelon Realty Office ==
( Instance: 0 )
100 Speak to Zora at the Aphelion Realty office COMPLETED
 
== The Devils You Know ==
( Instance: 1 )
100 Proceed to your meeting (Subsection Seven) COMPLETED
 
== Friends Like These ==
( Instance: 1 )
311 (Optional) Speak to the Freestar Embassy staffer COMPLETED
312 (Optional) Read "FC Embassy Security" slate COMPLETED
348 Speak to MacIntyre COMPLETED
370 Follow Cameron COMPLETED
375 Speak to Cameron COMPLETED
529 Throw the power switch COMPLETED
700 Speak to the stranger COMPLETED
725 Convince Bal'mor to hand over his Archive Code COMPLETED
 
== The Devils You Know ==
( Instance: 1 )
105 Use the intercom COMPLETED
110 Speak to the stranger COMPLETED
120 Speak to Vae Victis COMPLETED
150 (Optional) Speak to Deputy MacIntyre COMPLETED
200 Speak to Captain Marquez COMPLETED
 
== Misc pointer to player apartment in the Well ==
( Instance: 0 )
100 Visit your new home in the Well COMPLETED
 
== [BE - Objective Quest] ==
( Instance: 1 )
20 Kill the crew COMPLETED
 
== [BE - Objective Quest] ==
( Instance: 1 )
30 Take over the ship DORMANT
 
== [BE - Objective Quest] ==
( Instance: 1 )
40 Leave the ship COMPLETED
 
==  ==
( Instance: 1 )
100 Evacuate the Survivalist COMPLETED
 
== Failure to Communicate ==
( Instance: 1 )
100 Talk to Alban Lopez on Altair III-c COMPLETED
200 Repair the Lopez Communication Satellite COMPLETED
 
== [BE - Objective Quest] ==
( Instance: 2 )
30 Take over the ship COMPLETED
 
== [BE - Objective Quest] ==
( Instance: 2 )
40 Leave the ship COMPLETED
 
== [BE - Objective Quest] ==
( Instance: 2 )
20 Kill the crew COMPLETED
 
== Failure to Communicate ==
( Instance: 1 )
400 Repair the Lemaire Communication Satellite orbiting Altair I COMPLETED
500 Repair the Banda Communication Satellite orbiting Altair V-a COMPLETED
600 Repair the Wen Communication Satellite orbiting Altair IV-c COMPLETED
 
== Quest for spawning and scenes for settlers to sign up for LIST ==
( Instance: 8 )
100 Sign up settler for the LIST DORMANT
 
== A Break at Dawn ==
( Instance: 1 )
10 Speak to Royce at the Dawn's Roost COMPLETED
 
== [BE - Objective Quest] ==
( Instance: 3 )
30 Take over the ship COMPLETED
 
== [BE - Objective Quest] ==
( Instance: 3 )
40 Leave the ship COMPLETED
 
== [BE - Objective Quest] ==
( Instance: 3 )
20 Kill the crew COMPLETED
 
== Failure to Communicate ==
( Instance: 1 )
700 Report back to Alban Lopez on Jacquelyn's ship COMPLETED
780 Wait for Family Meeting to end COMPLETED
800 Get Chanda Banda to join Defense Pact COMPLETED
850 (Optional) Get Jacquelyn Lemaire to join Defense Pact COMPLETED
875 Talk to Alban Lopez about the Defense Pact COMPLETED
900 Eliminate Spacer ships orbiting Altair IV-a COMPLETED
950 Eliminate Spacer ships orbiting Altair IV-d COMPLETED
 
== Refurbished Goods ==
( Instance: 1 )
100 Go to Deserted Relay Station DISPLAYED
 
== Start-Up Stopped ==
( Instance: 1 )
100 Find the Berry Mule COMPLETED
 
== Heart of Mars ==
( Instance: 1 )
100 Find the Heart of Mars DISPLAYED
 
== Quest for spawning and scenes for settlers to sign up for LIST ==
( Instance: 9 )
100 Sign up settler for the LIST DORMANT
 
== Into the Unknown ==
( Instance: 1 )
12 Go to Piazzi IV-c COMPLETED
22 Go to Nesoi COMPLETED
40 Find Andreja COMPLETED
 
== Vlad's Home ==
( Instance: 0 )
10 Visit Vladimir's House COMPLETED
 
== Into the Unknown ==
( Instance: 1 )
30 Find the Artifact on Nesoi COMPLETED
20 Find the Artifact on Piazzi IV-c COMPLETED
 
== Training Ship ==
( Instance: 1 )
10 Damage the ship DORMANT
20 Board the ship DORMANT
30 Assist the instructor DORMANT
 
==  ==
( Instance: 2 )
100 Evacuate the Survivalist COMPLETED
 
== Into the Unknown ==
( Instance: 1 )
50 Add the Artifacts to the Collection COMPLETED
55 Talk to Matteo COMPLETED
60 Talk to Vladimir COMPLETED
 
== A Tree Grows in New Atlantis ==
( Instance: 1 )
58 Ask Jorden about the Sensor COMPLETED
60 Get the Sensor from Wen Tseng COMPLETED
 
== A Light in the Darkness ==
( Instance: 1 )
10 Talk to Tommy Bitlow COMPLETED
25 Talk to Nadia Muffaz COMPLETED
50 Find new stories for SSNN COMPLETED
100 Deliver news about the Vectera Mining Attack COMPLETED
400 Deliver news about the Terrormorph attack on New Atlantis COMPLETED
 
== Primary Sources ==
( Instance: 1 )
20 Speak to Theresa at Kay's House COMPLETED
40 Speak to Nurse O'Shea at the Medbay COMPLETED
60 Speak to Henrik at Apex Electronics COMPLETED
 
== A Tree Grows in New Atlantis ==
( Instance: 1 )
100 Deliver the Sensors COMPLETED
110 Wait for Kelton's Analysis COMPLETED
120 Speak to Kelton Frush COMPLETED
10 Speak to Kelton Frush COMPLETED
69 (Optional) Steal the Sensor COMPLETED
 
== [Misc Objective Pointer] ==
( Instance: 1 )
10 Check in on Kelton later COMPLETED
 
== Start-Up Stopped ==
( Instance: 1 )
200 Hail the Berry Mule COMPLETED
250 Dock with and enter the Berry Mule COMPLETED
300 Speak with the mercenary leader COMPLETED
400 Free Erick COMPLETED
500 Return to Jane COMPLETED
 
== Married Couple ==
( Instance: 1 )
10 Board the Haemosu FAILED
 
== Into the Unknown ==
( Instance: 1 )
70 Go to Procyon III COMPLETED
71 Land at the Scanner Anomaly COMPLETED
72 Follow Distortions on the Scanner COMPLETED
 
== [Misc Objective Pointer] ==
( Instance: 1 )
20 Speak with Kelton Frush DISPLAYED
 
== Into the Unknown ==
( Instance: 1 )
73 Investigate the Source of the Anomaly COMPLETED
80 Return to the Lodge COMPLETED
 
==  ==
( Instance: 3 )
100 Evacuate the Survivalist COMPLETED
 
== Into the Unknown ==
( Instance: 1 )
90 Use "Anti-Gravity Field" in front of Vladimir COMPLETED
100 Talk to Vladimir COMPLETED
 
== Power from Beyond ==
( Instance: 1 )
3 Talk to Vladimir to Locate Temples COMPLETED
 
== Power from Beyond ==
( Instance: 1 )
10 Acquire the Power on Indum II COMPLETED
 
== Power from Beyond (Altair III) ==
( Instance: 1 )
10 Acquire the Power on Altair III COMPLETED
 
== Power from Beyond (Altair III) ==
( Instance: 1 )
15 Follow Distortions on the Scanner COMPLETED
 
== Re-Re-Application ==
( Instance: 1 )
20 Travel to New Atlantis COMPLETED
 
== The Devils You Know ==
( Instance: 1 )
300 Find "The Warlock" COMPLETED
 
== Winning Hearts and Minds ==
( Instance: 1 )
15 Talk to Orval Romack DISPLAYED
 
== Quest for spawning and scenes for settlers to sign up for LIST ==
( Instance: 10 )
100 Sign up settler for the LIST DORMANT
 
== The Devils You Know ==
( Instance: 1 )
400 Destroy the ship COMPLETED
401 OR Disable the engines COMPLETED
420 OR Board "The Warlock" COMPLETED
430 OR Arrest the pilot COMPLETED
500 Collect the evidence COMPLETED
550 Return the evidence to Vae Victis COMPLETED
 
== Party Cruise ==
( Instance: 1 )
20 Board the Party Cruise FAILED
 
== Power from Beyond (Volii Epsilon) ==
( Instance: 2 )
10 Acquire the Power on Volii Epsilon COMPLETED
 
== Back to the Grind ==
( Instance: 1 )
200 Attend Your Interview at Ryujin Tower COMPLETED
 
==  ==
( Instance: 1 )
1 Visit your rented room DISPLAYED
 
== Wrapper for NeonZ04 ==
( Instance: 1 )
100 Talk to James Newill COMPLETED
 
== Loose Ends ==
( Instance: 1 )
100 Speak to Neshar in Jail DISPLAYED
 
== Back to the Grind ==
( Instance: 1 )
210 (Optional) Check in with Lane Garza COMPLETED
300 Take a Seat COMPLETED
400 Complete Your Interview COMPLETED
500 Pick Up Coffee at TerraBrew COMPLETED
600 Deal With Tomo COMPLETED
630 Kill Tomo COMPLETED
700 Pick Up Coffee at TerraBrew COMPLETED
800 Talk to Imogene COMPLETED
 
== Wrapper for NeonZ03 ==
( Instance: 1 )
100 Talk to Frank Renick COMPLETED
 
== Back to the Grind ==
( Instance: 1 )
900 Meet Alexis Pryce COMPLETED
1000 Meet Camden Cho COMPLETED
1100 Meet Genevieve Monohan COMPLETED
1200 Meet Linden Calderi COMPLETED
1300 Talk to Imogene COMPLETED
 
== One Step Ahead ==
( Instance: 1 )
100 Talk to Imogene COMPLETED
200 Upload the Program at CeltCorp COMPLETED
 
== Re-Re-Application ==
( Instance: 1 )
30 Talk to John Tuala COMPLETED
 
== The Empty Nest ==
( Instance: 1 )
20 Travel to Akila City COMPLETED
 
== Power from Beyond (Ourea) ==
( Instance: 1 )
10 Acquire the Power on Ourea COMPLETED
 
== The Empty Nest ==
( Instance: 1 )
30 Talk to Sam COMPLETED
40 Go to the GalBank Vault COMPLETED
 
== Job Gone Wrong ==
( Instance: 1 )
100 Talk to Daniel Blake COMPLETED
 
== The Empty Nest ==
( Instance: 1 )
45 Complete "Job Gone Wrong" COMPLETED
 
== Job Gone Wrong ==
( Instance: 1 )
200 Persuade the Bank Robbers to Surrender COMPLETED
300 Talk to Daniel Blake COMPLETED
400 Ask to see the hostages OR sneak into the bank COMPLETED
410 Enter GalBank COMPLETED
420 Eliminate the bank robbers COMPLETED
500 Talk to Daniel Blake COMPLETED
 
== Deputized ==
( Instance: 1 )
100 Talk to Emma Wilcox COMPLETED
 
== A Light in the Darkness ==
( Instance: 1 )
300 Deliver news about the GalBank robbery in Akila City COMPLETED
 
== Deputized ==
( Instance: 1 )
150 Complete a Freestar Ranger mission COMPLETED
 
== Kill the Outlaw Gang Leader on Okoro Epsilon ==
( Instance: 1 )
10 Collect the bounty on the Outlaw Gang Leader at the Abandoned Research Tower COMPLETED
 
== Destroy the Crimson Fleet Phantom at Narion ==
( Instance: 2 )
20 Locate and destroy the Crimson Fleet Phantom COMPLETED
 
== Deputized ==
( Instance: 1 )
180 Talk to Emma Wilcox COMPLETED
200 Follow Emma Wilcox COMPLETED
250 Talk to Marshal Blake COMPLETED
500 Travel to Waggoner Farm COMPLETED
 
==  ==
( Instance: 0 )
5 Go to Eleos DISPLAYED
 
== Defensive Measures ==
( Instance: 1 )
8 Listen to Davis Wilson's argument COMPLETED
10 Talk to Davis Wilson COMPLETED
20 Talk to Keoni Alpin COMPLETED
 
== Deputized ==
( Instance: 1 )
600 Talk to Mikaela Waggoner COMPLETED
800 Find the Ruffians by examining their tracks COMPLETED
1000 Confront the Ruffians COMPLETED
1050 Kill the Ruffians COMPLETED
1075 Talk to Emma Wilcox COMPLETED
1100 Search for clues in the camp COMPLETED
1300 Read the "Job's Done" Slate COMPLETED
1400 Talk to Emma Wilcox COMPLETED
1500 Talk to Mikaela Waggoner COMPLETED
300 Talk to Emma Wilcox COMPLETED
400 Meet Helga Dubray at your ship COMPLETED
410 (Optional) Get supplies from the armory COMPLETED
700 Talk to Emma Wilcox COMPLETED
750 Follow Emma Wilcox COMPLETED
1200 Search the Stash Box COMPLETED
1550 Wait for Emma Wilcox to board your ship COMPLETED
1600 Talk to Marshal Blake COMPLETED
 
== Where Hope is Built ==
( Instance: 1 )
100 Go to Akila City COMPLETED
200 Talk to Daniel Blake COMPLETED
850 Go to Polvo's orbit COMPLETED
 
== The Empty Nest ==
( Instance: 1 )
50 Search for the Maps COMPLETED
55 Talk to Sam COMPLETED
60 Talk to Jacob COMPLETED
70 Talk to Sam COMPLETED
80 Get the Maps COMPLETED
85 (Optional) Talk to Jacob and Persuade Him COMPLETED
90 (Optional) Talk to Jacob and Have Sam Distract Him COMPLETED
95 (Optional) Ask Cora for Help COMPLETED
100 Talk to Sam COMPLETED
110 Travel to the Empty Nest COMPLETED
120 Get the Artifact COMPLETED
125 Leave the Hideout COMPLETED
127 Deal with Shaw COMPLETED
130 Return to the Lodge COMPLETED
128 (Optional) Kill the Ashta COMPLETED
129 (Optional) Talk to Shaw COMPLETED
 
== Where Hope is Built ==
( Instance: 1 )
950 Board Nia Kalu's ship COMPLETED
 
== Juno's Gambit ==
( Instance: 1 )
10 Destroy Ecliptic Ship COMPLETED
100 Dock with the ship COMPLETED
110 Investigate the voices on board COMPLETED
200 Talk to Ryujin Operatives COMPLETED
350 Talk to Juno COMPLETED
352 Attach Control Board COMPLETED
355 Deal with Ryujin Operatives  COMPLETED
550 Talk to Juno COMPLETED
500 Talk to Ryujin Operatives COMPLETED
600 Return to your ship COMPLETED
400 Deliver Operatives to Neon COMPLETED
700 Undock your Ship COMPLETED
800 Observe Juno COMPLETED
 
== Power from Beyond (Dionysus) ==
( Instance: 3 )
10 Acquire the Power on Dionysus COMPLETED
 
== Delivery Truck ==
( Instance: 1 )
401 Deliver Star Parcel Package to Akila City COMPLETED
 
== [BE - Objective Quest] ==
( Instance: 4 )
30 Take over the ship COMPLETED
 
== [BE - Objective Quest] ==
( Instance: 4 )
40 Leave the ship COMPLETED
 
== [BE - Objective Quest] ==
( Instance: 4 )
20 Kill the crew COMPLETED
 
== The Empty Nest ==
( Instance: 1 )
140 Add the Artifact COMPLETED
150 Wait for Sam COMPLETED
160 Talk to Sam COMPLETED
 
== Mantis ==
( Instance: 1 )
300 Explore the Lair of the Mantis COMPLETED
350 (Optional) Learn more about the Lair of the Mantis COMPLETED
400 (Optional) Talk to Livvey COMPLETED
600 Claim the Mantis's Spacesuit COMPLETED
700 Claim the Mantis's Starship COMPLETED
800 Go to the Mantis's Starship COMPLETED
 
== First Contact ==
( Instance: 1 )
100 Speak with Chief Sugiyama COMPLETED
200 Hail the Unidentified Ship COMPLETED
300 Dock with the ship and enter COMPLETED
400 Speak with the ship's captain COMPLETED
500 Follow Captain Brackenridge COMPLETED
600 Speak with Captain Brackenridge COMPLETED
650 Speak with Chief Sugiyama COMPLETED
 
== Where Hope is Built ==
( Instance: 1 )
1000 Talk to Nia Kalu COMPLETED
1050 Repair Nia Kalu's ship COMPLETED
1100 Talk to Nia Kalu COMPLETED
1110 Go to Miatha COMPLETED
1125 Destroy the outlaw ships COMPLETED
 
== [BE - Objective Quest] ==
( Instance: 5 )
30 Take over the ship DORMANT
 
== [BE - Objective Quest] ==
( Instance: 5 )
40 Leave the ship COMPLETED
 
== [BE - Objective Quest] ==
( Instance: 5 )
20 Kill the crew DORMANT
 
== [BE - Objective Quest] ==
( Instance: 6 )
30 Take over the ship DORMANT
 
== [BE - Objective Quest] ==
( Instance: 6 )
40 Leave the ship COMPLETED
 
== [BE - Objective Quest] ==
( Instance: 6 )
20 Kill the crew COMPLETED
 
== Where Hope is Built ==
( Instance: 1 )
1130 Go to HopeTown COMPLETED
1150 Talk to Nia Kalu COMPLETED
 
== Captain's Bounty ==
( Instance: 1 )
100 Find the Tracker hunting Captain Faheem COMPLETED
300 Return to Adella Faheem COMPLETED
200 Kill Ernesto COMPLETED
 
== Where Hope is Built ==
( Instance: 1 )
1170 Follow Nia Kalu COMPLETED
1200 Talk to Ron Hope COMPLETED
1300 Listen to Cosette's Report COMPLETED
1400 Talk to Nia Kalu COMPLETED
300 Talk to Daniel Blake COMPLETED
400 Wait for the debriefing to end COMPLETED
600 Talk to Daniel Blake COMPLETED
650 Talk to Emma Wilcox COMPLETED
800 Talk to Daniel Blake COMPLETED
900 Talk to Nia Kalu COMPLETED
 
== Shadows in Neon ==
( Instance: 1 )
50 Go to Neon COMPLETED
100 Talk to Jaylen Pryce COMPLETED
150 Follow Jaylen Pryce COMPLETED
 
== Wrapper for NeonZ01 ==
( Instance: 1 )
100 Talk to BorealUS COMPLETED
 
==  ==
( Instance: 0 )
10 Visit your Sleepcrate unit in Neon COMPLETED
 
== Shadows in Neon ==
( Instance: 1 )
200 Talk to Billy Clayton COMPLETED
250 Acquire a Warehouse Key COMPLETED
300 Confront Emmet Goodman COMPLETED
400 Talk to Billy Clayton COMPLETED
500 Confront Grace Early COMPLETED
 
== Wrapper for NeonZ08 ==
( Instance: 1 )
100 Talk to Tevin Anastas COMPLETED
 
== Shadows in Neon ==
( Instance: 1 )
650 Go to Akila City COMPLETED
700 Talk to Alex Shadid COMPLETED
800 Talk to Daniel Blake COMPLETED
320 (Optional) Pay off Billy's debt COMPLETED
600 Talk to Jaylen Pryce COMPLETED
 
== On the Run ==
( Instance: 1 )
50 Go to Red Mile COMPLETED
 
== Surgical Strike ==
( Instance: 1 )
50 Go to the Clinic COMPLETED
 
== Power from Beyond ==
( Instance: 1 )
15 Follow Distortions on the Scanner COMPLETED
 
== Power from Beyond ==
( Instance: 1 )
5 Use the Eye to Locate Temples COMPLETED
 
== Power from Beyond ==
( Instance: 1 )
7 Obtain a Ship with a 28LY Grav Jump Range COMPLETED
 
== Power from Beyond (Ourea) ==
( Instance: 1 )
15 Follow Distortions on the Scanner COMPLETED
 
== [BE - Objective Quest] ==
( Instance: 7 )
30 Take over the ship COMPLETED
 
== [BE - Objective Quest] ==
( Instance: 7 )
40 Leave the ship COMPLETED
 
== [BE - Objective Quest] ==
( Instance: 7 )
20 Kill the crew COMPLETED
 
== Power from Beyond (Volii Epsilon) ==
( Instance: 2 )
15 Follow Distortions on the Scanner COMPLETED
 
== First Contact ==
( Instance: 1 )
700 Negotiate with Oliver Campbell COMPLETED
 
== Misc Pointer ==
( Instance: 0 )
50 Speak with Dirk COMPLETED
100 Check the Computer's Lost and Found DISPLAYED
 
== First Contact ==
( Instance: 1 )
690 Speak with Keavy COMPLETED
2000 Go to HopeTech COMPLETED
2100 Speak with Bennu St. James COMPLETED
2200 Speak with Amin COMPLETED
2300 Prepare the ship for the Grav Drive COMPLETED
2400 Speak with Captain Brackenridge COMPLETED
 
== Misc Pointer ==
( Instance: 0 )
100 Speak with Abe COMPLETED
 
== Misc Pointer ==
( Instance: 0 )
100 Speak with Janet DISPLAYED
 
== Family Reunion ==
( Instance: 1 )
100 Deliver message to Amina Kurz DISPLAYED
 
==  ==
( Instance: 1 )
2100 Location of the ECS Constant DISPLAYED
 
== Power from Beyond (Dionysus) ==
( Instance: 3 )
15 Follow Distortions on the Scanner COMPLETED
 
== On the Run ==
( Instance: 1 )
100 Talk to Autumn MacMillan COMPLETED
 
== Misc Pointer ==
( Instance: 0 )
100 Tell Mei You Want to Run the Red Mile DISPLAYED
110 Return When the Red Mile is Ready COMPLETED
 
== On the Run ==
( Instance: 1 )
200 Follow Autumn MacMillan COMPLETED
300 Sit at the table COMPLETED
350 Meet with Autumn's contact COMPLETED
400 Talk to Mei Devine COMPLETED
500 Listen to Mei Devine's Introduction COMPLETED
600 Activate the Red Mile Beacon COMPLETED
900 Talk to Mei Devine COMPLETED
1000 Talk to Autumn MacMillan COMPLETED
1100 Talk to Mei Devine COMPLETED
1200 Go to Codos COMPLETED
1400 Board Marco's ship COMPLETED
1500 Confront Marco COMPLETED
1600 Search for information about the First COMPLETED
1700 Give the Encrypted Slate to Alex Shadid COMPLETED
1300 Reserved COMPLETED
 
== Back to Vectera ==
( Instance: 1 )
20 Talk to Lin COMPLETED
30 Fix the Comms Computer COMPLETED
35 (Optional) Obtain 3 Power Cells COMPLETED
40 Find a Clue to Barrett's Location COMPLETED
50 Listen to "Emergency Transmission 01" COMPLETED
60 Return to Lin COMPLETED
65 Travel to Altair IV-a COMPLETED
67 (Optional) Recruit Lin as Crew COMPLETED
70 Find Barrett and Heller COMPLETED
80 Talk to Heller COMPLETED
85 Listen to "Emergency Transmission 02" COMPLETED
92 (Optional) Recruit Heller as Crew COMPLETED
90 Travel to Bessel III COMPLETED
100 Rescue Barrett COMPLETED
105 Kill Matsura COMPLETED
110 Return to the Lodge COMPLETED
115 Enter the Library COMPLETED
120 Wait for Barrett COMPLETED
130 Talk to Barrett COMPLETED
 
== All That Money Can Buy ==
( Instance: 1 )
10 Talk to Walter Stroud COMPLETED
20 Travel to Neon COMPLETED
30 Talk to Walter Stroud COMPLETED
40 Check in at Stroud-Eklund HQ COMPLETED
50 Talk to Issa Eklund COMPLETED
 
== Wrapper for NeonZ05 ==
( Instance: 1 )
100 Talk to "Doc" Manning COMPLETED
 
== All That Money Can Buy ==
( Instance: 1 )
60 Investigate the Seller COMPLETED
70 Ask about Security COMPLETED
75 Check the Door Controls COMPLETED
 
== Wrapper for NeonZ06 ==
( Instance: 1 )
100 Talk to Katherine Luzion DISPLAYED
 
== All for One ==
( Instance: 1 )
10 Speak to Dietrich Sieghart COMPLETED
15 (Optional) Ask Rosa Newill about Dietrich Sieghart COMPLETED
 
== All That Money Can Buy ==
( Instance: 1 )
65 Search the Seller's Sleepcrate COMPLETED
 
== Bare Metal ==
( Instance: 1 )
10 Speak to Warlord COMPLETED
20 Kill Warlord COMPLETED
 
== Wrapper for NeonZ07 ==
( Instance: 1 )
100 Talk to Saburo Okadigbo DISPLAYED
 
== All for One ==
( Instance: 1 )
20 Speak to James Newill COMPLETED
 
== All That Money Can Buy ==
( Instance: 1 )
80 Talk to Walter Stroud COMPLETED
 
== Bare Metal ==
( Instance: 1 )
25 Retrieve the credits COMPLETED
30 Return to Renick DISPLAYED
 
== Relief Run ==
( Instance: 1 )
10 Speak with Abbie Edding DISPLAYED
 
== Wrapper for NeonZ02 ==
( Instance: 1 )
100 Talk to Clover DISPLAYED
 
== All That Money Can Buy ==
( Instance: 1 )
90 Go to the Astral Lounge COMPLETED
100 Talk to Walter Stroud COMPLETED
110 Locate the Seller COMPLETED
120 Talk to Walter Stroud COMPLETED
130 Negotiate for the Artifact COMPLETED
137 Take the Artifact COMPLETED
140 Head to the Entrance COMPLETED
150 Talk to Issa Eklund COMPLETED
160 Go to Slayton Aerospace HQ COMPLETED
 
== Wrapper for NeonZ09 ==
( Instance: 1 )
100 Talk to Hamza DISPLAYED
 
== All That Money Can Buy ==
( Instance: 1 )
170 Find a Way to the Next Floor COMPLETED
180 Take the Elevator COMPLETED
190 Talk to Walter COMPLETED
210 Go to the Next Floor COMPLETED
215 Climb the Trade Tower COMPLETED
220 Confront Nicolaus Slayton COMPLETED
240 Talk to Musgrove COMPLETED
230 Kill Nicolaus Slayton COMPLETED
242 Kill Musgrove COMPLETED
250 Go to the Ship COMPLETED
 
== Mob Mentality ==
( Instance: 1 )
10 Ask Frankie about Headlock COMPLETED
 
== All That Money Can Buy ==
( Instance: 1 )
252 Talk to Walter Stroud COMPLETED
255 Take off from Neon COMPLETED
260 Return to the Lodge COMPLETED
135 Kill Musgrove COMPLETED
 
== Starborn ==
( Instance: 1 )
10 Surrender the Artifact COMPLETED
20 Grav Jump to Anywhere COMPLETED
25 Grav Jump to Anywhere COMPLETED
30 Talk to Noel COMPLETED
 
== [BE - Objective Quest] ==
( Instance: 8 )
30 Take over the ship DORMANT
 
== [BE - Objective Quest] ==
( Instance: 8 )
40 Leave the ship COMPLETED
 
== [BE - Objective Quest] ==
( Instance: 8 )
20 Kill the crew COMPLETED
 
== Power from Beyond ==
( Instance: 4 )
3 Talk to Vladimir to Locate Temples COMPLETED
 
== Power from Beyond ==
( Instance: 4 )
10 Acquire the Power on Bessel III-b COMPLETED
 
== Starborn ==
( Instance: 1 )
40 Attend the Lodge Meeting COMPLETED
45 Talk to Sarah COMPLETED
50 Add the Artifact to the Collection COMPLETED
 
==  ==
( Instance: 0 )
100 Speak with Walter COMPLETED
 
== Further Into the Unknown ==
( Instance: 1 )
10 Talk to Vladimir COMPLETED
12 Go to Aranae IV-d COMPLETED
22 Go to Worthless COMPLETED
 
== Surgical Strike ==
( Instance: 1 )
100 Talk to Ben Armistead COMPLETED
200 Follow Ben Armistead COMPLETED
300 Talk to Ari Miller COMPLETED
 
== Comfort Food ==
( Instance: 1 )
20 Deliver a plate of Liver Pate to The Clinic DISPLAYED
 
== Surgical Strike ==
( Instance: 1 )
310 Follow Ari Miller COMPLETED
320 Wait for Ari Miller to check the patient records COMPLETED
330 Talk to Ari Miller COMPLETED
350 (Optional) Search for the source of the system disruption COMPLETED
400 Talk to Candace Doolin COMPLETED
500 Talk to Jane Nakamori COMPLETED
600 Talk to Ari Miller COMPLETED
650 Gain entry to the VIP wing COMPLETED
700 Talk to Catalina Rivera COMPLETED
800 Search for clues COMPLETED
900 Read the "Urgent - Read Immediately" slate COMPLETED
950 Locate Catalina Rivera COMPLETED
975 Go to Sakharov COMPLETED
1000 Investigate the abandoned asteroid mine COMPLETED
1025 Talk to Maya Cruz COMPLETED
1100 Search for information about the First COMPLETED
1200 Give the Encrypted Slate to Alex Shadid COMPLETED
1050 Kill Maya Cruz COMPLETED
 
== First to Fight, First to Die ==
( Instance: 1 )
100 Go to the meeting room COMPLETED
120 Sit down COMPLETED
140 Continue the debrifing COMPLETED
180 Travel to Arcturus II COMPLETED
 
== Power from Beyond ==
( Instance: 4 )
15 Follow Distortions on the Scanner COMPLETED
 
== Power from Beyond ==
( Instance: 4 )
5 Use the Eye to Locate Temples COMPLETED
 
== Power from Beyond ==
( Instance: 4 )
7 Obtain a Ship with a 28LY Grav Jump Range COMPLETED
 
== First to Fight, First to Die ==
( Instance: 1 )
200 Destroy the mercenary patrol ships COMPLETED
 
== [BE - Objective Quest] ==
( Instance: 9 )
30 Take over the ship DORMANT
 
== [BE - Objective Quest] ==
( Instance: 9 )
40 Leave the ship COMPLETED
 
== [BE - Objective Quest] ==
( Instance: 9 )
20 Kill the crew COMPLETED
 
== Quest for spawning and scenes for settlers to sign up for LIST ==
( Instance: 11 )
100 Sign up settler for the LIST DORMANT
 
== The Audition ==
( Instance: 1 )
100 Meet the Ebbside Strikers DISPLAYED
 
== Mob Mentality ==
( Instance: 1 )
20 Deal with Headlock COMPLETED
30 Return to Tevin COMPLETED
14 Bribe Frankie for information COMPLETED
 
== One Step Ahead ==
( Instance: 1 )
300 Return to Imogene at Ryujin Tower COMPLETED
 
== A New Narrative ==
( Instance: 1 )
100 Talk to Imogene COMPLETED
150 Travel to Akila City COMPLETED
200 Plant the Confidential Files at Laredo Firearms in Akila City COMPLETED
300 Return to Imogene at Ryujin Tower COMPLETED
 
== Access is Key ==
( Instance: 1 )
100 Talk to Imogene COMPLETED
150 Travel to HopeTech  COMPLETED
110 (Optional) Wear a Suit or Security Guard Uniform COMPLETED
200 Retrieve the Security Keycard COMPLETED
300 Return to Imogene at Ryujin Tower COMPLETED
 
== Sowing Discord ==
( Instance: 1 )
100 Talk to Imogene COMPLETED
200 Influence Zola Adisa Against Infinity LTD COMPLETED
300 Influence Arthur Cruz Against Infinity LTD COMPLETED
400 (Optional) Read the Dossiers COMPLETED
500 Switch Nina Hart's Presentation COMPLETED
550 (Optional) Find a Way into Nina's Suite COMPLETED
600 Return to Imogene at Ryujin Tower COMPLETED
 
== Accidents Happen ==
( Instance: 1 )
100 Talk to Imogene COMPLETED
150 Travel to HopeTech  COMPLETED
 
== Top of the L.I.S.T. ==
( Instance: 1 )
190 Sell a habitable planet survey data to LIST DISPLAYED
 
== Accidents Happen ==
( Instance: 1 )
200 Plant the ARC Device COMPLETED
300 Return to Imogene at Ryujin Tower COMPLETED
 
== Maintaining the Edge ==
( Instance: 1 )
100 Talk to Imogene COMPLETED
150 Travel to Trident Luxury Lines Staryard COMPLETED
200 Obtain the Schematic COMPLETED
300 Return to Imogene at Ryujin Tower COMPLETED
 
== Top Secrets ==
( Instance: 1 )
100 Meet Ularu Chen DORMANT
110 Talk to Maeve COMPLETED
120 Wait for Ularu Chen COMPLETED
130 Meet Ularu Chen COMPLETED
170 Travel to Cydonia on Mars in the Sol System COMPLETED
200 Rendezvous with Simon Ryczek COMPLETED
300 Find The Datura in Saturn's Orbit COMPLETED
500 Talk to Malai Liskova COMPLETED
525 Dock with the Datura COMPLETED
400 Destroy the Datura DORMANT
600 Retrieve Malai's Gun for Simon COMPLETED
700 Return to Simon in Cydonia COMPLETED
800 Read Stanley McMillan's File COMPLETED
900 Listen to the Audio File COMPLETED
1000 Retrieve the Project Dominion Files COMPLETED
1100 Return to Ularu at Ryujin Tower COMPLETED
1200 Follow Ularu COMPLETED
1300 Talk to Masako Imada COMPLETED
 
== Background Checks ==
( Instance: 1 )
100 Talk to Dalton Fiennes COMPLETED
200 Meet Nyx at Madame Sauvage's Place COMPLETED
300 Talk to Nyx COMPLETED
375 Find a way into Ryujin Tower COMPLETED
400 Run Nyx's Program on Ularu's Computer COMPLETED
405 (Optional) Equip the Arboron Novablast Disrupter COMPLETED
575 Exit Ryujin Tower COMPLETED
600 Meet Nyx at his Apartment COMPLETED
700 Talk to Nyx COMPLETED
800 Wait for Nyx COMPLETED
900 Talk to Nyx COMPLETED
1000 Return to Dalton at Ryujin Tower COMPLETED
1100 Talk to Dalton COMPLETED
1200 Wait for Dalton COMPLETED
1300 Talk to Dalton COMPLETED
 
== Guilty Parties ==
( Instance: 1 )
100 Talk to Imogene on the Operations Floor DORMANT
300 Talk to Yuko COMPLETED
400 (Optional) Talk to Dalton on the Executive Offices Floor COMPLETED
600 Find Imogene in the Seokguh Syndicate Hideout DORMANT
500 (Optional) Talk to Benjamin Bayu COMPLETED
 
== Superfan ==
( Instance: 1 )
10 Speak to Myka COMPLETED
 
== Guilty Parties ==
( Instance: 1 )
610 (Optional) Head to Frankie's Grab & Go COMPLETED
620 (Optional) Talk to Franchesca "Frankie" Moore COMPLETED
660 Find Imogene COMPLETED
800 Confront Imogene COMPLETED
900 Decide Imogene's Fate COMPLETED
1200 Return to Dalton at Ryujin Tower on the Executive Offices Floor COMPLETED
1000 (Optional) Confront Ularu on the Executive Offices Floor COMPLETED
1300 (Optional) Talk to Yuko COMPLETED
 
== The Key Ingredient ==
( Instance: 1 )
100 Meet Masako in Veena Kalra's Office COMPLETED
200 Talk to Masako COMPLETED
500 Travel to Carinae III-a COMPLETED
600 Land at CM Station RC-1 COMPLETED
850 Enter the CM Station RC-1 Mine COMPLETED
700 Find the Rothicite Shipment COMPLETED
750 Discover What Happened at CM Station RC-1 COMPLETED
751 Read the Contract COMPLETED
900 Travel to the Clinic in the Narion System COMPLETED
1000 Dock at the Clinic COMPLETED
1100 Enter the Clinic COMPLETED
1200 Retrieve the Rothicite Shipment COMPLETED
1250 Discover What Infinity LTD is Doing at the Clinic COMPLETED
1400 Return to Veena at Ryujin Tower COMPLETED
 
== Divided Loyalties ==
( Instance: 1 )
10 Reach Akila City COMPLETED
20 Talk to Andreja at Aggie's COMPLETED
 
== Delivery Truck ==
( Instance: 1 )
402 Deliver Star Parcel Package to Cydonia COMPLETED
403 Deliver Star Parcel Package to Neon COMPLETED
404 Deliver Star Parcel Package to New Atlantis COMPLETED
 
== Divided Loyalties ==
( Instance: 1 )
30 Ask about Eren Bascolm COMPLETED
40 Talk to Andreja COMPLETED
45 Travel to Hyla II COMPLETED
50 Talk to Andreja COMPLETED
55 Find Eren Bascolm's camp COMPLETED
60 Defeat the Zealots COMPLETED
70 Follow Andreja COMPLETED
75 Find information about Jaeda COMPLETED
80 Talk to Andreja COMPLETED
85 Travel to The Den COMPLETED
90 Ask around about Jaeda COMPLETED
100 Reach Jaeda's last known location COMPLETED
120 Hail Jaeda's ship COMPLETED
130 Defeat the Zealots COMPLETED
140 Hail Jaeda's ship COMPLETED
150 Board Jaeda's ship COMPLETED
160 Wait for Andreja COMPLETED
170 Talk to Jaeda COMPLETED
200 Return to your Ship COMPLETED
220 Defeat the Zealot Captain COMPLETED
300 Talk to Andreja COMPLETED
305 Travel to the Derelict Station COMPLETED
310 Follow Andreja COMPLETED
320 Talk to Andreja COMPLETED
340 Follow Andreja COMPLETED
360 Talk to Tomisar COMPLETED
370 Talk to Andreja COMPLETED
500 Accompany Andreja to The Lodge COMPLETED
 
== Quest for spawning and scenes for settlers to sign up for LIST ==
( Instance: 12 )
100 Sign up settler for the LIST DORMANT
 
== Divided Loyalties ==
( Instance: 1 )
600 Talk to Andreja COMPLETED
110 Find Jaeda COMPLETED
210 Hail the Zealot Captain COMPLETED
375 Kill Tomisar COMPLETED
400 Talk to Andreja COMPLETED
 
== Failure to Communicate ==
( Instance: 1 )
1000 Report back to Alban Lopez on Altair III-c COMPLETED
1100 Clear out Spacer ships orbiting Altair IV COMPLETED
1200 Clear the Spacers from the Retrofitted Starstation COMPLETED
1400 Talk with Alban Lopez COMPLETED
1210 (Optional) Talk with Jacquelyn Lemaire COMPLETED
1250 (Optional) Kill Alban Lopez COMPLETED
1500 Talk with Jacquelyn Lemaire COMPLETED
 
== The Key Ingredient ==
( Instance: 1 )
1500 Talk to Veena COMPLETED
1600 Talk to Masako COMPLETED
 
== Sabotage ==
( Instance: 1 )
100 Meet Dalton in His Office COMPLETED
200 Talk to Dalton COMPLETED
300 Take a Seat in the Conference Room COMPLETED
350 Wait for Dalton to Arrive COMPLETED
400 Listen to the Meeting COMPLETED
500 Meet Veena in the Neuroamp Division COMPLETED
600 Talk to Veena COMPLETED
700 Lie Down on the Operating Table COMPLETED
800 Talk to Veena COMPLETED
850 Go to the Observation Deck COMPLETED
900 Use Manipulation on DeMarcus to find a way to open the door COMPLETED
999 Wait for Veena and DeMarcus COMPLETED
1000 Talk to Veena COMPLETED
1100 Talk to Masako in Her Office COMPLETED
1150 Talk to Dalton COMPLETED
1155 Wait for Dalton COMPLETED
1180 Talk to Dalton COMPLETED
1200 Meet Ularu in Her Office COMPLETED
1250 Talk to Ularu COMPLETED
1300 Travel to New Atlantis COMPLETED
 
== Drydock Blues: Taiyo Astroneering Store ==
( Instance: 9 )
50 Speak to Vern Sobakin COMPLETED
100 Deliver 499 Chlorine for Vern Sobakin DISPLAYED
 
== Sabotage ==
( Instance: 1 )
1400 Enter the Infinity LTD Corporate Headquarters COMPLETED
1410 Use the Roof Access Entrance COMPLETED
1420 Check In at the Infinity LTD Front Desk COMPLETED
1430 Meet Aelys Ortiz  COMPLETED
1435 Have a Seat in the Waiting Area COMPLETED
1440 Wait for Aelys to Leave COMPLETED
1500 Run the Program on Lucas' Computer  COMPLETED
1560 Find a way into Research and Development COMPLETED
1450 (Optional) Sabotage the Heating System COMPLETED
1490 (Optional) Manipulate the guard to turn off the fan COMPLETED
1510 (Optional) Talk to Lucas Drexler DORMANT
1600 Run the Program on Faye's Computer  COMPLETED
1700 Obtain the Neuroamp Prototype  COMPLETED
1750 Find an R&D ID Card COMPLETED
1800 Deliver the Evidence to David Barron at the SSNN Field Office COMPLETED
1900 Report to Masako at Ryujin Tower COMPLETED
2000 Listen to the Conversation COMPLETED
2100 Talk to Masako COMPLETED
 
== Executive Level ==
( Instance: 1 )
200 Lobby Board Members  (7/7) COMPLETED
201 For Infinity LTD (Y:6 N:1) DORMANT
202 For Internal Neuroamp (Y:1 N:5) DORMANT
203 For Ularu as CEO  (Y:0 N:0) DORMANT
900 Attend the Meeting COMPLETED
925 Wait for Everyone to Arrive COMPLETED
1000 Talk to Masako COMPLETED
 
== [Misc Pointer for Mission Board] ==
( Instance: 0 )
100 Talk to Imogene COMPLETED
200 Examine the Mission Board COMPLETED
 
== Further Into the Unknown ==
( Instance: 1 )
20 Find the Artifact on Aranae IV-d COMPLETED
30 Find the Artifact on Worthless COMPLETED
 
== Commitment: Andreja ==
( Instance: 0 )
10 Travel to Shoza II COMPLETED
 
== Further Into the Unknown ==
( Instance: 1 )
50 Add the Artifacts to the Collection COMPLETED
 
== Power from Beyond ==
( Instance: 9 )
3 Talk to Vladimir to Locate Temples COMPLETED
 
== Further Into the Unknown ==
( Instance: 1 )
60 Talk to Matteo COMPLETED
70 Talk to Vladimir COMPLETED
 
== Commitment: Andreja ==
( Instance: 0 )
20 Land on Shoza II COMPLETED
30 Reach the Location COMPLETED
40 Talk to Andreja COMPLETED
50 Find Andreja's Gift COMPLETED
60 Talk to Andreja COMPLETED
 
== Short Sighted ==
( Instance: 1 )
10 Go to the Eye COMPLETED
100 Check in on Everyone COMPLETED
140 Weld the Panel COMPLETED
110 Install the Parts COMPLETED
120 Rewire the Research Lab COMPLETED
130 Test the Computer COMPLETED
150 Use the Wrenches COMPLETED
200 Talk to Vladimir COMPLETED
 
== No Sudden Moves ==
( Instance: 1 )
10 Talk to Vladimir COMPLETED
20 Travel to the Scow COMPLETED
 
== Power from Beyond ==
( Instance: 9 )
10 Acquire the Power on Procyon IV COMPLETED
 
== Power from Beyond ==
( Instance: 9 )
15 Follow Distortions on the Scanner COMPLETED
 
== Power from Beyond ==
( Instance: 9 )
5 Use the Eye to Locate Temples COMPLETED
 
== Power from Beyond ==
( Instance: 9 )
7 Obtain a Ship with a 28LY Grav Jump Range COMPLETED
 
== Power from Beyond ==
( Instance: 10 )
3 Talk to Vladimir to Locate Temples COMPLETED
 
== No Sudden Moves ==
( Instance: 1 )
30 Board the Scow COMPLETED
32 (Optional) Talk Your Way In COMPLETED
34 (Optional) Disable the Engines COMPLETED
40 Find Captain Petrov COMPLETED
50 Access Petrov's Vault COMPLETED
52 (Optional) Pickpocket the Key to the Vault COMPLETED
54 (Optional) Pick the Vault Door Lock COMPLETED
56 (Optional) Fight Petrov COMPLETED
57 Follow Petrov COMPLETED
200 (Optional) Find the Thin Walls COMPLETED
210 (Optional) Use a Cutter on the Four Bolts COMPLETED
220 (Optional) Find the Wall Behind the Vault COMPLETED
230 (Optional) Use a Cutter on the Four Bolts COMPLETED
250 (Optional) Release the Zoo Aliens COMPLETED
60 Steal the Artifact COMPLETED
70 Leave the Scow COMPLETED
100 Add the Artifact to the Collection COMPLETED
 
== Quest for spawning and scenes for settlers to sign up for LIST ==
( Instance: 13 )
100 Sign up settler for the LIST DORMANT
 
== High Price to Pay ==
( Instance: 1 )
50 Talk to Noel COMPLETED
 
== A Light in the Darkness ==
( Instance: 1 )
200 Deliver news about the Scow and Captain Petrov COMPLETED
 
== High Price to Pay ==
( Instance: 1 )
55 Defend the Lodge COMPLETED
60 Go to the Eye COMPLETED
80 Check on Walter COMPLETED
120 Find Everyone on the Eye COMPLETED
300 Go to the Lodge COMPLETED
310 Find Everyone in the Lodge COMPLETED
320 Find Noel COMPLETED
330 Escape to Your Ship COMPLETED
340 Initiate Take-Off COMPLETED
350 Go to the Eye COMPLETED
360 Talk to Vladimir COMPLETED
150 Talk to Vladimir COMPLETED
500 Build the Armillary on Your Ship COMPLETED
510 Build the Armillary at an Outpost COMPLETED
520 Return to the Lodge COMPLETED
 
== Companion's Belongings ==
( Instance: 1 )
10 Retrieve Sarah Morgan's Belongings COMPLETED
 
== High Price to Pay ==
( Instance: 1 )
530 Talk to Matteo COMPLETED
 
== Overdesigned ==
( Instance: 1 )
100 Go to Stroud-Eklund Staryard DISPLAYED
 
== High Price to Pay ==
( Instance: 1 )
90 Hold off the Hunter COMPLETED
100 Escape the Lodge COMPLETED
105 Go to Your Ship COMPLETED
110 Go to the Eye COMPLETED
140 Return to Noel COMPLETED
 
== Unity ==
( Instance: 1 )
10 Talk to Keeper Aquilus COMPLETED
20 Enter the Sanctum Universum COMPLETED
30 Talk to Keeper Aquilus COMPLETED
40 Talk to the House of Enlightenment COMPLETED
50 Talk to the Va'ruun Prisoner COMPLETED
60 Return to Keeper Aquilus COMPLETED
70 Go to Indum II COMPLETED
 
== Power from Beyond ==
( Instance: 10 )
10 Acquire the Power on Procyon-B I COMPLETED
 
== Power from Beyond ==
( Instance: 10 )
15 Follow Distortions on the Scanner COMPLETED
 
== Power from Beyond ==
( Instance: 10 )
5 Use the Eye to Locate Temples COMPLETED
 
== Power from Beyond ==
( Instance: 10 )
7 Obtain a Ship with a 28LY Grav Jump Range COMPLETED
 
== Power from Beyond ==
( Instance: 11 )
3 Talk to Vladimir to Locate Temples COMPLETED
 
== Power from Beyond ==
( Instance: 11 )
10 Acquire the Power on Skink COMPLETED
 
== Power from Beyond ==
( Instance: 11 )
15 Follow Distortions on the Scanner COMPLETED
 
== Power from Beyond ==
( Instance: 11 )
5 Use the Eye to Locate Temples COMPLETED
 
== Power from Beyond ==
( Instance: 11 )
7 Obtain a Ship with a 28LY Grav Jump Range COMPLETED
 
== The Devils You Know ==
( Instance: 1 )
560 Transfer the ID to Vae Victis COMPLETED
580 Speak to Vae Victis COMPLETED
 
== War Relics ==
( Instance: 1 )
100 Report to Hadrian COMPLETED
 
== The Devils You Know ==
( Instance: 1 )
445 (Optional) Search the ship for a cabin key COMPLETED
 
== War Relics ==
( Instance: 1 )
200 Follow Hadrian COMPLETED
300 Search for Kaiser on Niira COMPLETED
305 Search for Kaiser in the mech fields COMPLETED
306 (Optional) Gather information on Kaiser's location from Gel COMPLETED
400 Proceed to the Syracuse COMPLETED
410 Follow the beacon COMPLETED
420 Speak to Kaiser COMPLETED
430 Remove the creatures from Kaiser COMPLETED
450 Find a MicroCell COMPLETED
460 Purchase a MicroCell from Gel COMPLETED
461 (Optional) Talk to Gel about making MicroCells COMPLETED
481 (Optional) Recover a MicroCell Conductor Array COMPLETED
482 (Optional) Recover a MicroCell Power Source COMPLETED
483 (Optional) Recover MicroCell Shielding COMPLETED
485 (Optional) Fabricate a MicroCell COMPLETED
500 Return the MicroCell to Kaiser COMPLETED
510 Speak with Kaiser COMPLETED
550 Follow Kaiser to the mission site COMPLETED
560 Speak to Kaiser COMPLETED
598 (Optional) Eliminate Ecliptic forces without being detected COMPLETED
597 (Optional) Find a way to seal Unit 99’s restraints COMPLETED
600 Clear the Ecliptic base COMPLETED
660 Kill Unit 99 COMPLETED
599 Speak to Kaiser COMPLETED
700 Follow Kaiser COMPLETED
710 Speak to Kaiser COMPLETED
800 Return to the Red Devils HQ COMPLETED
 
== Hostile Intelligence ==
( Instance: 1 )
100 Follow Hadrian COMPLETED
 
== War Relics ==
( Instance: 1 )
465 (Optional) Read Kaiser's Schematics Catalogue COMPLETED
470 (Optional) Collect parts and fabricate a MicroCell COMPLETED
 
== Hostile Intelligence ==
( Instance: 1 )
110 Speak to Hadrian COMPLETED
150 Proceed to Londinion COMPLETED
151 (Optional) Visit Lt. Azevedo to purchase new gear COMPLETED
165 Proceed to the Command Post COMPLETED
175 Discuss the exploration plan COMPLETED
250 Inform Hadrian you're ready to move out COMPLETED
200 (Optional) Collect your gear COMPLETED
295 Allow Kaiser to unlock the airlock COMPLETED
300 Proceed into the city COMPLETED
315 Locate the Aceles gene samples COMPLETED
318 Clear the processing plant COMPLETED
320 Locate the Aceles gene samples COMPLETED
325 (Optional) Explore the base cache COMPLETED
335 (Optional) Access the supply caches COMPLETED
331 Collect the Aceles samples COMPLETED
381 Eliminate the remaining hostiles COMPLETED
369 Give Kaiser the samples COMPLETED
383 Locate the next sample COMPLETED
370 Proceed to the steam tunnels COMPLETED
415 Observe the Lazarus Plant with Hadrian COMPLETED
426 Speak to Hadrian COMPLETED
450 Kill the Terrormorph COMPLETED
460 Collect a tissue sample from the Terrormorph COMPLETED
470 Give the sample to Kaiser COMPLETED
480 Discuss the findings with the team COMPLETED
490 Follow Kaiser COMPLETED
500 Restore power to the spaceport access hatch COMPLETED
507 Initiate the system reboot COMPLETED
510 Listen to the playback COMPLETED
610 Listen to the playback COMPLETED
617 Collect the recording COMPLETED
620 Speak to Hadrian COMPLETED
513 Return to Kaiser COMPLETED
515 Find the final sample COMPLETED
525 Secure the final sample COMPLETED
526 (Optional) Power up the spaceport radar dishes COMPLETED
530 Collect the final sample COMPLETED
531 Secure the Anomalous Sample COMPLETED
660 Return to Forward Base 441 COMPLETED
700 Discuss your findings COMPLETED
 
== A Legacy Forged ==
( Instance: 1 )
100 Speak to Vae Victis COMPLETED
 
== Hostile Intelligence ==
( Instance: 1 )
330 Follow Kaiser COMPLETED
340 Collect the manifest COMPLETED
350 Locate the Aceles gene samples COMPLETED
355 (Optional) Collect the remaining manifests COMPLETED
360 Find the next sample COMPLETED
365 Return to the plant COMPLETED
367 Speak to Hadrian COMPLETED
371 Head towards the next sample COMPLETED
560 Restore power to the containment chamber COMPLETED
580 Reactivate the fuel tanks COMPLETED
590 Return to Hadrian COMPLETED
630 Return to Kaiser COMPLETED
650 Collect the final sample COMPLETED
 
== A Legacy Forged ==
( Instance: 1 )
200 Speak to Hadrian and Percival COMPLETED
300 Address the Cabinet COMPLETED
 
== Quest for spawning and scenes for settlers to sign up for LIST ==
( Instance: 14 )
100 Sign up settler for the LIST DORMANT
 
== Misc pointer to Tuala for UC09 Post-Quest ==
( Instance: 0 )
100 Speak to Commander Tuala COMPLETED
 
== Misc pointer to Percival for UCR04 ==
( Instance: 0 )
100 Speak to Percival COMPLETED
 
== Misc pointer to Hadrian Elite Crew ==
( Instance: 0 )
100 Speak to Hadrian about joining you DISPLAYED
 
== Apex Predator ==
( Instance: 1 )
111 (Optional) Approach targets to harvest non-lethally DISPLAYED
100 Collect samples on Muphrid V-b (0/4) DISPLAYED
 
== Re-Re-Application ==
( Instance: 1 )
100 Talk to Bastien Graff COMPLETED
 
== Misc pointer to player penthouse in New Atlantis ==
( Instance: 0 )
100 Visit your new penthouse in New Atlantis COMPLETED
 
== Re-Re-Application ==
( Instance: 1 )
15 Talk to Bastien Graff COMPLETED
 
== [BE - Objective Quest] ==
( Instance: 10 )
30 Take over the ship DORMANT
 
== [BE - Objective Quest] ==
( Instance: 10 )
40 Leave the ship COMPLETED
 
== [BE - Objective Quest] ==
( Instance: 10 )
20 Kill the crew COMPLETED
 
== First to Fight, First to Die ==
( Instance: 1 )
300 Land at the Freestar mech factory COMPLETED
400 Investigate the Freestar mech factory COMPLETED
500 Confront Paxton Hull COMPLETED
600 Defeat Paxton Hull COMPLETED
650 Defeat the First Mercenaries COMPLETED
700 Talk to Paxton Hull COMPLETED
800 Kill Paxton Hull COMPLETED
130 Join the debriefing COMPLETED
 
== The Hammer Falls ==
( Instance: 1 )
50 Go to HopeTown COMPLETED
 
== Quest for spawning and scenes for settlers to sign up for LIST ==
( Instance: 15 )
100 Sign up settler for the LIST DORMANT
 
== The Hammer Falls ==
( Instance: 1 )
100 Confront Ron Hope COMPLETED
200 Kill Ron Hope COMPLETED
220 Eliminate Hope's Security COMPLETED
250 Talk to Birgit McDougall COMPLETED
 
== A Light in the Darkness ==
( Instance: 1 )
500 Deliver news about Ron Hope's death COMPLETED
 
== The Hammer Falls ==
( Instance: 1 )
300 Talk to Marshal Blake COMPLETED
 
== Beer Run ==
( Instance: 1 )
25 Talk to Sarah Filburn COMPLETED
 
== Deep Cover ==
( Instance: 1 )
35 Speak to the SysDef Guard COMPLETED
36 Follow the SysDef Guard COMPLETED
40 Speak to Commander Ikande in the Operations Center COMPLETED
50 Speak to Lieutenant Toft COMPLETED
60 Proceed to Cydonia COMPLETED
 
== Burden of Proof ==
( Instance: 1 )
100 Search for Evidence DISPLAYED
 
== Deep Cover ==
( Instance: 1 )
70 Speak to Saoirse Bowden at the Trade Authority COMPLETED
80 Speak to Adler Kemp at the Broken Spear COMPLETED
90 Recover Karl Fielding's Debt COMPLETED
110 Return to Adler Kemp COMPLETED
 
== Rook Meets King ==
( Instance: 1 )
100 Rendezvous with the Astraea at Europa COMPLETED
 
== Quest for spawning and scenes for settlers to sign up for LIST ==
( Instance: 16 )
100 Sign up settler for the LIST DORMANT
 
== Rook Meets King ==
( Instance: 1 )
200 Hail the Astraea COMPLETED
300 Find the Ragana Near Enceladus COMPLETED
310 Wait for the Ragana to arrive COMPLETED
400 Hail the Ragana COMPLETED
500 Attack the Ragana COMPLETED
600 Convince the Ragana to kill Austin Rake COMPLETED
605 Find a way to spare Austin Rake COMPLETED
550 Board the Ragana COMPLETED
560 Talk to Dmitri COMPLETED
501 Destroy the Ragana DORMANT
610 Get the Medical Supplies COMPLETED
700 Return to Naeva in Europa's Orbit COMPLETED
750 Fend off the Ecliptic Fighters COMPLETED
800 Hail the Astraea COMPLETED
1000 Report to the Vigilance COMPLETED
900 Jettison the Medical Supplies COMPLETED
1100 Board the Vigilance COMPLETED
1150 Follow the SysDef Cadet COMPLETED
1200 Speak to Commander Ikande COMPLETED
1400 Escape the Vigilance COMPLETED
1500 Travel to the Key COMPLETED
1600 Dock at the Key COMPLETED
1700 Speak to Naeva COMPLETED
1800 Follow Naeva COMPLETED
1900 Speak to Shinya Voss COMPLETED
2000 Speak to Delgado COMPLETED
 
== Echoes of the Past ==
( Instance: 1 )
10 Proceed to the surface of Suvorov COMPLETED
 
== UC Vigilance Dialogue ==
( Instance: 1 )
85 Find information on the conditions in the Lock COMPLETED
 
== Echoes of the Past ==
( Instance: 1 )
20 Speak to Delgado COMPLETED
30 Meet Delgado at the entrance to the Lock COMPLETED
40 Speak to Delgado at the outer door to the Lock COMPLETED
45 Wait for Delgado to unseal the Lock COMPLETED
50 Continue exploring the Lock COMPLETED
60 Secure the area COMPLETED
70 Speak to Delgado COMPLETED
80 Proceed to the Control Room COMPLETED
90 Speak to Mathis COMPLETED
110 Use the intercom to speak to Delgado COMPLETED
120 Open the doors for Delgado COMPLETED
130 Proceed to D-Block COMPLETED
140 Enter D-Block's Guard Tower COMPLETED
150 Speak to Mathis COMPLETED
171 (Optional) Locate the cache in Cell D-02-106 DORMANT
170 Locate Jasper Kryx's Cell COMPLETED
176 Open Cells in D-Block Section 3 COMPLETED
180 Search Kryx's Cell for information COMPLETED
185 Listen to Kryx's Log COMPLETED
190 Search Carter's Locker COMPLETED
195 Listen to Carter's Slate COMPLETED
 
== Burden of Proof ==
( Instance: 1 )
200 Return Evidence to Lt. Toft DORMANT
 
== Echoes of the Past ==
( Instance: 1 )
200 Access Kryx's escape tunnel COMPLETED
210 Proceed to the Shuttle Bay COMPLETED
220 Use the intercom to speak to Delgado COMPLETED
240 Speak to Mathis COMPLETED
250 Enter the Shuttle Bay COMPLETED
260 Secure the Shuttle Bay COMPLETED
310 Use the Shuttle to exit the Lock COMPLETED
315 Travel to the Key COMPLETED
320 Speak to Delgado COMPLETED
 
== Doctor's Orders ==
( Instance: 1 )
100 Speak to Gennady Ayton at the Clinic FAILED
 
== Echoes of the Past ==
( Instance: 1 )
340 Speak to Mathis COMPLETED
365 Meet Naeva at her spot in the Last Nova COMPLETED
370 Speak to Naeva at the Last Nova COMPLETED
410 Report mission progress on the UC Vigilance COMPLETED
420 Speak to Commander Ikande COMPLETED
 
== Breaking the Bank ==
( Instance: 1 )
15 Travel to the Siren of the Stars COMPLETED
20 Dock with the Siren of the Stars COMPLETED
30 Speak with Evgeny Rokov COMPLETED
40 Ask Society Patrons about Larry Dumbrosky COMPLETED
50 (Optional) Kill Larry Dumbrosky DORMANT
600 (Optional) Discover location of the ES Award COMPLETED
610 (Optional) Speak to Sheila about the ES Award COMPLETED
620 (Optional) Discover how to access the Purser's safe COMPLETED
630 (Optional) Acquire the ES Award Claim ID COMPLETED
640 (Optional) Steal the ES Award DORMANT
60 Speak to Klaudia Swist COMPLETED
70 Speak to Gabriel Vera COMPLETED
90 Return to Evgeny Rokov COMPLETED
100 Speak to Chief Engineer Sandin COMPLETED
110 Gain entry to the Life Support System COMPLETED
120 Disable the Life Support System COMPLETED
130 Confront Gabriel Vera in his cabin COMPLETED
150 Confront Larry Dumbrosky COMPLETED
140 Recover embezzlement scheme evidence COMPLETED
170 Return to Evgeny Rokov COMPLETED
310 Proceed to New Atlantis COMPLETED
330 Enter the GalBank Archives COMPLETED
 
== Quest for spawning and scenes for settlers to sign up for LIST ==
( Instance: 17 )
100 Sign up settler for the LIST DORMANT
 
== Breaking the Bank ==
( Instance: 1 )
340 Deal with the GalBank Guard COMPLETED
346 Enter the Archives COMPLETED
350 Deal with the Ecliptic ambush COMPLETED
370 Recover information regarding GalBank's lost transport COMPLETED
410 Proceed to the Key COMPLETED
420 Speak to Delgado COMPLETED
 
== [Misc Objective Pointer] ==
( Instance: 1 )
100 Speak to Bog FAILED
 
== Breaking the Bank ==
( Instance: 1 )
450 Follow Naeva COMPLETED
460 Speak to Naeva COMPLETED
680 (Optional) Give the ES Award to Naeva COMPLETED
510 Report mission progress on the UC Vigilance COMPLETED
520 Speak to Commander Ikande COMPLETED
 
== The Best There Is ==
( Instance: 1 )
100 Meet Naeva and Jasmine on the Key COMPLETED
200 Speak to Naeva COMPLETED
300 Meet Huan Daiyu in New Atlantis COMPLETED
400 Speak to Huan Daiyu COMPLETED
500 Meet Huan on the Jade Swan COMPLETED
 
== The Boot ==
( Instance: 1 )
50 Travel to the Den COMPLETED
 
== A Shipment for Salinas ==
( Instance: 1 )
20 Pick up the package from Red Mile DISPLAYED
 
== Suspicious Activities ==
( Instance: 1 )
10 Speak to Theresa Mason  COMPLETED
 
== The Best There Is ==
( Instance: 1 )
700 Travel with Huan to SY-920 COMPLETED
755 Wait for the Jade Swan to dock COMPLETED
800 Speak to Huan COMPLETED
900 Board SY-920 COMPLETED
810 Gain access to the barracks to find a uniform COMPLETED
820 (Optional) Speak to Huan DORMANT
1020 Find an Ensign Uniform COMPLETED
795 (Optional) Eavesdrop on the marines DORMANT
796 (Optional) Talk to Elijah DORMANT
797 (Optional) Retrieve the maintenance key DORMANT
1051 (Optional) Sabotage and deliver the sandwich to the guard DORMANT
1000 Enter the Command Bay COMPLETED
1034 (Optional) Remove the uniform from the locker COMPLETED
1033 (Optional) Steal the uniform from the checkpoint DORMANT
1035 (Optional) Pick up the uniform in the lounge DORMANT
1041 (Optional) Find a ventilation shaft DORMANT
1200 Find information on the ComSpike COMPLETED
1014 (Optional) Find an intercom to speak to Huan COMPLETED
1016 (Optional) Obtain a Clearance Code COMPLETED
1210 Upgrade security clearance for Engineering Bay 4 COMPLETED
1400 Go to Engineering Bay 4 COMPLETED
1013 (Optional) Report Huan to Dr. Vogel DORMANT
1050 (Optional) Speak to Commander Natara to Reduce Exposure DORMANT
1600 Locate the ComSpike COMPLETED
1610 Arrange a test flight on the Prototype COMPLETED
1620 (Optional) Betray Huan to Divert Attention DORMANT
1611 (Optional) Find a route to bypass the checkpoint COMPLETED
1710 (Optional) Find a Pilot Uniform COMPLETED
1800 Escape SY-920 in the Prototype Ship COMPLETED
1730 (Optional) Schedule a Test Flight DORMANT
1900 Return to The Key COMPLETED
2000 Speak to Huan at the Last Nova COMPLETED
1990 Travel to the Operations Center COMPLETED
2100 Speak to Delgado COMPLETED
2200 Proceed to the UC Vigilance COMPLETED
2300 Speak to Commander Ikande COMPLETED
 
== Absolute Power ==
( Instance: 1 )
10 Travel to Neon COMPLETED
20 Proceed to Madame Sauvage's Place COMPLETED
30 Speak to Estelle Vincent COMPLETED
40 Ask Myka about Ayumi Komiko’s whereabouts COMPLETED
50 (Optional) Locate Evidence to Extort Ayumi Komiko COMPLETED
70 (Optional) Use Evidence to Extort Ayumi Komiko COMPLETED
 
== Superfan ==
( Instance: 1 )
20 Recover the music slate DISPLAYED
30 (Optional) Convince Stratos to hand over the slate DISPLAYED
 
== Absolute Power ==
( Instance: 1 )
45 Acquire the Generdyne Passkey from Ayumi Komiko COMPLETED
60 (Optional) Kill Ayumi Komiko DORMANT
80 Enter Generdyne through the Storage Entrance COMPLETED
100 Proceed to Generdyne's Power Core COMPLETED
110 Obtain the Conduction Grid Data COMPLETED
120 Locate Breyson Bayu COMPLETED
130 Gain the Encryption Cipher from Breyson Bayu COMPLETED
150 Decrypt the Conduction Grid Data COMPLETED
160 Upload Estelle's virus COMPLETED
170 Return to Estelle COMPLETED
180 Speak to Benjamin Bayu COMPLETED
190 Locate Estelle at Madame Sauvage's Place COMPLETED
200 Speak to Estelle COMPLETED
300 Return to the Key COMPLETED
320 Speak to Naeva COMPLETED
 
== Eye of the Storm ==
( Instance: 1 )
10 Speak with Delgado COMPLETED
20 Install both the ComSpike Module and the Conduction Grid Module COMPLETED
110 Proceed to the UC Vigilance COMPLETED
120 Speak to Commander Ikande COMPLETED
210 Proceed to Bannoc IV COMPLETED
220 Board the GalBank Transport "Legacy" COMPLETED
230 Locate the Vault Control Center COMPLETED
240 Locate a Transfer Module COMPLETED
250 Open the Vault Door COMPLETED
260 (Optional) Access the CredTank COMPLETED
270 Retrieve Jasper Kryx's Possessions COMPLETED
280 Listen to Kryx's Recording COMPLETED
290 Reroute Ship's Power COMPLETED
300 Access the Legacy's Credit Reserves COMPLETED
360 Download the Legacy's Credit Reserves COMPLETED
370 Wait for the Transfer Process to Complete COMPLETED
380 Remove Transfer Modules and Data Core COMPLETED
400 Escape the Legacy COMPLETED
410 Bring Kryx's Legacy to the UC Vigilance COMPLETED
420 Bring Kryx's Legacy to The Key COMPLETED
 
== Legacy's End ==
( Instance: 1 )
10 Defeat the Crimson Fleet Scouts COMPLETED
 
== Doctor's Orders ==
( Instance: 1 )
50 Retrieve the Supply Storage Key FAILED
200 Get the Medical Supplies FAILED
300 Return to Samina at the Key FAILED
 
== Legacy's End ==
( Instance: 1 )
20 Board the Vigilance COMPLETED
30 Speak to Commander Ikande COMPLETED
40 Disable Defensive Battery Alpha COMPLETED
50 Disable Defensive Battery Beta COMPLETED
60 Disable Defensive Battery Gamma COMPLETED
70 Join the Battle at the Key COMPLETED
80 Destroy the Crimson Fleet Defenders COMPLETED
90 Board the Key COMPLETED
100 Reach the Operations Center COMPLETED
105 Bypass the Security Doors COMPLETED
110 Open the Last Nova's Doors COMPLETED
120 Speak to Shinya Voss COMPLETED
125 (Optional) Deactivate Shinya Voss' Bomb COMPLETED
130 Confront Delgado COMPLETED
190 Return to the Vigilance COMPLETED
 
== Osaka Landmark Quest ==
( Instance: 1 )
100 Visit the Osaka Landmark on Earth COMPLETED
 
== Legacy's End ==
( Instance: 1 )
200 Speak to Commander Ikande COMPLETED
 
== A Light in the Darkness ==
( Instance: 1 )
600 Deliver news about SysDef's attack on the Key COMPLETED
8000 Talk with Nadia Muffaz COMPLETED
 
== The Boot ==
( Instance: 1 )
60 Place the boots in a crate COMPLETED
 
== Unity ==
( Instance: 1 )
72 Go to "Pilgrim's Rest" COMPLETED
74 Search for Clues on "Unity" COMPLETED
75 Access the Locked Room COMPLETED
77 Search the Room COMPLETED
79 Read "Pilgrim's Final Writing" COMPLETED
80 Find the "Scorpion's Sting" on Hyla II COMPLETED
90 Go to the Last Star of the Scorpius Constellation COMPLETED
 
== [BE - Objective Quest] ==
( Instance: 11 )
30 Take over the ship COMPLETED
40 Leave the ship COMPLETED
20 Kill the crew COMPLETED
 
== In Their Footsteps ==
( Instance: 1 )
10 Hail the Scorpius COMPLETED
15 Dock with the Scorpius COMPLETED
20 Talk to the Hunter COMPLETED
30 Leave the Scorpius COMPLETED
40 (Optional) Talk to the Emissary COMPLETED
50 (Optional) Talk to the Hunter COMPLETED
60 Talk to the Emissary COMPLETED
 
== Unearthed ==
( Instance: 1 )
5 Go to Nova Galactic Research Station COMPLETED
 
== Final Glimpses ==
( Instance: 1 )
5 Return to the Lodge COMPLETED
 
== Unearthed ==
( Instance: 1 )
10 Investigate the Research Station COMPLETED
15 Play the Recording on the Roof COMPLETED
12 (Optional) Collect All the Slates COMPLETED
20 Go to NASA COMPLETED
25 Find a Way Inside COMPLETED
30 Enter NASA COMPLETED
40 Find Information about NASA COMPLETED
50 Find Information about the Martian Sample COMPLETED
 
== London Landmark Quest ==
( Instance: 1 )
100 Look for the Opportunity Rover on Mars COMPLETED
 
== Unearthed ==
( Instance: 1 )
60 Find Information about the Prototype Grav Drive COMPLETED
70 Find Information about Grav Drive Side Effects COMPLETED
80 Release the Artifact COMPLETED
90 Take the Artifact COMPLETED
100 Leave NASA COMPLETED
110 Talk to the Emissary COMPLETED
120 Complete "Final Glimpses" COMPLETED
 
== Power from Beyond ==
( Instance: 15 )
3 Talk to Vladimir to Locate Temples COMPLETED
 
== Missed Beyond Measure ==
( Instance: 1 )
10 Wait for the Service (1 UT Days) COMPLETED
 
== Final Glimpses ==
( Instance: 1 )
10 Talk to Vladimir COMPLETED
12 Go to Tau Ceti VIII-a COMPLETED
30 Go to Freya III COMPLETED
 
== Power from Beyond ==
( Instance: 15 )
10 Acquire the Power on Tau Ceti III COMPLETED
 
== A Break at Dawn ==
( Instance: 1 )
20 Find Hugo Fournier at Athena Tower COMPLETED
30 Report to Sergeant Yumi COMPLETED
 
== Search and Seizure ==
( Instance: 1 )
20 Speak to the port workers COMPLETED
40 Obtain the ship records from Aegis COMPLETED
3 Read the slate and identify the smuggler's ship COMPLETED
50 Report to Sergeant Yumi COMPLETED
2 Wait for the Anansi to land COMPLETED
60 Obtain the contraband COMPLETED
70 (Optional) Bargain with the Captain COMPLETED
80 Report to Sergeant Yumi COMPLETED
 
== Misc Pointer ==
( Instance: 1 )
30 Talk to Sergeant Yumi COMPLETED
 
== Two Tales, Two Cities ==
( Instance: 1 )
10 Investigate the incident at Embassy District COMPLETED
20 Speak to Officer Markkanen COMPLETED
40 Speak to Functionary Gershon COMPLETED
45 Check the computer for evidence on the vandal COMPLETED
50 Speak to Officer Markkanen COMPLETED
88 Gather additional evidence COMPLETED
70 Arrest Functionary Gershon at UC Security COMPLETED
30 Speak to the Witness COMPLETED
60 Arrest Tahir Vala in the Well COMPLETED
80 Speak to Officer Markkanen COMPLETED
90 Speak to Sergeant Yumi COMPLETED
 
== Power from Beyond ==
( Instance: 15 )
15 Follow Distortions on the Scanner COMPLETED
 
== Power from Beyond ==
( Instance: 15 )
5 Use the Eye to Locate Temples COMPLETED
 
== Power from Beyond ==
( Instance: 15 )
7 Obtain a Ship with a 28LY Grav Jump Range COMPLETED
 
== Final Glimpses ==
( Instance: 1 )
11 Obtain a Ship with a 21LY Grav Jump range COMPLETED
20 Find the Artifact on Tau Ceti VIII-a COMPLETED
 
== Power from Beyond (Washakie) ==
( Instance: 22 )
10 Acquire the Power on Washakie COMPLETED
15 Follow Distortions on the Scanner COMPLETED
 
== Final Glimpses ==
( Instance: 1 )
35 Listen to the distress signal COMPLETED
 
== Entangled ==
( Instance: 1 )
105 Investigate the distress signal COMPLETED
 
== Final Glimpses ==
( Instance: 1 )
40 Complete "Entangled" COMPLETED
 
== Entangled ==
( Instance: 1 )
120 Enter Nishina Research Station COMPLETED
130 Speak to Ethan COMPLETED
140 Follow Ethan COMPLETED
150 Explore the ruins COMPLETED
171 Explore the ruins COMPLETED
181 Speak to Rafael COMPLETED
191 Find the Director's Office COMPLETED
210 Speak to Director Patel COMPLETED
400 Follow Maria COMPLETED
410 Get to the lab COMPLETED
420 Speak to Rafael COMPLETED
445 Speak to Rafael COMPLETED
450 Speak to Ethan COMPLETED
500 Speak to Maria COMPLETED
510 Assist Maria COMPLETED
520 Stand in the distortion COMPLETED
540 Find a Probe Control Unit COMPLETED
550 Recalibrate the distortion COMPLETED
560 Speak to Maria COMPLETED
570 Speak to Director Patel COMPLETED
571 (Optional) Get supplies COMPLETED
580 Take the elevator COMPLETED
600 Get to the lab COMPLETED
611 Override the lockdown in the Facilities Section COMPLETED
621 Override the lockdown in the Accelerator Section COMPLETED
700 Disengage Power Interlocks (0/7) OR Switch Universes COMPLETED
710 Shut down the Probe COMPLETED
720 Take the Artifact COMPLETED
770 Activate Primary Calibration COMPLETED
780 Wait for the Experiment COMPLETED
930 Speak to everyone COMPLETED
631 Override the lockdown in the Storage Section COMPLETED
910 Speak to Director Patel COMPLETED
920 Speak to Rafael COMPLETED
 
== Final Glimpses ==
( Instance: 1 )
22 Go to [...] COMPLETED
25 Find the Artifact on [...] COMPLETED
 
== Revelation ==
( Instance: 1 )
10 Go to Masada III COMPLETED
 
== [Entangled - Rafael Postquest] ==
( Instance: 0 )
210 Speak to Rafael Aguerro COMPLETED
 
== The Boot ==
( Instance: 1 )
70 (Optional) Sell the boots to the worker DORMANT
80 Return to Antonio COMPLETED
 
== Tapping the Grid ==
( Instance: 1 )
10 Talk to Louisa Reyez COMPLETED
20 Locate the Junction Box COMPLETED
40 Power Down the Junction Box COMPLETED
50 Locate the Junction Box COMPLETED
90 Power Down the Junction Box COMPLETED
100 Locate the Junction Box COMPLETED
140 Power Down the Junction Box COMPLETED
150 Locate the Junction Box COMPLETED
180 Power Down the Junction Box COMPLETED
190 Talk to Louisa Reyez COMPLETED
 
== Suspicious Activities ==
( Instance: 1 )
20 Find the suspicious people outside the UC Surplus COMPLETED
 
== Tapping the Grid ==
( Instance: 1 )
0  COMPLETED
 
== Alternating Currents ==
( Instance: 1 )
10 Talk to Louisa Reyez COMPLETED
20 Follow Louisa Reyez COMPLETED
30 Talk to Louisa Reyez COMPLETED
40 Wait for Louisa Reyez COMPLETED
50 Locate the Junction Box COMPLETED
60 Power Down the Junction Box COMPLETED
70 Locate Junction Box 45A COMPLETED
71 Locate Junction Box 47B COMPLETED
110 Locate the Junction Box COMPLETED
100 Power Down the Junction Box COMPLETED
 
== Reliable Care ==
( Instance: 1 )
10 Speak to Nurse O'Shea at the Medbay COMPLETED
 
== Primary Sources ==
( Instance: 1 )
80 Return to Nadia Muffaz DISPLAYED
 
== Reliable Care ==
( Instance: 1 )
20 Visit Dr. Lebedev at Reliant Medical DISPLAYED
 
== Alternating Currents ==
( Instance: 1 )
120 Power Down the Junction Box COMPLETED
150 Locate the Power Drain's source COMPLETED
151 Access the Apartment COMPLETED
160 Find evidence COMPLETED
170 Deliver evidence to Louisa Reyez COMPLETED
180 Deliver evidence to Zoe Kaminski COMPLETED
80 Power Down the Junction Box COMPLETED
 
== Suspicious Activities ==
( Instance: 1 )
25 Eavesdrop on the conversation COMPLETED
30 Report back to Theresa about the planned robbery COMPLETED
35 Report the planned robbery to Officer Endler COMPLETED
40 Confront the thieves COMPLETED
50 Kill the thieves COMPLETED
60 Report back to Theresa COMPLETED
 
== Power from Beyond ==
( Instance: 17 )
3 Talk to Vladimir to Locate Temples COMPLETED
 
== Missed Beyond Measure ==
( Instance: 1 )
20 Attend the Memorial Service Today COMPLETED
25 (Optional) Wait 24 Hours (UT) to Skip the Memorial COMPLETED
30 Say a Few Words COMPLETED
40 (Optional) Speak with Everyone COMPLETED
50 Leave When You're Ready COMPLETED
 
== Power from Beyond ==
( Instance: 17 )
10 Acquire the Power on Eridani I COMPLETED
 
== Power from Beyond ==
( Instance: 17 )
7 Obtain a Ship with a 28LY Grav Jump Range COMPLETED
 
== Power from Beyond ==
( Instance: 17 )
15 Follow Distortions on the Scanner COMPLETED
 
== Power from Beyond ==
( Instance: 17 )
5 Use the Eye to Locate Temples COMPLETED
 
== Defensive Measures ==
( Instance: 1 )
30 Place Keoni's Sensors COMPLETED
 
== Blast Zone ==
( Instance: 1 )
25 Talk to Mr. Tate COMPLETED
 
== Solomon's Trove ==
( Instance: 0 )
600 Return Akila City Charter to Leah Casler COMPLETED
 
== Defensive Measures ==
( Instance: 1 )
40 Talk to Keoni Alpin COMPLETED
50 Get the Guard to leave the tower COMPLETED
55 (Optional) Steal something in front of Guard COMPLETED
70 Talk to Keoni Alpin COMPLETED
25 Agree to help Keoni Alpin COMPLETED
 
== False Positives ==
( Instance: 1 )
10 Wait 24 hours (UT) for Keoni to collect her data COMPLETED
 
== Misc pointer to player house in Akila City midtown ==
( Instance: 0 )
100 Visit your new home in midtown Akila City COMPLETED
 
== Blast Zone ==
( Instance: 1 )
300 Clear out the hard rock on Tate's land (6/6) COMPLETED
 
== False Positives ==
( Instance: 1 )
30 Go to the East Gate COMPLETED
35 Talk to Davis Wilson DISPLAYED
 
==  ==
( Instance: 1 )
100 Find the distress call in the Charybdis system DISPLAYED
 
== Blast Zone ==
( Instance: 1 )
400 Report back to Ngodup Tate COMPLETED
 
== Misc pointer to player manor in Akila City core ==
( Instance: 0 )
100 Visit your new manor in the Akila City core COMPLETED
 
== Hand Delivered ==
( Instance: 0 )
100 Deliver message to Marcel Duris DISPLAYED
 
== Beer Run ==
( Instance: 1 )
100 Sabotage the latest batch of Henry's beer DISPLAYED
 
== Quest for spawning and scenes for settlers to sign up for LIST ==
( Instance: 18 )
100 Sign up settler for the LIST DORMANT
 
== Revelation ==
( Instance: 1 )
20 Hail the Scorpius COMPLETED
30 Defeat the Starborn Ships COMPLETED
40 Hail the [...] COMPLETED
50 Land at the Buried Temple COMPLETED
60 Find the Artifact COMPLETED
70 Defeat the Starborn COMPLETED
80 Defeat the Starborn COMPLETED
81 Defeat the Ecliptic Soldiers COMPLETED
90 Find a way to open the Door COMPLETED
100 Enter the Anomaly COMPLETED
110 Leave the Anomaly COMPLETED
120 Defeat the Starborn COMPLETED
130 Find a way to open the Door COMPLETED
140 Enter the Anomaly COMPLETED
145 Find a way out of the Anomaly COMPLETED
150 Leave the Anomaly COMPLETED
190 Leave the Anomaly COMPLETED
200 Defeat the Starborn COMPLETED
225 Defeat the Emissary and the Hunter COMPLETED
230 Take the Hunter's Artifacts COMPLETED
240 Take the Emissary's Artifacts COMPLETED
260 Talk to the Hunter COMPLETED
250 Talk to the Emissary COMPLETED
270 Take the Artifact COMPLETED
 
== One Giant Leap ==
( Instance: 1 )
30 Build the Armillary on Your Ship DISPLAYED
50 (Optional) Talk to your Friends before you Leave DISPLAYED
 
== Revelation ==
( Instance: 1 )
125 (Optional) Disable the Defenses COMPLETED
135 Find the Artifact COMPLETED
210 Defeat the Hunter COMPLETED
220 Defeat the Emissary COMPLETED
 
== Power from Beyond ==
( Instance: 20 )
3 Talk to Vladimir to Locate Temples COMPLETED
 
== Power from Beyond ==
( Instance: 20 )
10 Acquire the Power on Tidacha I COMPLETED
 
== Power from Beyond ==
( Instance: 20 )
7 Obtain a Ship with a 28LY Grav Jump Range COMPLETED
 
== Power from Beyond ==
( Instance: 20 )
15 Follow Distortions on the Scanner COMPLETED
 
== Power from Beyond ==
( Instance: 20 )
5 Use the Eye to Locate Temples COMPLETED
 
== Power from Beyond ==
( Instance: 21 )
3 Talk to Vladimir to Locate Temples COMPLETED
 
== Power from Beyond ==
( Instance: 21 )
10 Acquire the Power on Neebas COMPLETED
 
== Power from Beyond ==
( Instance: 21 )
7 Obtain a Ship with a 28LY Grav Jump Range COMPLETED
 
== Power from Beyond ==
( Instance: 21 )
15 Follow Distortions on the Scanner COMPLETED
 
== Power from Beyond ==
( Instance: 21 )
5 Use the Eye to Locate Temples COMPLETED
 
== Power from Beyond ==
( Instance: 22 )
3 Talk to Vladimir to Locate Temples COMPLETED
 
== Power from Beyond ==
( Instance: 22 )
10 Acquire the Power on Rasalhague II COMPLETED
 
== Power from Beyond ==
( Instance: 22 )
7 Obtain a Ship with a 28LY Grav Jump Range COMPLETED
 
== Power from Beyond ==
( Instance: 22 )
15 Follow Distortions on the Scanner COMPLETED
 
== Power from Beyond ==
( Instance: 22 )
5 Use the Eye to Locate Temples COMPLETED
 
== Power from Beyond ==
( Instance: 23 )
3 Talk to Vladimir to Locate Temples COMPLETED
 
== Power from Beyond ==
( Instance: 23 )
10 Acquire the Power on Sirius I COMPLETED
 
== Power from Beyond ==
( Instance: 23 )
7 Obtain a Ship with a 28LY Grav Jump Range COMPLETED
 
== Power from Beyond ==
( Instance: 23 )
15 Follow Distortions on the Scanner COMPLETED
 
== False Positives ==
( Instance: 1 )
15 Talk to Keoni Alpin about Ashta data DISPLAYED
 
== Power from Beyond ==
( Instance: 23 )
5 Use the Eye to Locate Temples COMPLETED
 
== Power from Beyond ==
( Instance: 24 )
3 Talk to Vladimir to Locate Temples COMPLETED
 
== Power from Beyond ==
( Instance: 24 )
10 Acquire the Power on Feynman I COMPLETED
 
== Power from Beyond ==
( Instance: 24 )
7 Obtain a Ship with a 28LY Grav Jump Range COMPLETED
 
== Power from Beyond ==
( Instance: 24 )
15 Follow Distortions on the Scanner COMPLETED
 
== Power from Beyond ==
( Instance: 24 )
5 Use the Eye to Locate Temples COMPLETED
 
== Power from Beyond ==
( Instance: 25 )
3 Talk to Vladimir to Locate Temples COMPLETED
 
== Power from Beyond ==
( Instance: 25 )
10 Acquire the Power on Nikola II COMPLETED
 
== Power from Beyond ==
( Instance: 25 )
7 Obtain a Ship with a 28LY Grav Jump Range COMPLETED
 
== Power from Beyond ==
( Instance: 25 )
15 Follow Distortions on the Scanner COMPLETED
 
== Power from Beyond ==
( Instance: 25 )
5 Use the Eye to Locate Temples COMPLETED
3 Talk to Vladimir to Locate Temples COMPLETED
10 Acquire the Power on Marduk IV DISPLAYED
7 Obtain a Ship with a 28LY Grav Jump Range DISPLAYED
 
==  ==
( Instance: 4 )
100 Evacuate the Survivalist COMPLETED

        """.trimIndent()
        else -> command
    }
}