package edu.spectrum.utils

import java.io.File
import edu.spectrum.logging.Messager

object ExeFileExecutor {

  private def execute(exe: Array[String]) {
    val pb: ProcessBuilder = new ProcessBuilder(exe: _*)
    pb.directory(new File(exe(0)).getParentFile)
    val p: Process = pb.start
    try {
      p.waitFor
    } catch {
      case e: InterruptedException =>
        Messager.publish(s"Run ${exe.mkString("\n")} error! Trace:", e)
    }
    Messager.publish(s"Running ${exe.mkString("\n")} is done...")
  }

  def synthExecute() = execute(Array(new File(Constants.SYNTH_EXE).getCanonicalPath))

  def convolveExecute() = execute(Array(new File(Constants.CONV_EXE).getCanonicalPath))

}