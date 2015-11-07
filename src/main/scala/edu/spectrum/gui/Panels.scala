package edu.spectrum.gui

import java.awt.GridLayout
import java.awt.event.{ActionEvent, ActionListener}
import java.beans.{PropertyChangeEvent, PropertyChangeListener}
import java.lang.Short.MAX_VALUE
import java.text.{DecimalFormat, DecimalFormatSymbols}
import java.util
import javax.swing.GroupLayout.Alignment
import javax.swing.LayoutStyle.ComponentPlacement
import javax.swing.SwingConstants._
import javax.swing._
import javax.swing.text.{DefaultFormatterFactory, NumberFormatter}

import edu.spectrum.settings.AbundanceSettings
import edu.spectrum.state.SyntHelperState

/**
 * Created by cf on 31.05.2015.
 */
@SerialVersionUID(5533990238322706064L)
class AbundancesPanel extends JPanel {
  /**
   * Create the panel.
   */
  private val lblAbundancesdefault = new JLabel("Abundances (Default - solar chemical composition)")
  private val scrollPane = new JScrollPane
  val groupLayout: GroupLayout = new GroupLayout(this)
  groupLayout.setHorizontalGroup(groupLayout.createParallelGroup(Alignment.LEADING).addGroup(groupLayout.createSequentialGroup.addContainerGap().addGroup(groupLayout.createParallelGroup(Alignment.LEADING).addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 430, MAX_VALUE).addComponent(lblAbundancesdefault)).addContainerGap()))
  groupLayout.setVerticalGroup(groupLayout.createParallelGroup(Alignment.LEADING).addGroup(groupLayout.createSequentialGroup.addContainerGap().addComponent(lblAbundancesdefault).addPreferredGap(ComponentPlacement.UNRELATED).addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 253, MAX_VALUE).addContainerGap()))
  setLayout(groupLayout)
  private val chemicalCompositionPanel = new ChemicalCompositionPanel
  scrollPane.setViewportView(chemicalCompositionPanel)

  def setValues() = chemicalCompositionPanel.setValues()

}

