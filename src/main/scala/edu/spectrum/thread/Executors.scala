package edu.spectrum.thread

import java.io.File

import edu.spectrum.logging.Messager
import edu.spectrum.state.SyntHelperState
import edu.spectrum.utils.Constants

trait Executor {

  protected def isExist(path: String): Boolean =
    new File(path) match {
      case f if f.exists() => true
      case _ =>
        Messager.publish(s"$path is not exist! Check workspace.")
        false
    }

}

trait SynthExecutor extends Executor {

  val state = SyntHelperState

  protected def isConvReady: Boolean = isExist(Constants.CONV_PATH) &&
    isExist(Constants.SYNTH_RES) &&
    isExist(Constants.DEF_BSN)

  protected def isSynthReady: Boolean = isExist(Constants.SYNTH_PATH) &&
    isExist(Constants.MODEL_PATH) &&
    isExist(Constants.ABN_PATH) && {
    val lns = state.synthSettings.binaryFilesPaths map isExist
    if (lns.size == 1)
      lns.head
    else
      false
  }
}
