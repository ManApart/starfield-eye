package views

import Perk
import el
import inMemoryStorage
import kotlinx.html.*
import kotlinx.html.js.onChangeFunction
import org.w3c.dom.HTMLInputElement
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
                        (0..4).forEach { option {value = "$it"} }
                    }

//                    Perk.entries.groupBy { it.category }.forEach { (category, categoryPerks) ->
//                        h3 { +category.name.lowercase().capitalize() }
//                        table("perk-table") {
//                            categoryPerks.groupBy { it.rank }.entries.sortedBy { it.key.ordinal }.forEach { (rank, perks) ->
//                                tr("perk-row") {
//                                    perks.forEach { perk ->
//                                        td("perk-cell") {
//                                            img(classes = "perk-image", src = "") {  }
//                                            +perk.cleanName
//                                            input(type = InputType.range) {
//                                                id = "${perk.name}-slider"
//                                                list = "ticks"
//                                                min = "0"
//                                                max = "4"
//                                                value = inMemoryStorage.perks[perk.name]?.toString() ?: "0"
//                                                step = "1"
//                                                onChangeFunction = {
//                                                    val newValue = el<HTMLInputElement>("${perk.name}-slider").value.toIntOrNull() ?: 0
//                                                    inMemoryStorage.perks[perk.name] = newValue
//                                                    persistMemory()
//                                                }
//                                            }
//                                        }
//                                    }
//                                }
//                            }
//                        }
//                    }
                }
            }
        }
    }
}