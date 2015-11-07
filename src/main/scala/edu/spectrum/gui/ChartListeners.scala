package edu.spectrum.gui

import java.awt.event.MouseEvent
import java.util.Locale

import org.jfree.chart.JFreeChart
import org.jfree.chart.event.{ChartProgressEvent, ChartProgressListener}
import org.jfree.chart.plot.XYPlot

/**
 * Created by cf on 31.05.2015.
 */
trait ChartWithStatusBarUpdater extends ChartProgressListener {

  val statusBar: StatusBar = new StatusBar(" ")

  override def chartProgress(event: ChartProgressEvent) =
    if (event.getType == 2) {
      val chart: JFreeChart = event.getChart
      if (chart != null) {
        val plot: XYPlot = chart.getXYPlot
        statusBar.setInfo("\u03BB: %4.3f \u212B  r: %1.4f".formatLocal(Locale.ENGLISH, plot.getDomainCrosshairValue, plot.getRangeCrosshairValue))
      }
    }

}

trait ChartMouseClickListener {
  protected var chart: Option[JFreeChart] = None
  def init(c: JFreeChart) = {chart = Option(c)}
  def mouseClicked (x: Double, y: Double, trigger: MouseEvent)
  def mouseOver (x: Double, y: Double, trigger: MouseEvent)
}
