package edu.spectrum.utils

import java.awt.geom.Rectangle2D
import java.awt.{BasicStroke, Color, Point}
import java.lang.{Double => jDouble}
import java.text.{DecimalFormat, NumberFormat}
import java.util.Locale

import edu.spectrum.gui.ChartMouseClickListener
import org.apache.commons.math3.analysis.integration.{TrapezoidIntegrator, UnivariateIntegrator}
import org.jfree.chart._
import org.jfree.chart.axis.NumberAxis
import org.jfree.chart.labels.StandardXYToolTipGenerator
import org.jfree.chart.plot.{PlotOrientation, XYPlot}
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer
import org.jfree.data.xy.{XYSeries, XYSeriesCollection}

/**
 * Created by cf on 12.05.2015.
 */
object Constants {
  val DEF_SYNTH_INP: String = "./common/defs.inp"
  val DEF_SOLAR_ABN: String = "./common/defs.abn"
  val DEF_CONV_CONF: String = "./common/dc.conf"

  val DEF_BSN: String = "./common/1.bsn"
  val DEF_LNS: String = "./common/vald.lns"

  val MODEL_PATH: String = "./common/input.atl"
  val OBS_DATA: String = "./common/1.obs"
  val ABN_PATH: String = "./common/1.abn"

  val CONV_PATH: String = "./common/convolve.conf"
  val SYNTH_PATH: String = "./common/synt.inp"
  val SYNTH_EXE: String = "./common/synth.exe"
  val CONV_EXE: String = "./common/conv.exe"
  val SYNTH_RES: String = "./common/1.snt"
  val CONV_RES: String = "./common/1.rgs"
  val DIV_RES: String = "./common/res.txt"
}

object Helpers {
  def tryo[T](onError: Throwable => Unit = _ => Unit)
             (f: => T): Option[T] = {
    try {
      Some(f)
    } catch {
      case c: Throwable =>
        onError(c)
        None
    }
  }
}

object ChartBuilder {

  private val INTEGRATOR: UnivariateIntegrator = new TrapezoidIntegrator

  //	public static ChartPanel createChart(double[] x, double[] y, List<UnivariateFunction> functions,
  //			double maxX, double minX, double discreteness, String chartLabel) throws FunctionEvaluationException {
  //
  //		return createChart(x, y, functions, maxX, minX, discreteness, chartLabel, null);
  //	}
  def createObsSynthChart(minX: Double, minY: Double, maxX: Double, maxY: Double, chartLabel: String, xs: Map[String, List[Double]], ys: Map[String, List[Double]], listener: ChartMouseClickListener): ChartPanel = {
    val xyDataset: XYSeriesCollection = new XYSeriesCollection
    xs foreach {
      case (title, x) =>
        val pointSeries: XYSeries = new XYSeries(title)
        val y = ys.getOrElse(title, Nil)
        0 to x.length - 1 foreach(i => pointSeries.add(jDouble.valueOf(x(i).toString), jDouble.valueOf(y(i).toString)))
        xyDataset.addSeries(pointSeries)
    }
    val series: XYSeries = {
      val series: XYSeries = new XYSeries("Continuum")
      minX :: maxX :: Nil foreach(i => series.add(jDouble.valueOf(i), 1))
      series
    }
    xyDataset.addSeries(series)
    val chart: JFreeChart = ChartFactory.createXYLineChart(chartLabel, "\u03BB", "r", xyDataset, PlotOrientation.VERTICAL, true, true, false)
    chart.getXYPlot.getDomainAxis.setUpperBound(maxX)
    chart.getXYPlot.getDomainAxis.setLowerBound(minX)
    chart.getXYPlot.getRangeAxis.setUpperBound(maxY)
    chart.getXYPlot.getRangeAxis.setLowerBound(minY)
    val numberAxis: NumberAxis = chart.getXYPlot.getRangeAxis.asInstanceOf[NumberAxis]
    val formatter: DecimalFormat = NumberFormat.getNumberInstance(Locale.ENGLISH).asInstanceOf[DecimalFormat]
    formatter.applyPattern("# ##0.000")
    numberAxis.setNumberFormatOverride(formatter)
    val renderer: XYLineAndShapeRenderer = chart.getXYPlot.getRenderer.asInstanceOf[XYLineAndShapeRenderer]
    renderer.setSeriesPaint(0, Color.RED)

    0 to 2 foreach {
      i =>
        renderer.setSeriesLinesVisible(i, true)
        renderer.setSeriesShapesVisible(i, false)
        if (i > 0) renderer.setSeriesPaint(i, Color.BLACK)
    }

    val nf: DecimalFormat = NumberFormat.getNumberInstance(Locale.ENGLISH).asInstanceOf[DecimalFormat]
    nf.applyPattern("# ##0.0000")
    renderer.setBaseToolTipGenerator(new StandardXYToolTipGenerator("\u03BB: {1}\u212B  r: {2}", formatter, nf))
    createChartPanel(listener, chart)
  }

  private def createChartPanel(listener: ChartMouseClickListener, chart: JFreeChart): ChartPanel = {
    val plot: XYPlot = chart.getXYPlot
    plot.setDomainCrosshairVisible(true)
    plot.setRangeCrosshairVisible(true)
    plot.setDomainCrosshairLockedOnData(true)
    plot.setRangeCrosshairLockedOnData(true)
    plot.setDomainZeroBaselineVisible(true)
    plot.setRangeZeroBaselineVisible(true)
    val baseLineStroke: BasicStroke = new BasicStroke(2, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL)
    plot.setDomainZeroBaselinePaint(Color.BLACK)
    plot.setDomainZeroBaselineStroke(baseLineStroke)
    plot.setRangeZeroBaselinePaint(Color.BLACK)
    plot.setRangeZeroBaselineStroke(baseLineStroke)
    plot.setBackgroundPaint(Color.WHITE)
    plot.setDomainPannable(true)
    val chartPanel: ChartPanel = new ChartPanel(chart)
    if (listener != null) {
      listener.init(chart)
      chartPanel.addChartMouseListener(
        new ChartMouseListener {
          private def handleChartEvent(event: ChartMouseEvent) = {
            val trigger = event.getTrigger
            val anchor: Point = trigger.getPoint
            val plot: XYPlot = chartPanel.getChart.getXYPlot
            val plotArea: Rectangle2D = chartPanel.getScreenDataArea
            val x: Double = plot.getDomainAxis.java2DToValue(anchor.getX, plotArea, plot.getDomainAxisEdge)
            val y: Double = plot.getRangeAxis.java2DToValue(anchor.getY, plotArea, plot.getRangeAxisEdge)
            (x, y, trigger)
          }

          def chartMouseClicked(event: ChartMouseEvent) {
            val r = handleChartEvent(event)
            listener.mouseClicked(r._1, r._2, r._3)
          }

          def chartMouseMoved(event: ChartMouseEvent) {
            val r = handleChartEvent(event)
            listener.mouseOver(r._1, r._2, r._3)
          }
        })
    }
    chartPanel.setMouseWheelEnabled(true)
    chartPanel
  }

}