@SerialVersionUID(2370083210892233507L)
class GeneralSynthPanel extends JPanel {
  /**
   * Create the panel.
   */
  private val settings = SyntHelperState.convolveSettings
  private val jLabel4 = new JLabel("Synth settings")
  private val jLabel5 = new JLabel("Convolution")
  private val synthSetScrollPane = new JScrollPane
  private val synthSetPanel = new SynthSettingsPanel
  synthSetScrollPane.setViewportView(synthSetPanel)
  private val convolvePanel = new JPanel
  convolvePanel.setBorder(UIManager.getBorder("ScrollPane.border"))
  private val jLabel9 = new JLabel("Vsin(i)")
  private val jLabel11 = new JLabel("Macroturbulence")
  private val jLabel12 = new JLabel("Resolution")
  private val otherSymbols: DecimalFormatSymbols = new DecimalFormatSymbols
  otherSymbols.setDecimalSeparator('.')
  private val ftxtVsini = new JFormattedTextField
  ftxtVsini.setHorizontalAlignment(RIGHT)
  ftxtVsini.addPropertyChangeListener(new PropertyChangeListener {
    def propertyChange(arg0: PropertyChangeEvent) {
      if (arg0.getNewValue.isInstanceOf[Number]) {
        val s: String = arg0.getNewValue.toString
        settings.vsini = s.toDouble
      }
    }
  })
  private val ftxtVmacro = new JFormattedTextField
  ftxtVmacro.setHorizontalAlignment(RIGHT)
  ftxtVmacro.addPropertyChangeListener(new PropertyChangeListener {
    def propertyChange(arg0: PropertyChangeEvent) {
      if (arg0.getNewValue.isInstanceOf[Number]) {
        val s: String = arg0.getNewValue.toString
        settings.vmacro = s.toDouble
      }
    }
  })
  private val ftxtResolution = new JFormattedTextField
  ftxtResolution.setHorizontalAlignment(RIGHT)
  ftxtResolution.addPropertyChangeListener(new PropertyChangeListener {
    def propertyChange(arg0: PropertyChangeEvent) {
      if (arg0.getNewValue.isInstanceOf[Number]) {
        val s: String = arg0.getNewValue.toString
        settings.resolution = s.toInt
      }
    }
  })
  private val convolvePanelLayout: GroupLayout = new GroupLayout(convolvePanel)
  convolvePanelLayout.setHorizontalGroup(convolvePanelLayout.createParallelGroup(Alignment.LEADING).addGroup(convolvePanelLayout.createSequentialGroup.addContainerGap().addComponent(jLabel9).addPreferredGap(ComponentPlacement.UNRELATED).addComponent(ftxtVsini, GroupLayout.PREFERRED_SIZE, 50, GroupLayout.PREFERRED_SIZE).addGap(18).addComponent(jLabel11).addPreferredGap(ComponentPlacement.UNRELATED).addComponent(ftxtVmacro, GroupLayout.PREFERRED_SIZE, 64, GroupLayout.PREFERRED_SIZE).addGap(18).addComponent(jLabel12).addPreferredGap(ComponentPlacement.UNRELATED).addComponent(ftxtResolution, GroupLayout.PREFERRED_SIZE, 100, GroupLayout.PREFERRED_SIZE).addContainerGap(157, MAX_VALUE)))
  convolvePanelLayout.setVerticalGroup(convolvePanelLayout.createParallelGroup(Alignment.LEADING).addGroup(convolvePanelLayout.createSequentialGroup.addContainerGap().addGroup(convolvePanelLayout.createParallelGroup(Alignment.BASELINE).addComponent(jLabel9).addComponent(ftxtVsini, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE).addComponent(jLabel11).addComponent(ftxtVmacro, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE).addComponent(jLabel12).addComponent(ftxtResolution, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)).addContainerGap(GroupLayout.DEFAULT_SIZE, MAX_VALUE)))
  convolvePanel.setLayout(convolvePanelLayout)
  ftxtVsini.setFormatterFactory(new DefaultFormatterFactory(new NumberFormatter(new DecimalFormat("#0.00", otherSymbols))))
  ftxtVmacro.setFormatterFactory(new DefaultFormatterFactory(new NumberFormatter(new DecimalFormat("#0.00", otherSymbols))))
  ftxtResolution.setFormatterFactory(new DefaultFormatterFactory(new NumberFormatter(new DecimalFormat("#0"))))
  private val synthPanelLayout: GroupLayout = new GroupLayout(this)
  synthPanelLayout.setHorizontalGroup(synthPanelLayout.createParallelGroup(Alignment.TRAILING).addGroup(synthPanelLayout.createSequentialGroup.addContainerGap().addGroup(synthPanelLayout.createParallelGroup(Alignment.LEADING).addGroup(synthPanelLayout.createSequentialGroup.addGroup(synthPanelLayout.createParallelGroup(Alignment.LEADING).addComponent(synthSetScrollPane, GroupLayout.DEFAULT_SIZE, 477, MAX_VALUE).addComponent(jLabel4)).addGap(12)).addGroup(synthPanelLayout.createSequentialGroup.addComponent(jLabel5, GroupLayout.PREFERRED_SIZE, 67, GroupLayout.PREFERRED_SIZE).addContainerGap(422, MAX_VALUE)).addGroup(synthPanelLayout.createSequentialGroup.addComponent(convolvePanel, 0, 0, MAX_VALUE).addGap(12)))))
  synthPanelLayout.setVerticalGroup(synthPanelLayout.createParallelGroup(Alignment.LEADING).addGroup(synthPanelLayout.createSequentialGroup.addContainerGap().addComponent(jLabel4).addPreferredGap(ComponentPlacement.UNRELATED).addComponent(synthSetScrollPane, GroupLayout.PREFERRED_SIZE, 202, GroupLayout.PREFERRED_SIZE).addPreferredGap(ComponentPlacement.UNRELATED).addComponent(jLabel5).addGap(9).addComponent(convolvePanel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE).addContainerGap(GroupLayout.DEFAULT_SIZE, MAX_VALUE)))
  this.setLayout(synthPanelLayout)
  setValues()

  def setValues() = {
    synthSetPanel.setValues()
    ftxtVsini.setValue(settings.vsini)
    ftxtVmacro.setValue(settings.vmacro)
    ftxtResolution.setValue(settings.resolution)
  }
}

