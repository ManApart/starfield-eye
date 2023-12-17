package views

import components.linkableH2
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

fun perkView(section: String? = null) {
    updateUrl("perks", section)
    replaceElement {
        div {
            id = "perk-view"
            navButtons()
            div {
                id = "sections"
                perks.values.groupBy { it.category }.forEach { (category, categoryPerks) ->
                    div("section-view-box") {
                        div("perk-header") {
                            style = "background-color: ${category.color};"

                            linkableH2("perks", category.name.lowercase().capitalize())
                        }

                        dataList {
                            id = "ticks"
                            (0..4).forEach { option { value = "$it" } }
                        }

                        div("perk-table") {
                            categoryPerks.groupBy { it.tier }.entries.sortedBy { it.key.ordinal }
                                .forEach { (tier, perks) ->
                                    div("perk-row perk-row-$tier") {
                                        perks.forEach { perk ->
                                            val rank = inMemoryStorage.perks[perk.name] ?: 0
                                            div("perk-cell perk-cell-$tier") {
                                                a(perk.url, target = "_blank") {
                                                    img(classes = "perk-image", src = perk.ranks[rank]) {
                                                        id = "${perk.name}-badge"
                                                    }
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
                                                div {
                                                    +perk.name.replace("_", " ")
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