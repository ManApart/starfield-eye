package views

import Perk
import el
import inMemoryStorage
import kotlinx.html.*
import kotlinx.html.js.onChangeFunction
import org.w3c.dom.HTMLImageElement
import org.w3c.dom.HTMLInputElement
import perks
import persistMemory
import replaceElement
import updateUrl

fun perkView() {
    updateUrl("perks")
    replaceElement {
        div {
            id = "perk-view"
            navButtons()
            div {
                id = "sections"
                div("section-view-box") {
                    id = "perk-tables"
                    h2 { +"Perks" }
                    div("accent-line") { +"" }

                    dataList {
                        id = "ticks"
                        (0..4).forEach { option { value = "$it" } }
                    }

                    perks.values.groupBy { it.category }.forEach { (category, categoryPerks) ->
                        h3 { +category.name.lowercase().capitalize() }
                        table("perk-table") {
                            categoryPerks.groupBy { it.tier }.entries.sortedBy { it.key.ordinal }
                                .forEach { (tier, perks) ->
                                    tr("perk-row perk-row-$tier") {
                                        perks.forEach { perk ->
                                            val rank = inMemoryStorage.perks[perk.name] ?: 0
                                            td("perk-cell perk-cell-$tier") {
                                                a(perk.url, target = "_blank") {
                                                    img(classes = "perk-image", src = perk.ranks[rank]) {
                                                        id = "${perk.name}-badge"
                                                    }
                                                }
                                                div {
                                                    +perk.name.replace("_", " ")
                                                }
                                                input(type = InputType.range) {
                                                    id = "${perk.name}-slider"
                                                    list = "ticks"
                                                    min = "0"
                                                    max = "4"
                                                    value = rank.toString()
                                                    step = "1"
                                                    onChangeFunction = {
                                                        val newValue =
                                                            el<HTMLInputElement>("${perk.name}-slider").value.toIntOrNull()
                                                                ?: 0
                                                        inMemoryStorage.perks[perk.name] = newValue
                                                        el<HTMLImageElement>("${perk.name}-badge").src =
                                                            perk.ranks[newValue] ?: ""
                                                        persistMemory()
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                        }
                    }
                }
            }
        }
    }
}