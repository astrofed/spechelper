package edu.spectrum.utils

import java.io.PrintWriter

import edu.spectrum.logging.Messager
import edu.spectrum.utils.Helpers._

import scala.io.Source


object FileWorker {

  def write(file: String, list: List[String]) = {
    val writer = new PrintWriter(file)
    writer.write(list.mkString("\n"))
    writer.close()
  }

  def read(file: String): List[String] = Source.fromFile(file).getLines().toList

  def getSortedDoubleData(file: String) = {
    val ret = read(file).tail flatMap {
      s =>
        val sa = s.trim.split("\\s+")
        if(sa.length > 1)
          tryo(Messager.publish("FileWorker.getSortedDoubleData() ", _))((sa(0).toDouble, sa(1).toDouble))
        else
          None
    }

    ret.sortBy(t => t._1)
  }

}