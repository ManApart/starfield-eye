enum class ResourceType(val readableName: String, val aliases: List<String> = listOf()) {
    Ad("Aldumite"),
    Ag("Silver"),
    Al("Aluminum"),
    Ar("Argon"),
    Au("Gold"),
    Be("Beryllium"),
    C6Hn("Benzene"),
    Cl("Chlorine"),
    Co("Cobalt"),
    Cs("Caesium"),
    Ct("Caelumite"),
    Cu("Copper"),
    Dy("Dysprosium"),
    Eu("Europium"),
    F("Fluorine"),
    Fe("Iron"),
    H2O("Water", listOf("H20")),
    He3("Helium-3", listOf("He-3")),
    Hg("Mercury"),
    HnCn("Alkanes"),
    IL("Ionic Liquids"),
    Ie("Indicite"),
    Ir("Iridium"),
    Li("Lithium"),
    Nd("Neodymium"),
    Ne("Neon"),
    Ni("Nickel"),
    Pb("Lead"),
    Pd("Palladium"),
    Pt("Platinum"),
    Pu("Plutonium"),
    RCOC("R-Coclaurine"),
    RCOOH("Carboxylic Acids", listOf("R-COOH")),
    Rc("Rothicite"),
    Sb("Antimony"),
    SiH3Cl("Chlorosilanes"),
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