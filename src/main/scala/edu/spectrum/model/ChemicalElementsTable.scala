package edu.spectrum.model

/**
 * Created by cf on 17.05.2015.
 */
object ChemicalElementsTable {

  sealed abstract class ChemicalElement(
                                         val atomicMass: Double,
                                         val name: String,
                                         val symbol: String,
                                         val atomicNumber: Int) {
    def compare(that: ChemicalElement) = this.atomicNumber - that.atomicNumber

    override def toString = name
  }

  import Double.NaN

  case object H extends ChemicalElement(1.0079, "Hydrogen", "H", 1)

  case object He extends ChemicalElement(4.0026, "Helium", "He", 2)

  case object Li extends ChemicalElement(6.941, "Lithium", "Li", 3)

  case object Be extends ChemicalElement(9.0122, "Beryllium", "Be", 4)

  case object B extends ChemicalElement(10.811, "Boron", "B", 5)

  case object C extends ChemicalElement(12.0107, "Carbon", "C", 6)

  case object N extends ChemicalElement(14.0067, "Nitrogen", "N", 7)

  case object O extends ChemicalElement(15.9994, "Oxygen", "O", 8)

  case object F extends ChemicalElement(18.9984, "Fluorine", "F", 9)

  case object Ne extends ChemicalElement(20.1797, "Neon", "Ne", 10)

  case object Na extends ChemicalElement(22.9897, "Sodium", "Na", 11)

  case object Mg extends ChemicalElement(24.305, "Magnesium", "Mg", 12)

  case object Al extends ChemicalElement(26.9815, "Aluminum", "Al", 13)

  case object Si extends ChemicalElement(28.0855, "Silicon", "Si", 14)

  case object P extends ChemicalElement(30.9738, "Phosphorus", "P", 15)

  case object S extends ChemicalElement(32.065, "Sulfur", "S", 16)

  case object Cl extends ChemicalElement(35.453, "Chlorine", "Cl", 17)

  case object K extends ChemicalElement(39.0983, "Potassium", "K", 19)

  case object Ar extends ChemicalElement(39.948, "Argon", "Ar", 18)

  case object Ca extends ChemicalElement(40.078, "Calcium", "Ca", 20)

  case object Sc extends ChemicalElement(44.9559, "Scandium", "Sc", 21)

  case object Ti extends ChemicalElement(47.867, "Titanium", "Ti", 22)

  case object V extends ChemicalElement(50.9415, "Vanadium", "V", 23)

  case object Cr extends ChemicalElement(51.9961, "Chromium", "Cr", 24)

  case object Mn extends ChemicalElement(54.938, "Manganese", "Mn", 25)

  case object Fe extends ChemicalElement(55.845, "Iron", "Fe", 26)

  case object Ni extends ChemicalElement(58.6934, "Nickel", "Ni", 28)

  case object Co extends ChemicalElement(58.9332, "Cobalt", "Co", 27)

  case object Cu extends ChemicalElement(63.546, "Copper", "Cu", 29)

  case object Zn extends ChemicalElement(65.39, "Zinc", "Zn", 30)

  case object Ga extends ChemicalElement(69.723, "Gallium", "Ga", 31)

  case object Ge extends ChemicalElement(72.64, "Germanium", "Ge", 32)

  case object As extends ChemicalElement(74.9216, "Arsenic", "As", 33)

  case object Se extends ChemicalElement(78.96, "Selenium", "Se", 34)

  case object Br extends ChemicalElement(79.904, "Bromine", "Br", 35)

  case object Kr extends ChemicalElement(83.8, "Krypton", "Kr", 36)

  case object Rb extends ChemicalElement(85.4678, "Rubidium", "Rb", 37)

  case object Sr extends ChemicalElement(87.62, "Strontium", "Sr", 38)

  case object Y extends ChemicalElement(88.9059, "Yttrium", "Y", 39)

  case object Zr extends ChemicalElement(91.224, "Zirconium", "Zr", 40)

  case object Nb extends ChemicalElement(92.9064, "Niobium", "Nb", 41)

