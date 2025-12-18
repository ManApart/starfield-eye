package wikiScraper

import org.jsoup.nodes.Element


fun Element?.cleanText(): String? {
    return this?.text()?.replace("(?)", "")?.ifBlank { null }
}

fun Element.select(row: Int, cell: Int): Element? {
    return select("tr")[row].select("td")[cell]
}

fun Element.tablePair(headerText: String): Pair<String, String>? {
    return selectHeaderClean(headerText)?.let { headerText to it }
}

fun Element.selectHeaderClean(headerText: String): String? {
    return selectHeader(headerText).cleanText()
}

fun Element.selectHeader(headerText: String): Element? {
    return selectRight(headerText) ?: selectBelow(headerText)
}

fun Element.selectRightClean(headerText: String): String? {
    return selectRight(headerText).cleanText()
}

fun Element.selectRight(headerText: String): Element? {
    return select("tr")
        .firstOrNull { row -> row.select("th").any { it.text() == headerText } }
        ?.let { row ->
            val headers = row.select("th")
            val right = headers.first { it.text() == headerText }.let { headers.indexOf(it) }
            row.select("td").takeIf { it.size > right }?.get(right)
        }
}

fun Element.selectRightTdClean(tdText: String): String? {
    return select("tr")
        .firstOrNull { row -> row.select("td").any { it.text() == tdText } }
        ?.let { row ->
            val cells = row.select("td")
            val right = cells.first { it.text() == tdText }.let { cells.indexOf(it) + 1 }
            row.select("td").takeIf { it.size > right }?.get(right)
        }.cleanText()
}

fun Element.selectBelowClean(headerText: String): String? {
    return selectBelow(headerText).cleanText()
}

fun Element.selectBelow(headerText: String): Element? {
    val rows = select("tr")
    return rows.firstOrNull { row -> row.select("th").any { it.text() == headerText } }?.let { row ->
        val i = rows.indexOf(row) + 1
        rows[i]
    }
}

fun Element.selectTdClean(col: Int) = selectTd(col).cleanText()
fun Element.selectTd(col: Int): Element? {
    return select("td").takeIf { it.size > col }?.get(col)
}

fun parseName(box: Element): String {
    return if (box.text().contains(")")) {
        box.text().let { it.substring(0, it.indexOf(")") + 1) }.trim()
    } else {
        box.text()
    }
}

fun parsePlanet(box: Element): String {
    return if (box.select("a").isNotEmpty()) {
        box.select("a").first()!!.text().trim()
    } else {
        box.text()
    }
}
