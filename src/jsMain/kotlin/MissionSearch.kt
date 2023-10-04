import views.filterMissions

fun searchMissions() {
    val filtered = with(inMemoryStorage.missionSearchOptions) {
        val text = searchText.lowercase()
        inMemoryStorage.quests
            .filter { it.matches(types) }
            .filter { it.matches(showCompleted) }
            .filter { it.matches(text) }
    }
    filterMissions(filtered)
    persistMemory()
}

private fun Quest.matches(types: List<MissionType>): Boolean {
    if (this.type == MissionType.MAIN) println("Main")
    return this.type in types
}

private fun Quest.matches(showCompleted: Boolean): Boolean {
    return this.latestState == QuestStageState.DISPLAYED || (showCompleted && this.latestState == QuestStageState.COMPLETED)
}

private fun Quest.matches(searchText: String): Boolean {
    return searchText.isBlank() ||
            (this.name.lowercase().contains(searchText)
                    || this.stages.any { it.name.lowercase().contains(searchText) })
}