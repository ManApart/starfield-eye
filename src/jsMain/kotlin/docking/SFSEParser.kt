package docking

import CombatStats
import CraftingStats
import CrimeStats
import ExplorationStats
import GeneralStats
import MiscStats
import MissionStats
import PollResponse
import Quest
import QuestStage
import QuestStageState
import ShipStats

fun parseQuests(lines: List<String>): List<Quest> {
    return lines.chunkedBy("==")
        .map { parseQuest(it) }
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

private fun parseQuest(lines: List<String>): Quest {
    val title = lines.first().replace("==", "").trim()
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

    return Quest(cleanTitle, stages)
}

fun parsePollResponse(lines: List<String>): PollResponse {
    val commands = lines.chunkedBy(">")
        .filter { it.size != 1 }
        .associate { it.first().replace(">",  "").trim() to it.drop(1) }
        .let { RawPollResponse(it) }

    with(commands) {
        return PollResponse(
            parseQuests(getQuests()),
            MiscStats(
                GeneralStats(
                    getMiscStatInt("locations discovered"),
                    getMiscStatInt("locations explored"),
                    getMiscStatInt("days passed"),
                    getMiscStatInt("hours slept"),
                    getMiscStatInt("hours waited"),
                    getMiscStatInt("credits found"),
                    getMiscStatInt("most credits carried"),
                    getMiscStatInt("containers looted"),
                    getMiscStatInt("magazines found"),
                    getMiscStatInt("persuasion successes"),
                    getMiscStatInt("afflictions suffered"),
                    getMiscStatInt("food eaten"),
                    getMiscStatInt("data slates read"),
                    getMiscStatInt("skill challenges completed"),
                    getMiscStatInt("snow globes collected"),
                ),
                ExplorationStats(
                    getMiscStatInt("systems visited"),
                    getMiscStatInt("planets scanned"),
                    getMiscStatInt("planets fully surveyed"),
                    getMiscStatInt("planets landed on"),
                    getMiscStatInt("flora fully scanned"),
                    getMiscStatInt("fauna fully scanned"),
                    getMiscStatInt("stations docked with"),
                    getMiscStatInt("ships docked with"),
                    getMiscStatInt("unique creatures scanned"),
                    getMiscStatInt("temples found"),
                    getMiscStatInt("grav jumps"),
                ),
                ShipStats(
                    getMiscStatInt("farthest grav jump"),
                    getMiscStatInt("ships registered"),
                    getMiscStatInt("max ships owned"),
                    getMiscStatInt("most expensive ship owned"),
                    getMiscStatInt("largest ship owned"),
                    getMiscStatInt("largest crew size"),
                    getMiscStatInt("ships customized"),
                    getMiscStatInt("ship builder credits spent"),
                    getMiscStatInt("ships sold"),
                    getMiscStatInt("ships painted"),
                    getMiscStatInt("ship modules built"),
                ),
                  MissionStats(
                    getMiscStatInt("quests completed"),
                    getMiscStatInt("activities completed"),
                    getMiscStatInt("main quests completed"),
                    getMiscStatInt("crimson fleet collective quests completed"),
                    getMiscStatInt("freestar collective quests completed"),
                    getMiscStatInt("ryujin industries quests completed"),
                    getMiscStatInt("united colonies quests completed"),
                    getMiscStatInt("side quests completed"),
                  ),
                CombatStats(
                    getMiscStatInt("people killed"),
                    getMiscStatInt("creatures killed"),
                    getMiscStatInt("robots killed"),
                    getMiscStatInt("turrets killed"),
                    getMiscStatInt("elite enemies killed"),
                    getMiscStatInt("critical strikes"),
                    getMiscStatInt("sneak attacks"),
                    getMiscStatInt("boost packs exploded"),
                    getMiscStatInt("zero g kills"),
                    getMiscStatInt("powers used"),
                    getMiscStatInt("scoped kills"),
                    getMiscStatInt("max damage shot"),
                    getMiscStatInt("melee kills"),
                    getMiscStatInt("laser kills"),
                    getMiscStatInt("ballistic kills"),
                    getMiscStatInt("head shots"),
                    getMiscStatInt("explosive kills"),
                    getMiscStatInt("starborn killed"),
                    getMiscStatInt("ships destroyed"),
                    getMiscStatString("favorite weapon"),
                    getMiscStatString("favorite power"),
                ),
                    CraftingStats(
                    getMiscStatInt("weapon mods crafted"),
                    getMiscStatInt("armor mods crafted"),
                    getMiscStatInt("organic resources gathered"),
                    getMiscStatInt("inorganic resources gathered"),
                    getMiscStatInt("chems crafted"),
                    getMiscStatInt("food cooked"),
                    getMiscStatInt("outposts built"),
                    getMiscStatInt("objects built"),
                    getMiscStatInt("cargo links established"),
                    ),
                CrimeStats(
                    getMiscStatInt("locks picked"),
                    getMiscStatInt("pockets picked"),
                    getMiscStatInt("items stolen"),
                    getMiscStatInt("assaults"),
                    getMiscStatInt("murders"),
                    getMiscStatInt("trespasses"),
                    getMiscStatInt("times arrested"),
                    getMiscStatInt("most contraband carried"),
                    getMiscStatInt("times caught with contraband"),
                    getMiscStatInt("acts of piracy"),
                    getMiscStatInt("total lifetime bounty"),
                    getMiscStatInt("largest bounty"),
                ),
            )
        )
    }
}