enum class ResourceType(val readableName: String, val color: String = "2d343c", val aliases: List<String> = listOf()) {
    Ad("Aldumite"),
    Ag("Silver"),
    Al("Aluminum"),
    Ar("Argon"),
    Au("Gold"),
    Be("Beryllium"),
    C6Hn("Benzene"),
    Cl("Chlorine", "404753"),
    Co("Cobalt"),
    Cs("Caesium"),
    Ct("Caelumite"),
    Cu("Copper"),
    Dy("Dysprosium"),
    Eu("Europium"),
    F("Fluorine"),
    Fe("Iron"),
    H2O("Water", "63a39d", listOf("H20")),
    He3("Helium-3", "2d343c", listOf("He-3")),
    Hg("Mercury", "808080"),
    HnCn("Alkanes"),
    IL("Ionic Liquids"),
    Ie("Indicite"),
    Ir("Iridium"),
    Li("Lithium"),
    Nd("Neodymium"),
    Ne("Neon"),
    Ni("Nickel"),
    Pb("Lead", "454b51"),
    Pd("Palladium"),
    Pt("Platinum"),
    Pu("Plutonium"),
    RCOC("R-Coclaurine"),
    RCOOH("Carboxylic Acids", "2d343c", listOf("R-COOH")),
    Rc("Rothicite"),
    Sb("Antimony"),
    SiH3Cl("Chlorosilanes", "808080"),
    Ta("Tantalum"),
    Ti("Titanium"),
    Tsn("Tasine"),
    U("Uranium"),
    V("Vanadium"),
    Vr("Veryl"),
    Vy("Vytinium"),
    W("Tungsten"),
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