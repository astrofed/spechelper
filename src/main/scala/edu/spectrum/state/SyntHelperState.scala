package edu.spectrum.state

import java.io.File
import java.util.Locale

import edu.spectrum.logging.Messager
import edu.spectrum.settings.{AbundanceSettings, ConvolveSettings, SynthSettings}
import edu.spectrum.utils.Constants._
import edu.spectrum.utils.{Constants, FileWorker, FilenameFilter}
import org.apache.commons.io.FileUtils

/**
 * Singleton for ClearDIBHelper state
 *
 * @author Федор
 */
object SyntHelperState {

  var workspacePath: String = ""
  val synthSettings: SynthSettings = new SynthSettings
  val abundanceSettings: AbundanceSettings = new AbundanceSettings
  val convolveSettings: ConvolveSettings = new ConvolveSettings

  private def handleFileList(wDir: File, filter: FilenameFilter, dest: File): Boolean = {
    val files: Array[String] = wDir.list(filter)
    if (files.length > 0) {
      files.headOption foreach {
        f =>
          val path = wDir + File.separator + f
          val src: File = new File(path)
          if (!src.exists())
            src.mkdirs()
          FileUtils.copyFile(src, dest)
          Messager.publish("SYNTHelperState - handleFileList", path + " to " + dest.getPath + " has been copied successfuly...")
      }
      true
    } else
      false
  }

  def handleWorkspace(path: String) = {
    workspacePath = path
    val wDir = new File(workspacePath)
    if (wDir.isDirectory) {
      if (!handleFileList(wDir, new FilenameFilter(".abn"), new File(Constants.ABN_PATH))) {
        FileUtils.copyFile(new File(Constants.DEF_SOLAR_ABN), new File(Constants.ABN_PATH))
        Messager.publish("SYNTHelperState - handleWorkspace", Constants.DEF_SOLAR_ABN + " to " + Constants.ABN_PATH + " has been copied successfuly... Default settings!")
      }
      if (!handleFileList(wDir, new FilenameFilter("synt.inp"), new File(Constants.SYNTH_PATH))) {
        FileUtils.copyFile(new File(Constants.DEF_SYNTH_INP), new File(Constants.SYNTH_PATH))
        Messager.publish("SYNTHelperState - handleWorkspace", Constants.DEF_SYNTH_INP + " to " + Constants.SYNTH_PATH + " has been copied successfuly... Default settings!")
      }
      if (!handleFileList(wDir, new FilenameFilter("input.atl"), new File(Constants.MODEL_PATH))) Messager.publish("SYNTHelperState - handleWorkspace", "Kurucz's format model file is not found... Some error? Check workspace directory or file names there...")
      if (!handleFileList(wDir, new FilenameFilter(".obs"), new File(Constants.OBS_DATA))) Messager.publish("SYNTHelperState - handleWorkspace", "Observational data file is not found... Some error? Check workspace directory or file names there...")
      if (!handleFileList(wDir, new FilenameFilter("convolve.conf"), new File(Constants.CONV_PATH))) {
        FileUtils.copyFile(new File(Constants.DEF_CONV_CONF), new File(Constants.CONV_PATH))
        Messager.publish("SYNTHelperState - handleWorkspace", Constants.DEF_CONV_CONF + " to " + Constants.CONV_PATH + " has been copied successfuly... Default settings!")
      }
    }
  }

  def resetLinePathList() = synthSettings.binaryFilesPaths = Set(new File(Constants.DEF_LNS).getCanonicalPath)

  def saveSettings() = {
    synthSettings :: convolveSettings :: abundanceSettings :: Nil foreach(_.save())
    Messager.publish("Saving Settings", "Settings have been saved successfuly...")
  }

  def copySettingsFilesToWorkspace() = {
    var src: File = new File(Constants.SYNTH_PATH)
    FileUtils.copyFile(src, new File(workspacePath + File.separator + src.getName))
    src = new File(Constants.CONV_PATH)
    FileUtils.copyFile(src, new File(workspacePath + File.separator + src.getName))
    src = new File(Constants.ABN_PATH)
    FileUtils.copyFile(src, new File(workspacePath + File.separator + src.getName))
    Messager.publish("Export Settings Files to Workspace", "Files have been copied successfuly...")
  }

  def handleObsData() = {
    val obs = FileWorker.getSortedDoubleData(Constants.OBS_DATA)
    synthSettings.startSynth = obs.headOption.map(_._1.floor.toInt) getOrElse 0
    synthSettings.endSynth = obs.lastOption.map(_._1.ceil.toInt) getOrElse 0
    FileWorker.write(Constants.OBS_DATA, obs.map { case (l, f) => "%1.4f\t%1.4f".formatLocal(Locale.ENGLISH, l, f) })
  }

  def clear() = List(SYNTH_RES, CONV_RES, DIV_RES, OBS_DATA, MODEL_PATH).foreach(new File(_).delete())

}