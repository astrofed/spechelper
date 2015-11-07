package edu.spectrum.gui

import java.awt.event.{ActionEvent, ActionListener}
import java.io.File
import java.lang.Short.MAX_VALUE
import java.util.Locale
import javax.swing.GroupLayout.Alignment
import javax.swing._
import javax.swing.filechooser.FileNameExtensionFilter

import edu.spectrum.BuildInfo
import edu.spectrum.logging.{LogPublisher, Messager}
import edu.spectrum.state.SyntHelperState
import edu.spectrum.state.SyntHelperState._
import edu.spectrum.thread.{ExecuteSynthConvThread, ExecuteSynthConvDivThread}
import edu.spectrum.utils.Constants
import org.apache.commons.io.FileUtils

/**
 * Created by cf on 01.06.2015.
 */
@SerialVersionUID(-2415943730144176337L)
class SpecHelperFrame extends JFrame {
  lazy val synthPanel: GeneralSynthPanel = new GeneralSynthPanel
  lazy val abundancesPanel: AbundancesPanel = new AbundancesPanel
  val tabbedPane: JTabbedPane = new JTabbedPane

  private def initSettings() = {
    synthSettings :: convolveSettings :: abundanceSettings :: Nil foreach(_.parse())
    if (new File(Constants.OBS_DATA).exists)
      handleObsData()
    resetLinePathList()
    synthSettings.save()
  }

  def setValues() = {
    SpecHelperFrame.statusBar.setMainMessage(workspacePath)
    synthPanel.setValues()
    abundancesPanel.setValues()
  }

