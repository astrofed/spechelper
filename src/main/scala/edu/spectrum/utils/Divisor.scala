package edu.spectrum.utils

import java.util.Locale

import edu.math.interpolation.AkimaCubicSplineInterpolator
import org.apache.commons.math3.analysis.UnivariateFunction

object Divisor {

  def divide(obs: String, synth: String): List[String] = {
    val od = FileWorker.getSortedDoubleData(obs)
    val sd = FileWorker.getSortedDoubleData(synth)

    val f: UnivariateFunction = new AkimaCubicSplineInterpolator().interpolate(sd.map(t => t._1).toArray, sd.map(t => t._2).toArray)

    "%s\t%s\t%s\t%s".formatLocal(Locale.ENGLISH, "Lambda  ", "R(o)    ", "R(s)    ", "R(o)/R(s)") ::
      od.map {
        case (l, v) =>
          val d: Double = f.value(l)
          "%1.4f\t%1.6f\t%1.6f\t%1.6f".formatLocal(Locale.ENGLISH, l, v, d, v / d)
      }
  }

}