enum class ResourceType(val readableName: String, val color: String = "808080", val aliases: List<String> = listOf()) {
    Ad("Aldumite", "808080"),
    Ag("Silver", "808080"),
    Al("Aluminum", "2f4644"),
    Ar("Argon", "094961"),
    Au("Gold"),
    Be("Beryllium"),
    C6Hn("Benzene", "1a607f"),
    Cl("Chlorine", "404753"),
    Co("Cobalt"),
    Cs("Caesium"),
    Ct("Caelumite"),
    Cu("Copper", "1e5548"),
    Dy("Dysprosium"),
    Eu("Europium"),
    F("Fluorine"),
    Fe("Iron", "7c3b37"),
    H2O("Water", "63a39d", listOf("H20")),
    He3("Helium-3", "2d343c", listOf("He-3")),
    Hg("Mercury", "808080"),
    HnCn("Alkanes", "808080"),
    IL("Ionic Liquids"),
    Ie("Indicite"),
    Ir("Iridium"),
    Li("Lithium"),
    Nd("Neodymium"),
    Ne("Neon"),
    Ni("Nickel", "614325"),
    Pb("Lead", "454b51"),
    Pd("Palladium", "808080"),
    Pt("Platinum"),
    Pu("Plutonium", "808080"),
    RCOC("R-Coclaurine"),
    RCOOH("Carboxylic Acids", "2d343c", listOf("R-COOH")),
    Rc("Rothicite"),
    Sb("Antimony"),
    SiH3Cl("Chlorosilanes", "808080"),
    Ta("Tantalum"),
    Ti("Titanium", "808080"),
    Tsn("Tasine"),
    U("Uranium", "4b4815"),
    V("Vanadium"),
    Vr("Veryl"),
    Vy("Vytinium"),
    W("Tungsten", "808080"),
    Xe("Xenon"),
    Yb("Ytterbium"),
    xF4("Tetrafluorides"),
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