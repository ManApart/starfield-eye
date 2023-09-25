enum class ResourceType(val readableName: String, val color: String = "808080", val aliases: List<String> = listOf()) {
    Ad("Aldumite", "bb8cff"),
    Ag("Silver", "80736d"),
    Al("Aluminum", "2f4644"),
    Ar("Argon", "094961"),
    Au("Gold", "c89f50"),
    Be("Beryllium", "396764"),
    C6Hn("Benzene", "1a607f"),
    Cl("Chlorine", "404753"),
    Co("Cobalt", "33588c"),
    Cs("Caesium", "8a84fe"),
    Ct("Caelumite", "ff7f00"),
    Cu("Copper", "1e5548"),
    Dy("Dysprosium", "75a4b8"),
    Eu("Europium", "9fd0cd"),
    F("Fluorine", "2f7867"),
    Fe("Iron", "7c3b37"),
    H2O("Water", "63a39d", listOf("H20")),
    He3("Helium-3", "2d343c", listOf("He-3")),
    Hg("Mercury", "ab958a"),
    HnCn("Alkanes", "a14b54"),
    IL("Ionic Liquids", "4cd7ba"),
    Ie("Indicite", "d8feff"),
    Ir("Iridium", "716e2b"),
    Li("Lithium", "5b73c9"),
    Nd("Neodymium", "699a97"),
    Ne("Neon", "2aa1bf"),
    Ni("Nickel", "614325"),
    Pb("Lead", "454b51"),
    Pd("Palladium", "d8a33d"),
    Pt("Platinum", "4970a7"),
    Pu("Plutonium", "b6b53f"),
    RCOC("R-Coclaurine"),
    RCOOH("Carboxylic Acids", "2d343c", listOf("R-COOH")),
    Rc("Rothicite", "fb9376"),
    Sb("Antimony", "e4c761"),
    SiH3Cl("Chlorosilanes", "4e5d94"),
    Ta("Tantalum", "bc5e5f"),
    Ti("Titanium", "6a8795"),
    Tsn("Tasine", "fbdd4b"),
    U("Uranium", "4b4815"),
    V("Vanadium", "8c8656"),
    Vr("Veryl", "28dace"),
    Vy("Vytinium", "eded57"),
    W("Tungsten", "596b75"),
    Xe("Xenon", "8698fe"),
    Yb("Ytterbium", "d4826c"),
    xF4("Tetrafluorides", "379e89"),
    ;

    fun matches(searchText: String): Boolean {
        if (searchText in listOf("Unknown", "None")) return false
        return name == searchText
                || readableName == searchText
                || aliases.any { it == searchText }
    }

    fun contains(searchText: String): Boolean {
        val search = searchText.lowercase()
        return name.lowercase().contains(search)
                || readableName.lowercase().contains(search)
                || aliases.any { it.contains(search) }
    }
}