  case object Mo extends ChemicalElement(95.94, "Molybdenum", "Mo", 42)

  case object Tc extends ChemicalElement(98, "Technetium", "Tc", 43)

  case object Ru extends ChemicalElement(101.07, "Ruthenium", "Ru", 44)

  case object Rh extends ChemicalElement(102.9055, "Rhodium", "Rh", 45)

  case object Pd extends ChemicalElement(106.42, "Palladium", "Pd", 46)

  case object Ag extends ChemicalElement(107.8682, "Silver", "Ag", 47)

  case object Cd extends ChemicalElement(112.411, "Cadmium", "Cd", 48)

  case object In extends ChemicalElement(114.818, "Indium", "In", 49)

  case object Sn extends ChemicalElement(118.71, "Tin", "Sn", 50)

  case object Sb extends ChemicalElement(121.76, "Antimony", "Sb", 51)

  case object I extends ChemicalElement(126.9045, "Iodine", "I", 53)

  case object Te extends ChemicalElement(127.6, "Tellurium", "Te", 52)

  case object Xe extends ChemicalElement(131.293, "Xenon", "Xe", 54)

  case object Cs extends ChemicalElement(132.9055, "Cesium", "Cs", 55)

  case object Ba extends ChemicalElement(137.327, "Barium", "Ba", 56)

  case object La extends ChemicalElement(138.9055, "Lanthanum", "La", 57)

  case object Ce extends ChemicalElement(140.116, "Cerium", "Ce", 58)

  case object Pr extends ChemicalElement(140.9077, "Praseodymium", "Pr", 59)

  case object Nd extends ChemicalElement(144.24, "Neodymium", "Nd", 60)

  case object Pm extends ChemicalElement(145, "Promethium", "Pm", 61)

  case object Sm extends ChemicalElement(150.36, "Samarium", "Sm", 62)

  case object Eu extends ChemicalElement(151.964, "Europium", "Eu", 63)

  case object Gd extends ChemicalElement(157.25, "Gadolinium", "Gd", 64)

  case object Tb extends ChemicalElement(158.9253, "Terbium", "Tb", 65)

  case object Dy extends ChemicalElement(162.5, "Dysprosium", "Dy", 66)

  case object Ho extends ChemicalElement(164.9303, "Holmium", "Ho", 67)

  case object Er extends ChemicalElement(167.259, "Erbium", "Er", 68)

  case object Tm extends ChemicalElement(168.9342, "Thulium", "Tm", 69)

  case object Yb extends ChemicalElement(173.04, "Ytterbium", "Yb", 70)

  case object Lu extends ChemicalElement(174.967, "Lutetium", "Lu", 71)

  case object Hf extends ChemicalElement(178.49, "Hafnium", "Hf", 72)

  case object Ta extends ChemicalElement(180.9479, "Tantalum", "Ta", 73)

  case object W extends ChemicalElement(183.84, "Tungsten", "W", 74)

  case object Re extends ChemicalElement(186.207, "Rhenium", "Re", 75)

  case object Os extends ChemicalElement(190.23, "Osmium", "Os", 76)

  case object Ir extends ChemicalElement(192.217, "Iridium", "Ir", 77)

  case object Pt extends ChemicalElement(195.078, "Platinum", "Pt", 78)

  case object Au extends ChemicalElement(196.9665, "Gold", "Au", 79)

  case object Hg extends ChemicalElement(200.59, "Mercury", "Hg", 80)

  case object Tl extends ChemicalElement(204.3833, "Thallium", "Tl", 81)

  case object Pb extends ChemicalElement(207.2, "Lead", "Pb", 82)

  case object Bi extends ChemicalElement(208.9804, "Bismuth", "Bi", 83)

  case object Po extends ChemicalElement(209, "Polonium", "Po", 84)

  case object At extends ChemicalElement(210, "Astatine", "At", 85)

  case object Rn extends ChemicalElement(222, "Radon", "Rn", 86)

