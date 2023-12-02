package docking

import MiscStats
import MissionType
import MissionWikiData
import PollResponse
import Quest
import QuestStage
import QuestStageState
import jsonMapper
import kotlinx.serialization.encodeToString
import stat

fun parseQuests(lines: List<String>, missionReference: Map<String, MissionWikiData>): List<Quest> {
    return lines.chunkedBy("==")
        .mapIndexed { i, q -> parseQuest(i, q, missionReference) }
        .filter { it.latestState == QuestStageState.COMPLETED || it.latestState == QuestStageState.DISPLAYED }
        .toList()
}

private fun List<String>.chunkedBy(delimiter: String): List<List<String>> {
    return asSequence()
        .mapIndexedNotNull { i, line ->
            if (line.startsWith(delimiter)) i else null
        }
        .windowed(2, 1, true) {
            val end = if (it.size > 1) it.last() else this.size
            this.subList(it.first(), end)
        }.toList()
}

private fun parseQuest(i: Int, lines: List<String>, missionReference: Map<String, MissionWikiData>): Quest {
    val title = lines.first().replace("==", "").trim()
    val instance = lines[1]
        .replace("( Instance:", "")
        .replace(")", "").trim()
        .toIntOrNull() ?: 0

    val stages = lines.asSequence().drop(2)
        .filter { it.isNotBlank() }
        .map { it.split(" ").map { word -> word.trim() } }
        .filter { it.first().toIntOrNull() != null }
        .map { words ->
            val id = words.first().toInt()
            val name = words.subList(1, words.size - 1).joinToString(" ")
            val state = QuestStageState.entries.firstOrNull { it.name == words.last() } ?: QuestStageState.OTHER
            QuestStage(id, name, state)
        }.toList()

    val cleanTitle = title.ifBlank { stages.firstOrNull()?.name ?: "" }
    val wikiRef = missionReference[cleanTitle]
    val id = wikiRef?.id ?: ""
    val type = wikiRef?.type ?: MissionType.OTHER

    return Quest(cleanTitle, id, i, instance, type, stages)
}

fun parsePollResponse(lines: List<String>, missionReference: Map<String, MissionWikiData>): PollResponse {
    val commands = lines.chunkedBy(">")
        .filter { it.size != 1 }
        .associate { it.first().replace(">", "").trim() to it.drop(1) }
//        .also { raw -> println(JSON.stringify(raw.toMutableMap().also { it.remove("sqo") }.values)) }
        .let { RawPollResponse(it) }

    return PollResponse(parseQuests(commands.getQuests(), missionReference), parseMiscStats(commands))
}

private fun parseMiscStats(commands: RawPollResponse): MiscStats? {
    return if (!commands.hasMiscStats()) null else {
        with(commands) {
            MiscStats(
                listOf(
                    stat("locations discovered"),
                    stat("locations explored"),
                    stat("days passed"),
                    stat("hours slept"),
//                    stat("hours waited"), //90
                    stat("credits found"),
                    stat("most credits carried"),
                    stat("containers looted"),//1359
                    stat("magazines found", "Thirst for Knowledge", 20),
//                    stat("persuasion successes"),//70
                    stat("afflictions suffered"),
                    stat("food eaten"),
                    stat("data slates read"),
//                    stat("skill challenges completed"), //52
                    stat("snow globes collected"),
                ),
                listOf(
                    stat("systems visited", "Stellar Cartography", 20),
                    stat("planets scanned"),
                    stat("planets fully surveyed"),
                    stat("planets landed on", "Boots on the Ground", 100),
                    stat("flora fully scanned"),
                    stat("fauna fully scanned"),
                    stat("stations docked with"),
                    stat("ships docked with"),
                    stat("unique creatures scanned"),
                    stat("temples found"),
                ),
                listOf(
                    stat("grav jumps"),
                    stat("farthest grav jump"),
//                    stat("ships registered"), //7
                    stat("max ships owned"),
                    stat("most expensive ship owned"),
                    stat("largest ship owned"),
                    stat("largest crew size"),
                    stat("ships customized"),
                    stat("ship builder credits spent"),
                    stat("ships sold"),
                    stat("ships painted"),
                    stat("ship modules built"),
                ),
                listOf(
                    stat("quests completed"),
                    stat("activities completed"),
                    stat("main quests completed"),
                    stat("crimson fleet quests completed"),
                    stat("freestar collective quests completed"),
                    stat("ryujin industries quests completed"),
                    stat("united colonies quests completed"),
                    stat("side quests completed", "Privateer", 30),
                ),
                listOf(
                    stat("people killed", "Dark Matter", 300),
                    stat("creatures killed", "Another Bug Hunt", 300),
                    stat("robots killed"),
                    stat("turrets killed"),
//                    stat("elite enemies killed"), //199
                    stat("critical strikes"),
                    stat("sneak attacks"),
//                    stat("boost packs exploded"), //134
                    stat("zero g kills"),
                    stat("powers used"),
                    stat("scoped kills"),
                    stat("max damage shot"),
                    stat("melee kills"),
                    stat("laser kills"),
                    stat("ballistic kills"),
                    stat("head shots"),
                    stat("explosive kills"),
                    stat("starborn killed", "War of Angels", 20),
                    stat("ships destroyed"),
//                    stat("favorite weapon"), //Kodama
//                    stat("favorite power"), // Personal Atmosphere
                ),
                listOf(
//                    stat("weapon mods crafted", "Soldier of Fortune", 50), //55
//                    stat("armor mods crafted"), //24
                    stat("organic resources gathered", "Life Begets Life", 500),
                    stat("inorganic resources gathered", "Rock Collection", 500),
//                    stat("chems crafted"), //12
                    stat("food cooked"), //4
                    stat("outposts built"),
                    stat("objects built"),
                    stat("cargo links established"),
                    stat("cargo links established", "Shipping Magnate", 5), //getting links, not count of outposts linked
                ),
                listOf(
                    stat("locks picked", "Cyber Jockey", 50),
                    stat("pockets picked"),
                    stat("items stolen"),
                    stat("assaults"),
                    stat("murders"),
                    stat("trespasses"),
                    stat("times arrested"),
                    stat("most contraband carried"),
                    stat("times caught with contraband"),
                    stat("acts of piracy"),
                    stat("total lifetime bounty"),
                    stat("largest bounty"),
                ),
            )
        }
    }
}