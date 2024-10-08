package wikiScraper

import PointOfInterest
import jsonMapper
import kotlinx.serialization.encodeToString
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import toPOIType
import java.io.File


fun main() {
    val inputFile = File("raw-data/places.html")
    val output = File("src/jsMain/resources/poi-wiki-data.json")

    println("Reading poi")
    parsePOI(Jsoup.parse(inputFile.readText())).let { output.writeText(jsonMapper.encodeToString(it)) }

}

private fun parsePOI(page: Document): List<PointOfInterest> {
    return page.select("li.tocsection-4").first()!!.select("a").flatMap { page.select(it.attr("href")) }.filter { it.id() != "Fixed_Points_of_Interest" }.flatMap { section ->
        val type = section.id().toPOIType()
        var contents = section.parent()!!.nextElementSibling()!!
        if (contents.`is`("p")) contents = contents.nextElementSibling()!!

        contents.select("li").map { li ->
            val link = li.select("a").toList().first { it.hasAttr("title") }
            val url = link.attr("href")
            val detailPage = fetch("https://starfieldwiki.net:$url", "places", true)
            val description = detailPage.select("p").first()?.text() ?: ""
            val locationSentence = detailPage.select("table").select("td").map { it.text() }.firstOrNull { it.startsWith("On the planet") }
            val locationStringPlanet = locationSentence?.split(",")?.first()?.replace("On the planet ", "")?.trim()
            val locationStringSystem = locationSentence?.split(",")?.last()?.replace("in the", "")?.replace("System.", "")?.trim()
            val infoPlanet = detailPage.select("table.infobox").firstOrNull()?.selectHeader("Planet")?.text()
            val infoSystem = detailPage.select("table.infobox").firstOrNull()?.selectHeader("System")?.text()


            val planet = locationStringPlanet ?: infoPlanet
            val system = (locationStringSystem ?: infoSystem)?.replace("System", "")?.trim()

            PointOfInterest(link!!.text(), description, type, url, system, planet)
        }
    }

}
