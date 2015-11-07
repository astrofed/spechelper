package edu.spectrum.gui

import java.awt.event.{InputEvent, MouseEvent}
import java.awt.{Color, Frame}
import java.lang.Short.MAX_VALUE
import java.util.Locale
import javax.swing.GroupLayout.Alignment
import javax.swing._

import edu.spectrum.logging.Messager
import edu.spectrum.utils.Helpers.tryo
import edu.spectrum.utils.{ChartBuilder, Constants, FileWorker}
import org.jfree.chart.JFreeChart
import org.jfree.chart.plot.XYPlot
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer
import org.jfree.data.xy.{XYSeries, XYSeriesCollection}

@SerialVersionUID(-845436371676510303L)
abstract class DataViewWindow(title: String) extends JFrame(title) with ChartWithStatusBarUpdater {
  val path = Constants.OBS_DATA
  val groupLayout: GroupLayout = new GroupLayout(getContentPane)
  lazy val obsChart = {
    val d = FileWorker.read(path).tail flatMap {
      s =>
        val sa = s.trim.split("\\s+")
        tryo(Messager.publish("DataViewWindow read obs file:", _))((sa(0).toDouble, sa(1).toDouble))
    }
    val l = d.map(_._1)
    val o = d.map(_._2)
    ChartBuilder.createObsSynthChart(l.head, o.min - 0.01, l(l.length - 1), o.max + 0.01, null, Map("Observed" -> l), Map("Observed" -> o), null)
  }

  def render() = {
    obsChart.getChart.addProgressListener(this)
    setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE)
    setSize(640, 480)
    import java.lang.Short
    groupLayout.setHorizontalGroup(groupLayout.createParallelGroup(Alignment.LEADING).addComponent(obsChart, GroupLayout.DEFAULT_SIZE, 624, Short.MAX_VALUE).addComponent(statusBar, GroupLayout.DEFAULT_SIZE, 624, Short.MAX_VALUE))
    groupLayout.setVerticalGroup(groupLayout.createParallelGroup(Alignment.LEADING).addGroup(groupLayout.createSequentialGroup.addComponent(obsChart, GroupLayout.DEFAULT_SIZE, 415, Short.MAX_VALUE).addComponent(statusBar, GroupLayout.PREFERRED_SIZE, 25, GroupLayout.PREFERRED_SIZE).addGap(2)))
    getContentPane.setLayout(groupLayout)
    setExtendedState(getExtendedState | Frame.MAXIMIZED_BOTH)
    setVisible(true)
  }

}

@SerialVersionUID(-845436371676510302L)
class SyntHelperChartWindow(title: String) extends DataViewWindow(title) {
  override  val path = Constants.DIV_RES
  private val tabbedPane: JTabbedPane = new JTabbedPane(SwingConstants.TOP)
  private[this] val data = FileWorker.read(path).tail map {
    s =>
      val sa = s.trim.split("\\s+")
      (sa(0).toDouble, sa(1).toDouble, sa(2).toDouble, sa(3).toDouble)
  }
  private[this] val l = data.map(_._1)
  private[this] val o = data.map(_._2)
  private[this] val s = data.map(_._3)
  private[this] val d = data.map(_._4)

  override lazy val obsChart = {
    val maxY: Double = (o.max, s.max) match {
      case (om, sm) if om > sm => om + 0.01
      case (_, sm) => sm + 0.01
    }
    val minY: Double = (o.min, s.min) match {
      case (om, sm) if om < sm => om - 0.01
      case (_, sm) => sm - 0.01
    }
    ChartBuilder.createObsSynthChart(l.head, minY, l(l.length - 1), maxY, null, Map("Observed" -> l, "Synthesized" -> l), Map("Observed" -> o, "Synthesized" -> s), null)
  }

  val divChart = {
    val maxY: Double = d.max + 0.01
    val minY: Double = d.min - 0.01
    ChartBuilder.createObsSynthChart(l.head, minY, l(l.length - 1), maxY, null, Map("Divided" -> l), Map("Divided" -> d), null)
  }

  override def render() = {
    obsChart.getChart.addProgressListener(this)
    divChart.getChart.addProgressListener(this)
    setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE)
    setSize(640, 480)
    tabbedPane.addTab("O - C spectra comparison", null, obsChart, null)
    tabbedPane.addTab("O/C divided spectrum", null, divChart, null)
    groupLayout.setHorizontalGroup(groupLayout.createParallelGroup(Alignment.LEADING).addComponent(tabbedPane, GroupLayout.DEFAULT_SIZE, 624, MAX_VALUE).addComponent(statusBar, GroupLayout.DEFAULT_SIZE, 624, MAX_VALUE))
    groupLayout.setVerticalGroup(groupLayout.createParallelGroup(Alignment.LEADING).addGroup(groupLayout.createSequentialGroup.addComponent(tabbedPane, GroupLayout.DEFAULT_SIZE, 415, MAX_VALUE).addComponent(statusBar, GroupLayout.PREFERRED_SIZE, 25, GroupLayout.PREFERRED_SIZE).addGap(2)))
    getContentPane.setLayout(groupLayout)
    setExtendedState(getExtendedState | Frame.MAXIMIZED_BOTH)
    setVisible(true)
  }

  render()

}