@SerialVersionUID(5860582472418450134L)
class SynthSettingsPanel extends JPanel {
  private val settings = SyntHelperState.synthSettings
  /**
   * Create the panel.
   */
  private val jLabel6 = new JLabel("Lines")
  private val jLabel8 = new JLabel("Microturbulence")
  private val jLabel10 = new JLabel("Synth interval")
  private val jScrollPane1 = new JScrollPane
  private val linesTextArea = new JTextArea
  private val btnAddLinesFile = new JButton("Add")
  private val ftxtVt = new JFormattedTextField
  ftxtVt.setHorizontalAlignment(RIGHT)
  ftxtVt.addPropertyChangeListener(new PropertyChangeListener {
    def propertyChange(arg0: PropertyChangeEvent) =
      if (arg0.getNewValue.isInstanceOf[Number])
        settings.vt = arg0.getNewValue.toString.toDouble
  })
  private val ftxtStart = new JFormattedTextField
  ftxtStart.setHorizontalAlignment(RIGHT)
  ftxtStart.addPropertyChangeListener(new PropertyChangeListener {
    def propertyChange(arg0: PropertyChangeEvent) =
      if (arg0.getNewValue.isInstanceOf[Number])
        settings.startSynth = arg0.getNewValue.toString.toInt
  })
  private val ftxtStep = new JFormattedTextField
  ftxtStep.setHorizontalAlignment(RIGHT)
  ftxtStep.addPropertyChangeListener(new PropertyChangeListener {
    def propertyChange(arg0: PropertyChangeEvent) =
      if (arg0.getNewValue.isInstanceOf[Number])
        settings.step = arg0.getNewValue.toString.toDouble
  })
  private val ftxtEnd = new JFormattedTextField
  ftxtEnd.setHorizontalAlignment(RIGHT)
  ftxtEnd.addPropertyChangeListener(new PropertyChangeListener {
    def propertyChange(arg0: PropertyChangeEvent) =
      if (arg0.getNewValue.isInstanceOf[Number])
        settings.endSynth = arg0.getNewValue.toString.toInt
  })
  private val jCheckBox1 = new JCheckBox("Identificate Lines")
  jCheckBox1.addActionListener(new ActionListener {
    def actionPerformed(arg0: ActionEvent) = {
      val abstractButton: AbstractButton = arg0.getSource.asInstanceOf[AbstractButton]
      val selected: Boolean = abstractButton.getModel.isSelected
      settings.printIdent = selected
    }
  })
  private val otherSymbols: DecimalFormatSymbols = new DecimalFormatSymbols
  otherSymbols.setDecimalSeparator('.')
  ftxtVt.setFormatterFactory(new DefaultFormatterFactory(new NumberFormatter(new DecimalFormat("#0.00", otherSymbols))))
  ftxtStart.setFormatterFactory(new DefaultFormatterFactory(new NumberFormatter(new DecimalFormat("#0"))))
  ftxtStep.setFormatterFactory(new DefaultFormatterFactory(new NumberFormatter(new DecimalFormat("#0.00", otherSymbols))))
  ftxtEnd.setFormatterFactory(new DefaultFormatterFactory(new NumberFormatter(new DecimalFormat("#0"))))
  linesTextArea.setColumns(20)
  linesTextArea.setRows(5)
  linesTextArea.setEditable(false)
  jScrollPane1.setViewportView(linesTextArea)
  btnAddLinesFile.addActionListener(new ActionListener {
    def actionPerformed(evt: ActionEvent) = addLinesFile()
  })
  val btnReset: JButton = new JButton("Reset")
  btnReset.addActionListener(new ActionListener {
    def actionPerformed(evt: ActionEvent) = {
      SyntHelperState.resetLinePathList()
      SyntHelperState.synthSettings.save()
      linesTextArea.setText(settings.binaryFilesPaths.mkString(";\n"))
    }
  })
  val synthSetPanelLayout: GroupLayout = new GroupLayout(this)
  synthSetPanelLayout.setHorizontalGroup(synthSetPanelLayout.createParallelGroup(Alignment.LEADING).addGroup(synthSetPanelLayout.createSequentialGroup.addContainerGap().addGroup(synthSetPanelLayout.createParallelGroup(Alignment.LEADING).addGroup(synthSetPanelLayout.createSequentialGroup.addGroup(synthSetPanelLayout.createParallelGroup(Alignment.TRAILING).addGroup(synthSetPanelLayout.createSequentialGroup.addComponent(jLabel6).addGap(18).addComponent(jScrollPane1, GroupLayout.PREFERRED_SIZE, 0, MAX_VALUE)).addGroup(synthSetPanelLayout.createSequentialGroup.addComponent(btnReset).addPreferredGap(ComponentPlacement.RELATED).addComponent(btnAddLinesFile))).addContainerGap()).addGroup(synthSetPanelLayout.createSequentialGroup.addGroup(synthSetPanelLayout.createParallelGroup(Alignment.LEADING).addComponent(jCheckBox1).addGroup(synthSetPanelLayout.createSequentialGroup.addComponent(jLabel8).addPreferredGap(ComponentPlacement.UNRELATED).addComponent(ftxtVt, GroupLayout.DEFAULT_SIZE, 34, MAX_VALUE).addGap(18).addComponent(jLabel10).addGap(18).addComponent(ftxtStart, GroupLayout.DEFAULT_SIZE, 64, MAX_VALUE).addPreferredGap(ComponentPlacement.RELATED).addComponent(ftxtStep, GroupLayout.DEFAULT_SIZE, 34, MAX_VALUE).addPreferredGap(ComponentPlacement.RELATED).addComponent(ftxtEnd, GroupLayout.DEFAULT_SIZE, 64, MAX_VALUE))).addGap(43)))))
  synthSetPanelLayout.setVerticalGroup(synthSetPanelLayout.createParallelGroup(Alignment.LEADING).addGroup(synthSetPanelLayout.createSequentialGroup.addContainerGap().addGroup(synthSetPanelLayout.createParallelGroup(Alignment.LEADING).addComponent(jLabel6).addComponent(jScrollPane1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)).addPreferredGap(ComponentPlacement.RELATED).addGroup(synthSetPanelLayout.createParallelGroup(Alignment.BASELINE).addComponent(btnAddLinesFile).addComponent(btnReset)).addGap(18).addGroup(synthSetPanelLayout.createParallelGroup(Alignment.BASELINE).addComponent(jLabel8).addComponent(ftxtVt, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE).addComponent(jLabel10).addComponent(ftxtStart, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE).addComponent(ftxtStep, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE).addComponent(ftxtEnd, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)).addGap(18).addComponent(jCheckBox1).addGap(115)))
  setLayout(synthSetPanelLayout)

