package edu.math.util.functions

import java.util

import org.apache.commons.math3.analysis.UnivariateFunction

class LinearFunction(val x1: Double, val x2: Double, val y1: Double, val y2: Double) extends UnivariateFunction {
  override def value(x: Double): Double = {
    y1 + ((y2 - y1) / (x2 - x1)) * (x - x1)
  }
}

class HermiteFunction(val xK: Double, val xK1: Double, val yK: Double, val yK1: Double, val mK: Double, val mK1: Double) extends UnivariateFunction {
  def h00(t: Double) = {
    2 * Math.pow(t, 3) - 3 * Math.pow(t, 2) + 1
  }

  def h10(t: Double) = {
    t * Math.pow(1 - t, 2)
  }

  def h01(t: Double) = {
    Math.pow(t, 2) * (3 - 2 * t)
  }

  def h11(t: Double) = {
    Math.pow(t, 2) * (t - 1)
  }

  override def value(x: Double): Double = {
    val h: Double = xK1 - xK
    val t: Double = (x - xK) / h
    h00(t) * yK + h10(t) * h * mK + h01(t) * yK1 + h11(t) * h * mK1
  }
}

class SplineFunction(kns: Array[Double], fns: Array[UnivariateFunction]) extends UnivariateFunction {

  if (kns.length < 2) throw new RuntimeException(s"spline partition must have at least 2 points, got ${kns.length}")
  if (kns.length - 1 != fns.length) throw new RuntimeException(s"number of interpolants must match the number of segments (${fns.length} != ${kns.length} - 1)")
  if (!isStrictlyIncreasing(kns)) new RuntimeException("knot values must be strictly increasing")

  val n = kns.length - 1
  val knots = new Array[Double](n + 1)

  System.arraycopy(kns, 0, knots, 0, n + 1)

  val functions = new Array[UnivariateFunction](n)

  System.arraycopy(fns, 0, functions, 0, n)

  def value(x: Double): Double = x match {
    case v if v < knots(0) || v > knots(n) => Double.NaN
    case _ =>
      val i: Int = util.Arrays.binarySearch(knots, x) match {
        case v if v < 0 => -v - 2
        case v if v >= functions.length => v - 1
        case v => v
      }
      functions(i).value(x)
  }

  private def isStrictlyIncreasing(x: Array[Double]): Boolean = {
    def check(it: Int): Boolean = it match {
      case i if i < x.length =>
        if(x(i - 1) >= x(i))
          false
        else
          check(i + 1)
      case _ => true
    }

    check(1)
  }
}