  private def controls() = {
    setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE)
    addWindowListener(new java.awt.event.WindowAdapter() {
      override def windowClosing(evt: java.awt.event.WindowEvent): Unit = exit()
    });
    setTitle("%s v. %s".formatLocal(Locale.ENGLISH, BuildInfo.name, BuildInfo.version))
    tabbedPane.setToolTipText("SYNTH Settings")
    tabbedPane.addTab("Synthesis", synthPanel)
    tabbedPane.addTab("Abundances", abundancesPanel)
    val layout: GroupLayout = new GroupLayout(getContentPane)
    layout.setHorizontalGroup(layout.createParallelGroup(Alignment.LEADING).addComponent(tabbedPane, GroupLayout.DEFAULT_SIZE, 640, MAX_VALUE).addComponent(SpecHelperFrame.statusBar, GroupLayout.DEFAULT_SIZE, 640, MAX_VALUE))
    layout.setVerticalGroup(layout.createParallelGroup(Alignment.TRAILING).addGroup(layout.createSequentialGroup.addContainerGap().addComponent(tabbedPane, GroupLayout.DEFAULT_SIZE, 357, MAX_VALUE).addComponent(SpecHelperFrame.statusBar, GroupLayout.PREFERRED_SIZE, 25, GroupLayout.PREFERRED_SIZE)))
    getContentPane.setLayout(layout)
    tabbedPane.getAccessibleContext.setAccessibleDescription("")
    pack()
  }

  private def menu() = {
    val menuBar: JMenuBar = new JMenuBar
    setJMenuBar(menuBar)
    val mnFile: JMenu = new JMenu("File")
    menuBar.add(mnFile)
    val mntmOpenWorkspace: JMenuItem = new JMenuItem("Open Workspace...")
    mntmOpenWorkspace.addActionListener(new ActionListener {
      def actionPerformed(e: ActionEvent) {
        new Thread(new Runnable {
          def run() = {
            SyntHelperState.clear()
            val path: String = SpecHelperFrame.openDialogFileChooser(SyntHelperState.workspacePath, dirOnly = true, "Choose Workspace", "Choose", "")
            SyntHelperState.handleWorkspace(path)
            initSettings()
            setValues()
          }
        }).start()
      }
    })
    mnFile.add(mntmOpenWorkspace)
    val mntmApplyChanges: JMenuItem = new JMenuItem("Save Changes...")
    mntmApplyChanges.addActionListener {
      new ActionListener {
        def actionPerformed(arg0: ActionEvent) {
          new Thread(new Runnable {
            def run() = SyntHelperState.saveSettings()
          }).start()
        }
      }
    }
    mnFile.add(mntmApplyChanges)
    val mntmExportConfFiles: JMenuItem = new JMenuItem("Export Settings Files to Workspace...")
    mntmExportConfFiles.addActionListener {
      new ActionListener {
        def actionPerformed(arg0: ActionEvent) {
          new Thread(new Runnable {
            def run() =  {
              SyntHelperState.saveSettings()
              SyntHelperState.copySettingsFilesToWorkspace()
            }
          }).start()
        }
      }
    }
    mnFile.add(mntmExportConfFiles)
    mnFile.add(new JSeparator)
    val mntmExit: JMenuItem = new JMenuItem("Exit")
    mntmExit.addActionListener(new ActionListener {
      def actionPerformed(arg0: ActionEvent) = exit()
    })
    mnFile.add(mntmExit)
    val mnOperations: JMenu = new JMenu("Operations")
    menuBar.add(mnOperations)
    val mntmViewObs: JMenuItem = new JMenuItem("View")
    mntmViewObs.setToolTipText("Observed data (1.obs) will be viewed")
    mntmViewObs.addActionListener(new ActionListener {
      def actionPerformed(arg0: ActionEvent) {
        new Thread( new Runnable {
          def run() = new DataViewWindow("Observed Data Viewer") {
            render()
          }
        }).start()
      }
    })
    mnOperations.add(mntmViewObs)
    mnOperations.add(new JSeparator)
    val mntmRunSynth: JMenuItem = new JMenuItem("Synth")
    mntmRunSynth.setToolTipText("Observed data (1.obs) will be compared by synthesized data")
    mntmRunSynth.addActionListener {
      new ActionListener {
        def actionPerformed(arg0: ActionEvent) = {
          val main: Thread = new Thread(new ExecuteSynthConvThread)
          main.start()
          new Thread(new Runnable {
            def run() = {
              while (main.isAlive) try {
                Thread.sleep(50)
              } catch {
                case e: InterruptedException =>
                  e.printStackTrace()
              }
              new SyntChartWindow("Result of synthesis")
            }
          }).start()
        }
      }
    }
    mnOperations.add(mntmRunSynth)
    val mntmRunSynthForObs: JMenuItem = new JMenuItem("Synth & Devide")
    mntmRunSynthForObs.setToolTipText("Observed data (1.obs) will be divided by synthesized data")
    mntmRunSynthForObs.addActionListener {
      new ActionListener {
        def actionPerformed(arg0: ActionEvent) {
          val main: Thread = new Thread(new ExecuteSynthConvDivThread)
          main.start()
          new Thread(new Runnable {
            def run() = {
              while (main.isAlive) try {
                Thread.sleep(50)
              } catch {
                case e: InterruptedException =>
                  e.printStackTrace()
              }
              new SyntHelperChartWindow("Result of synthesis and dividing")
            }
          }).start()
        }
      }
    }
    mnOperations.add(mntmRunSynthForObs)
    mnOperations.add(new JSeparator)
    val mntmDIB = new JMenuItem("DIB")
    mntmDIB.setToolTipText("DIB analysis wizard (using WIDTH9)")
    mntmDIB.addActionListener {
      new ActionListener {
        def actionPerformed(arg0: ActionEvent) {
          new Thread(new Runnable {
            def run() = {
              val path: String = SpecHelperFrame.openDialogFileChooser(SyntHelperState.workspacePath, dirOnly = false, "Choose spectrum contains DIB", "Choose", "")
              new DIBWindow("DIB Analysis", path)
            }
          }).start()
        }
      }
    }
    mnOperations.add(mntmDIB)
    val mnHelp: JMenu = new JMenu("Help")
    menuBar.add(mnHelp)
    val mntmLogWindow: JMenuItem = new JMenuItem("Log Window")
    mntmLogWindow.addActionListener {
      new ActionListener {
        def actionPerformed(arg0: ActionEvent)  = LogPublisher.getInstance.openWindow()
      }
    }
    mnHelp.add(mntmLogWindow)
    mnHelp.add(new JSeparator)
    val mntmAbout: JMenuItem = new JMenuItem("About SYNTHelper")
    mnHelp.add(mntmAbout)
    val mntmHelp: JMenuItem = new JMenuItem("Help")
    mnHelp.add(mntmHelp)
  }

  def exit() = {
    new Thread(new Runnable {
      def run() = {
        val main: Thread = new Thread(new Runnable() {
          def run() = {
            SyntHelperState.saveSettings()
            SyntHelperState.copySettingsFilesToWorkspace()
            SyntHelperState.clear()
          }
        })
        main.start()
        while (main.isAlive) try {
          Thread.sleep(50)
        } catch {
          case e: InterruptedException =>
            e.printStackTrace()
        }
        System.exit(0)
      }
    }).start()
  }

  private def render() = {
    FileUtils.copyFile(new File(Constants.DEF_SOLAR_ABN), new File(Constants.ABN_PATH))
    FileUtils.copyFile(new File(Constants.DEF_CONV_CONF), new File(Constants.CONV_PATH))
    FileUtils.copyFile(new File(Constants.DEF_SYNTH_INP), new File(Constants.SYNTH_PATH))
    SyntHelperState.clear()
    initSettings()
    menu()
    controls()
    Messager.publish("Welcome to  SYNTHelper! Logging started...")
  }

  render()

}

object SpecHelperFrame {
  val statusBar: StatusBar = new StatusBar("Workspace is not selected...")
  def openDialogFileChooser(path: String, dirOnly: Boolean, name: String, button: String, filterType: String): String = {
    val fileOpen: JFileChooser = new JFileChooser
    if (!path.isEmpty)
      fileOpen.setCurrentDirectory(new File(path))
    else
      fileOpen.setCurrentDirectory(new File("."))
    if (dirOnly)
      fileOpen.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY)
    else {
      fileOpen.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES)
      filterType match {
        case "abn" =>
          fileOpen.setFileFilter(new FileNameExtensionFilter("*.abn - Abundances file", "abn"))
        case "model" =>
          fileOpen.setFileFilter(new FileNameExtensionFilter("*.atl - Kurucz's format model file", "atl"))
        case "lns" =>
          fileOpen.setFileFilter(new FileNameExtensionFilter("*.lns - Lines file", "lns"))
        case _ =>
      }
    }
    fileOpen.setDialogTitle(name)
    fileOpen.setApproveButtonText(button)
    val returnVal: Int = fileOpen.showOpenDialog(fileOpen)
    if (returnVal == JFileChooser.APPROVE_OPTION) {
      val selectedPath: File = fileOpen.getSelectedFile
      if (selectedPath != null)
        selectedPath.getAbsolutePath
      else
        ""
    } else
      ""
  }
}
