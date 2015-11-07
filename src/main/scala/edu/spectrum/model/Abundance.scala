package edu.spectrum.model

import edu.spectrum.model.ChemicalElementsTable.ChemicalElement

/**
 * Created by cf on 13.05.2015.
 */
case class Abundance(val element: ChemicalElement, var value: Double)