package views

import components.linkableH2
import components.sliderPopup
import el
import inMemoryStorage
import kotlinx.html.*
import kotlinx.html.js.onChangeFunction
import kotlinx.html.js.onClickFunction
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
                perks.values.groupBy { it.category }.entries.sortedBy { it.key.ordinal }.forEach { (category, categoryPerks) ->
                    div("section-view-box") {
                        div("perk-header") {
                            style = "background-color: ${category.color};"

                            linkableH2("perks", category.name.lowercase().capitalize())
                        }

                        div("perk-table") {
                            categoryPerks.groupBy { it.tier }.entries.sortedBy { it.key.ordinal }
                                .forEach { (tier, perks) ->
                                    div("perk-row perk-row-$tier") {
                                        perks.sortedBy { it.name }.forEach { perk ->
                                            val rank = inMemoryStorage.perks[perk.name] ?: 0
                                            div("perk-cell perk-cell-$tier") {
                                                    img(classes = "perk-image", src = perk.ranks[rank]) {
                                                        id = "${perk.name}-badge"
                                                        onClickFunction = {
                                                            sliderPopup(0..4, inMemoryStorage.perks[perk.name] ?: 0){newValue ->
                                                                inMemoryStorage.perks[perk.name] = newValue
                                                                el<HTMLImageElement>("${perk.name}-badge").src =
                                                                    perk.ranks[newValue] ?: ""
                                                                persistMemory()
                                                            }
                                                        }
                                                    }
                                                a(perk.url, target = "_blank") {
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