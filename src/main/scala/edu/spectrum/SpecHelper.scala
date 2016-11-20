package edu.spectrum

import java.awt.Frame
import javax.swing._

import edu.spectrum.gui.SpecHelperFrame
import edu.spectrum.logging.Messager

object SpecHelper extends App {
    try {
      UIManager
        .getInstalledLookAndFeels
        .find(_.getName == "Windows")
        .foreach(info => UIManager.setLookAndFeel(info.getClassName))
    }
    catch {
      case ex: ClassNotFoundException =>
        Messager.publish("SYNTHelper - main", ex)
      case ex: InstantiationException =>
        Messager.publish("SYNTHelper - main", ex)
      case ex: IllegalAccessException =>
        Messager.publish("SYNTHelper - main", ex)
      case ex: UnsupportedLookAndFeelException =>
        Messager.publish("SYNTHelper - main", ex)
    }

    /* Create and display the form */
    java.awt.EventQueue.invokeLater(
      new Runnable() {
        def run() {
          val app: SpecHelperFrame = new SpecHelperFrame
          app.setVisible(true)
          app.setExtendedState(app.getExtendedState | Frame.MAXIMIZED_BOTH)
        }
      })
}