package edu.math.interpolation

import edu.math.util.functions.{HermiteFunction, LinearFunction, SplineFunction}
import org.apache.commons.math3.analysis.UnivariateFunction
import org.apache.commons.math3.analysis.interpolation.UnivariateInterpolator
import org.apache.commons.math3.exception.MathIllegalArgumentException

abstract class AbstractSplineInterpolator extends UnivariateInterpolator {
  protected def validate(xval: Array[Double], yval: Array[Double]) {
    if (xval == null || yval == null) throw new RuntimeException("Null arguments passed!")
    if (xval.length <= 1 || yval.length <= 1) throw new RuntimeException("At least two points must be given")
    if (xval.length != yval.length) throw new RuntimeException("The number of x differs from y")
    if (!isStrictlyIncreasing(xval)) throw new RuntimeException("The x values aren't increasing")
  }

  private def isStrictlyIncreasing(x: Array[Double]): Boolean = {
    def check(it: Int): Boolean = it match {
      case i if i < x.length =>
        x(i - 1) >= x(i) match {
          case true => false
          case false => check(i + 1)
        }
      case _ => true
    }

    check(1)
  }
}

class SplineLinearInterpolator extends AbstractSplineInterpolator {
  def interpolate(xval: Array[Double], yval: Array[Double]): UnivariateFunction = {
    validate(xval, yval)

    val functions: Array[UnivariateFunction] = (1 to xval.length - 1) map (i => new LinearFunction(xval(i - 1), xval(i), yval(i - 1), yval(i))) toArray

    new SplineFunction(xval, functions)
  }
}

class HermiteCubicSplineInterpolator extends AbstractSplineInterpolator {

  @throws(classOf[MathIllegalArgumentException])
  def interpolate(xval: Array[Double], yval: Array[Double]): UnivariateFunction = {
    validate(xval, yval)
    interpolate(xval, yval, findTangents(xval, yval))
  }

  protected def interpolate(xval: Array[Double], yval: Array[Double], tangents: Array[Double]): SplineFunction = {
    val functions: Array[UnivariateFunction] = (1 to xval.length - 1) map(i => new HermiteFunction(xval(i - 1), xval(i), yval(i - 1), yval(i), tangents(i - 1), tangents(i))) toArray
    val ret = new SplineFunction(xval, functions)
    ret
  }

  protected def findTangents(xval: Array[Double], yval: Array[Double]): Array[Double] = {
    val n = yval.length - 1
    (0 to n) map { i =>
      if(i == 0 || i == n)
        yval(i)
      else
        (yval(i + 1) - yval(i - 1)) / 2
    } toArray
  }
}

class AkimaCubicSplineInterpolator extends HermiteCubicSplineInterpolator {
  override  def validate(xval: Array[Double], yval: Array[Double]) = {
    super.validate(xval, yval)
    if (xval.length <= 4 || yval.length <= 4) throw new RuntimeException("At least five points must be given")
  }

  override  protected def findTangents(xval: Array[Double], yval: Array[Double]): Array[Double] = {
    val n = xval.length
    val diff = (0 to n - 1) map(i => if(i <= n - 2) (yval(i + 1) - yval(i)) / (xval(i + 1) - xval(i)) else 0.0)
    val w = (0 to n - 1) map(i => if(i > 0 && i <= n - 2) Math.abs(diff(i) - diff(i - 1)) else 0.0)
    val d = (0 to n) map {
      case 0 => diffthreepoint(xval(0), xval(0), yval(0), xval(1), yval(1), xval(2), yval(2))
      case 1 => diffthreepoint(xval(1), xval(0), yval(0), xval(1), yval(1), xval(2), yval(2))
      case i if i > 1 && i < n - 2 =>
        if (Math.abs(w(i - 1)) + Math.abs(w(i + 1)) != 0)
          (w(i + 1) * diff(i - 1) + w(i - 1) * diff(i)) / (w(i + 1) + w(i - 1))
        else
          ((xval(i + 1) - xval(i)) * diff(i - 1) + (xval(i) - xval(i - 1)) * diff(i)) / (xval(i + 1) - xval(i - 1))
      case i if i == n - 2 => diffthreepoint(xval(n - 2), xval(n - 3), yval(n - 3), xval(n - 2), yval(n - 2), xval(n - 1), yval(n - 1))
      case i if i == n - 1 => diffthreepoint(xval(n - 1), xval(n - 3), yval(n - 3), xval(n - 2), yval(n - 2), xval(n - 1), yval(n - 1))
      case _ => 0.0
    }
    d.toArray
  }

  private def diffthreepoint(t: Double, x0: Double, f0: Double, x1: Double, f1: Double, x2: Double, f2: Double) = {
    val _t = t - x0
    val _x1 = x1 - x0
    val _x2 = x2 - x0
    val a = (f2 - f0 - _x2 / _x1 * (f1 - f0)) / (Math.pow(_x2, 2) - _x1 * _x2)
    val b = (f1 - f0 - a * Math.pow(_x1, 2)) / _x1
    val res = 2 * a * _t + b
    res
  }
}