package wikiScraper

import Perk
import PerkCategory
import PerkTier
import org.jsoup.nodes.Document
import java.io.File

fun main() {
    val options = ScraperOptions("perks", onlyOne = false)
    val urlFile = File("raw-data/perks-pages.txt")
    if (!urlFile.exists()) urlFile.writeText("")
    fetchPagesIfEmpty(urlFile, listOf("https://starfieldwiki.net/wiki/Starfield:Skills"), options.onlyOne)
    val output = File("src/jsMain/resources/perks-wiki-data.json")

    println("Reading Perks")
    readFromUrls(urlFile, output, ::parsePerk, options)
}

private fun parsePerk(url: String, page: Document): List<Perk> {
    val name = page.title().replace("Starfield:", "").replace(" - Starfield Wiki", "").trim()
    val table = page.select(".wikitable").first()!!
    val category = table.selectRightTdClean("Category")
        ?.let { perkText -> PerkCategory.entries.firstOrNull { it.name.lowercase() == perkText.lowercase() } }
        ?: PerkCategory.OTHER
    val tier = table.selectRightTdClean("Skill Tier")
        ?.let { tierText -> PerkTier.entries.firstOrNull { it.name.lowercase() == tierText.lowercase() } }
        ?: PerkTier.NOVICE
    val ranks = page.select(".gallerybox")
        .flatMap { it.select("img") }
        .map { "https:"+ it.attr("src") }
        .associateBy {
            it.substring(it.length-5, it.length-4).toInt()
        }

    return listOf(Perk(name, category, tier, url, ranks))
}