@SerialVersionUID(-845436371676510301L)
class SyntChartWindow(title: String) extends DataViewWindow(title) {
  private val tabbedPane: JTabbedPane = new JTabbedPane(SwingConstants.TOP)

  override lazy val obsChart = {
    val obs = FileWorker.read(path).tail map {
      s =>
        val sa = s.trim.split("\\s+")
        (sa(0).toDouble, sa(1).toDouble)
    }
    val l1 = obs.map(_._1)
    val d1 = obs.map(_._2)

    val synth = FileWorker.read(Constants.CONV_RES).tail map {
      s =>
        val sa = s.trim.split("\\s+")
        (sa(0).toDouble, sa(1).toDouble)
    }
    val l2 = synth.map(_._1)
    val d2 = synth.map(_._2)

    val maxY = (d1.max, d2.max) match {
      case (m1, m2) if m1 > m2 => m1 + 0.01
      case (m1, m2) if m1 < m2 => m2 + 0.01
      case _ => 0.01
    }
    val minY = (d1.min, d2.min) match {
      case (m1, m2) if m1 > m2 => m2 - 0.01
      case (m1, m2) if m1 < m2 => m1 - 0.01
      case _ => -0.01
    }
    val maxX = (l1.max, l2.max) match {
      case (m1, m2) if m1 > m2 => m1
      case (m1, m2) if m1 < m2 => m2
      case _ => 0
    }
    val minX = (l1.min, l2.min) match {
      case (m1, m2) if m1 > m2 => m2
      case (m1, m2) if m1 < m2 => m1
      case _ => 0
    }
    ChartBuilder.createObsSynthChart(minX, minY, maxX, maxY, null, Map("Observed" -> l1, "Synthesized" -> l2), Map("Observed" -> d1, "Synthesized" -> d2), null)
  }

  override def render() = {
    obsChart.getChart.addProgressListener(this)

    setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE)
    setSize(640, 480)
    tabbedPane.addTab("O - C spectra comparison", null, obsChart, null)
    groupLayout.setHorizontalGroup(groupLayout.createParallelGroup(Alignment.LEADING).addComponent(tabbedPane, GroupLayout.DEFAULT_SIZE, 624, MAX_VALUE).addComponent(statusBar, GroupLayout.DEFAULT_SIZE, 624, MAX_VALUE))
    groupLayout.setVerticalGroup(groupLayout.createParallelGroup(Alignment.LEADING).addGroup(groupLayout.createSequentialGroup.addComponent(tabbedPane, GroupLayout.DEFAULT_SIZE, 415, MAX_VALUE).addComponent(statusBar, GroupLayout.PREFERRED_SIZE, 25, GroupLayout.PREFERRED_SIZE).addGap(2)))
    getContentPane.setLayout(groupLayout)
    setExtendedState(getExtendedState | Frame.MAXIMIZED_BOTH)
    setVisible(true)
  }

  render()

}

import java.lang.{Double => jDouble}
@SerialVersionUID(-845436371676510304L)
class DIBWindow(title: String, p: String) extends DataViewWindow(title) {
  override val path = p
  private val listener = new IntegratorLimitsChartMouseClickListener

  override   lazy val obsChart = {
    val d = FileWorker.read(path).tail flatMap {
      s =>
        val sa = s.trim.split("\\s+")
        tryo(Messager.publish("DataViewWindow read obs file:", _))((sa(0).toDouble, sa(1).toDouble))
    }
    val l = d.map(_._1)
    val o = d.map(_._2)
    ChartBuilder.createObsSynthChart(l.head, o.min - 0.01, l(l.length - 1), o.max + 0.01, null, Map("Observed" -> l), Map("Observed" -> o), listener)
  }

  render()

  class IntegratorLimitsChartMouseClickListener extends ChartMouseClickListener {
    var limits = Set.empty[Double]

    private val dataset = new XYSeriesCollection
    private val renderer = {
      val r = new XYLineAndShapeRenderer(true, false)
      0 to 1 foreach(i => r.setSeriesPaint(i, Color.MAGENTA))
      r
    }

    override def init(c: JFreeChart) = {
      super.init(c)
      getXYPlot.foreach{
        p =>
          p.setDataset(1, dataset)
          p.setRenderer(1, renderer)
      }
    }

    def getXYPlot = chart.map(_.getPlot.asInstanceOf[XYPlot])

    private def updateDataSet() = {
      dataset.removeAllSeries()
      getXYPlot.foreach{
        p =>
          val axis = p.getRangeAxis
          val minY = axis.getLowerBound
          val maxY = axis.getUpperBound
          limits.toList.sorted.foreach {
            x =>
              val series: XYSeries = {
                val s: XYSeries = new XYSeries("%1.3f".formatLocal(Locale.ENGLISH, x))
                minY :: maxY :: Nil foreach (i => s.add(jDouble.valueOf(x), i))
                s
              }
              dataset.addSeries(series)
          }
      }
    }

    override def mouseClicked(x: Double, y: Double, trigger: MouseEvent): Unit = {
      if((trigger.getModifiersEx & InputEvent.SHIFT_DOWN_MASK) != 0) {
        if(limits.size < 2) {
          limits += x
          updateDataSet()
        } else {
          limits = Set.empty[Double]
          limits += x
          updateDataSet()
        }
      }
    }

    override def mouseOver(x: Double, y: Double, trigger: MouseEvent): Unit = {}
  }

}