//enum class PerkCategory { PHYSICAL, SOCIAL, COMBAT, SCIENCE, TECHNICAL }
//enum class PerkRank { NOVICE, ADVANCED, EXPERT, MASTER }
//
//enum class Perk(val category: PerkCategory, val rank: PerkRank) {
//    BOXING(PerkCategory.PHYSICAL, PerkRank.NOVICE),
//    FITNESS(PerkCategory.PHYSICAL, PerkRank.NOVICE),
//    STEALTH(PerkCategory.PHYSICAL, PerkRank.NOVICE),
//    WEIGHT_LIFTING(PerkCategory.PHYSICAL, PerkRank.NOVICE),
//    WELLNESS(PerkCategory.PHYSICAL, PerkRank.NOVICE),
//    ENERGY_WEAPON_DISSIPATION(PerkCategory.PHYSICAL, PerkRank.ADVANCED),
//    COMMERCE(PerkCategory.SOCIAL, PerkRank.NOVICE),
//
//    ;
//
//    val cleanName = name.lowercase().split("_").joinToString(" ") { it.capitalize() }
//}