  def setValues() = {
    ftxtVt.setValue(settings.vt)
    ftxtStart.setValue(settings.startSynth)
    ftxtStep.setValue(settings.step)
    ftxtEnd.setValue(settings.endSynth)
    linesTextArea.setText(settings.binaryFilesPaths.mkString(";\n"))
    jCheckBox1.setSelected(settings.printIdent)
  }

  private def addLinesFile() = {
    val tmpF: String = SpecHelperFrame.openDialogFileChooser(".", dirOnly = false, "Choose Binary File With Lines", "Add", "lns")
    if (!tmpF.isEmpty) {
      settings.binaryFilesPaths += tmpF
      linesTextArea.setText(settings.binaryFilesPaths.mkString("; "))
    }
  }
}

@SerialVersionUID(-2415943730144176767L)
class ChemicalCompositionPanel extends JPanel {
  private val abundSettings: AbundanceSettings = SyntHelperState.abundanceSettings
  private val abunds: util.Map[String, JFormattedTextField] = new util.LinkedHashMap[String, JFormattedTextField]

  /**
   * Create the panel.
   */
  setLayout(new GridLayout(10, 11, 20, 5))

  {
    val otherSymbols: DecimalFormatSymbols = new DecimalFormatSymbols
    otherSymbols.setDecimalSeparator('.')

    abundSettings.elements.map {
      a =>
        val formattedTextField: JFormattedTextField = new JFormattedTextField
        val title = a.element.symbol
        formattedTextField.setName(title)
        title match {
          case "H" | "He" => formattedTextField.setFormatterFactory(new DefaultFormatterFactory(new NumberFormatter(new DecimalFormat("#0.0000", otherSymbols))))
          case _ => formattedTextField.setFormatterFactory(new DefaultFormatterFactory(new NumberFormatter(new DecimalFormat("#0.00", otherSymbols))))
        }
        formattedTextField.setValue(a.value)
        formattedTextField.setHorizontalAlignment(SwingConstants.RIGHT)
        formattedTextField.addPropertyChangeListener(new PropertyChangeListener {
          def propertyChange(arg0: PropertyChangeEvent) {
            if (arg0.getNewValue.isInstanceOf[Number]) {
              val src: JFormattedTextField = arg0.getSource.asInstanceOf[JFormattedTextField]
              val value = arg0.getNewValue.toString.toDouble
              abundSettings.elements.find(_.element.symbol == src.getName).foreach(a => a.value = value)
            }
          }
        })
        (title, formattedTextField)
    }
  } foreach {
    case (t, c) =>
      val tPanel: JPanel = new JPanel
      tPanel.setLayout(new GridLayout(2, 1))
      val label: JLabel = new JLabel(c.getName)
      label.setHorizontalAlignment(SwingConstants.CENTER)
      tPanel.add(label)
      tPanel.add(c)
      add(tPanel)

      abunds.put(t, c)
  }

  def setValues() = abundSettings.elements.foreach {
    a =>
      abunds.get(a.element.symbol).setValue(a.value)
  }

}