  case object Fr extends ChemicalElement(223, "Francium", "Fr", 87)

  case object Ra extends ChemicalElement(226, "Radium", "Ra", 88)

  case object Ac extends ChemicalElement(227, "Actinium", "Ac", 89)

  case object Pa extends ChemicalElement(231.0359, "Protactinium", "Pa", 91)

  case object Th extends ChemicalElement(232.0381, "Thorium", "Th", 90)

  case object Np extends ChemicalElement(237, "Neptunium", "Np", 93)

  case object U extends ChemicalElement(238.0289, "Uranium", "U", 92)

  case object Am extends ChemicalElement(243, "Americium", "Am", 95)

  case object Pu extends ChemicalElement(244, "Plutonium", "Pu", 94)

  case object Cm extends ChemicalElement(247, "Curium", "Cm", 96)

  case object Bk extends ChemicalElement(247, "Berkelium", "Bk", 97)

  case object Cf extends ChemicalElement(251, "Californium", "Cf", 98)

  case object Es extends ChemicalElement(252, "Einsteinium", "Es", 99)

  case object Fm extends ChemicalElement(257, "Fermium", "Fm", 100)

  case object Md extends ChemicalElement(258, "Mendelevium", "Md", 101)

  case object No extends ChemicalElement(259, "Nobelium", "No", 102)

  case object Rf extends ChemicalElement(261, "Rutherfordium", "Rf", 104)

  case object Lr extends ChemicalElement(262, "Lawrencium", "Lr", 103)

  case object Db extends ChemicalElement(262, "Dubnium", "Db", 105)

  case object Bh extends ChemicalElement(264, "Bohrium", "Bh", 107)

  case object Sg extends ChemicalElement(266, "Seaborgium", "Sg", 106)

  case object Mt extends ChemicalElement(268, "Meitnerium", "Mt", 109)

  case object Rg extends ChemicalElement(272, "Roentgenium", "Rg", 111)

  case object Hs extends ChemicalElement(277, "Hassium", "Hs", 108)

  case object Ds extends ChemicalElement(NaN, "Darmstadtium", "Ds", 110)

  case object Uub extends ChemicalElement(NaN, "Ununbium", "Uub", 112)

  case object Uut extends ChemicalElement(NaN, "Ununtrium", "Uut", 113)

  case object Uuq extends ChemicalElement(NaN, "Ununquadium", "Uuq", 114)

  case object Uup extends ChemicalElement(NaN, "Ununpentium", "Uup", 115)

  case object Uuh extends ChemicalElement(NaN, "Ununhexium", "Uuh", 116)

  case object Uus extends ChemicalElement(NaN, "Ununseptium", "Uus", 117)

  case object Uuo extends ChemicalElement(NaN, "Ununoctium", "Uuo", 118)

  val CHEMICAL_ELEMENTS: Set[ChemicalElement] = Set(H, He, Li, Be, B, C, N, O, F, Ne, Na, Mg, Al, Si, P, S, Cl, K, Ar, Ca, Sc, Ti, V, Cr, Mn, Fe, Ni, Co, Cu, Zn, Ga, Ge, As, Se, Br, Kr, Rb, Sr, Y, Zr, Nb, Mo, Tc, Ru, Rh, Pd, Ag, Cd, In, Sn, Sb, I, Te, Xe, Cs, Ba, La, Ce, Pr, Nd, Pm, Sm, Eu, Gd, Tb, Dy, Ho, Er, Tm, Yb, Lu, Hf, Ta, W, Re, Os, Ir, Pt, Au, Hg, Tl, Pb, Bi, Po, At, Rn, Fr, Ra, Ac, Pa, Th, Np, U, Am, Pu, Cm, Bk, Cf, Es, Fm, Md, No, Rf, Lr, Db, Bh, Sg, Mt, Rg, Hs, Ds, Uub, Uut, Uuq, Uup, Uuh, Uus, Uuo)

  def find(symbol: String) = CHEMICAL_ELEMENTS.find(c => c.symbol.toUpperCase == symbol.toUpperCase)

}
