package edu.spectrum.settings

import java.util.Locale

import edu.spectrum.model.{ChemicalElementsTable, Abundance}
import edu.spectrum.utils.{Constants, FileWorker}

import scala.io.Source
import scala.language.postfixOps

/**
 * Created by cf on 13.05.2015.
 */
trait Settings {
  def path: String

  def parse()

  def save()
}

class SynthSettings extends Settings {

  override def path: String = Constants.SYNTH_PATH

  var binaryFilesPaths: Set[String] = Set.empty
  var startSynth = 0
  var step = .01
  var endSynth = 0
  var vt = .0
  var printIdent = true

  override def parse() = {
    val br = Source.fromFile(path).bufferedReader()
    binaryFilesPaths = br.readLine.split("!")(0).trim.split(";").filter(s => !s.isEmpty).toSet
    br.readLine
    val str = br.readLine.split("!")(0).trim.split(" ")
    startSynth = str(0).trim.toInt
    step = str(1).trim.toDouble
    endSynth = str(2).trim.toInt
    vt = br.readLine.split("!")(0).trim.toDouble
    printIdent = br.readLine.split("!")(0).trim match {
      case "1" => true
      case _ => false
    }
    br.close()
  }

  override def save() = {
    val l =
      binaryFilesPaths.mkString(" ") ::
      "input.atl" ::
      "%d %1.4f %d".formatLocal(Locale.ENGLISH, startSynth, step, endSynth) ::
      "%1.2f".formatLocal(Locale.ENGLISH, vt) ::
      "%d".formatLocal(Locale.ENGLISH, if (printIdent) 1 else 0) ::
      "1.snt" :: "1.abn" :: "skip" :: "skip" :: "skip" ::
      Nil

    FileWorker.write(path, l)
  }

}

class ConvolveSettings extends Settings {

  override def path: String = Constants.CONV_PATH

  var vsini: Double = .0
  var vmacro: Double = .0
  var resolution: Int = 0

  override def parse() = {
    val br = Source.fromFile(path).bufferedReader()
    br.readLine
    vsini = br.readLine.split("!")(0).trim.toDouble
    vmacro = br.readLine.split("!")(0).trim.toDouble
    resolution = br.readLine.split("!")(0).trim.toInt
    br.close()
  }

  override def save() = {
    val l =
      "1.bsn" ::
      "%1.2f".formatLocal(Locale.ENGLISH, vsini) ::
      "%1.2f".formatLocal(Locale.ENGLISH, vmacro) ::
      "%d".formatLocal(Locale.ENGLISH, resolution) ::
      "1.rgs" ::
      Nil

    FileWorker.write(path, l)
  }

}

class AbundanceSettings extends Settings {

  private val defPath = Constants.DEF_SOLAR_ABN

  override def path: String = Constants.ABN_PATH

  var elements: List[Abundance] = List.empty

  override def parse() = {
    val strings = Source.fromFile(defPath).getLines()
    elements = List.empty
    while (strings.hasNext) {
      val symbols = strings.next().trim.split("\\s+")
      val values = strings.next().trim.split("\\s+")

      if (symbols.length == values.length)
        elements ++= {
          for{
            s <- symbols
            ch <- ChemicalElementsTable.find(s)
          } yield {
            Abundance(ch, values(symbols.indexOf(s)).toDouble)
          }
        }
    }
  }

  override def save() = {
    val max = elements.length
    val ret = (0 to 9) map {
      i =>
        val offset = 10 * i
        val limit = max - offset match {
          case d if d >= 10 => 10
          case d => d
        }
        val l = elements.drop(offset).take(limit)

        l.map(a => " " * 3 + a.element.symbol + (" " * (5 - a.element.symbol.length))).mkString("") ::
        l.map(a => s" ${"%1.4f".formatLocal(Locale.ENGLISH, a.value).take(6)} ").mkString("") ::
          Nil mkString "\n"
    } toList

    FileWorker.write(path, ret)
  }

}
