package edu.spectrum.gui

import java.awt.{BorderLayout, Color, Component, Dimension, Graphics, SystemColor}
import javax.swing.{Icon, JLabel, JPanel, JSeparator, JTextField, SwingConstants}

@SerialVersionUID(-4460585986151628416L)
class StatusBar(val defMainMessage: String) extends JPanel {

  private val rightPanel: JPanel = new JPanel(new BorderLayout)
  private val centerPanel: JPanel = new JPanel(new BorderLayout)
  private val separator = new JSeparator
  private val lblWorkspace: JLabel = new JLabel(defMainMessage)
  private val textField: JTextField = new JTextField

  setLayout(new BorderLayout)
  setPreferredSize(new Dimension(640, 23))
  rightPanel.add(new JLabel(new AngledLinesWindowsCornerIcon), BorderLayout.SOUTH)
  rightPanel.setOpaque(false)
  add(rightPanel, BorderLayout.EAST)
  setBackground(SystemColor.control)
  separator.setOrientation(SwingConstants.VERTICAL)
  centerPanel.add(separator, BorderLayout.WEST)
  add(centerPanel)
  add(lblWorkspace, BorderLayout.WEST)
  textField.setEditable(false)
  centerPanel.add(textField, BorderLayout.CENTER)

  protected override def paintComponent(g: Graphics) {
    super.paintComponent(g)
    def line(c: Color, i: Int) = {
      g.setColor(c)
      g.drawLine(0, i, getWidth, i)
    }
    var y: Int = 0
    line(new Color(156, 154, 140), y)
    y += 1
    line(new Color(196, 194, 183), y)
    y += 1
    line(new Color(218, 215, 201), y)
    y += 1
    line(new Color(233, 231, 217), y)
    y = getHeight - 3
    line(new Color(233, 232, 218), y)
    y += 1
    line(new Color(233, 231, 216), y)
    y = getHeight - 1
    line(new Color(221, 221, 220), y)
  }

  def setMainMessage(s: String) = {
    if(s == null || s.isEmpty)
      lblWorkspace.setText(s" $defMainMessage   ")
    else
      lblWorkspace.setText(s" $s   ")
  }

  def setInfo(str: String) = textField.setText(s" $str   ")

}

object AngledLinesWindowsCornerIcon {
  private val WHITE_LINE_COLOR: Color = new Color(255, 255, 255)
  private val GRAY_LINE_COLOR: Color = new Color(172, 168, 153)
  private val WIDTH: Int = 13
  private val HEIGHT: Int = 13
}

class AngledLinesWindowsCornerIcon extends Icon {
  def getIconHeight: Int = AngledLinesWindowsCornerIcon.WIDTH

  def getIconWidth: Int = AngledLinesWindowsCornerIcon.HEIGHT

  def paintIcon(c: Component, g: Graphics, x: Int, y: Int) {
    g.setColor(AngledLinesWindowsCornerIcon.WHITE_LINE_COLOR)
    g.drawLine(0, 12, 12, 0)
    g.drawLine(5, 12, 12, 5)
    g.drawLine(10, 12, 12, 10)
    g.setColor(AngledLinesWindowsCornerIcon.GRAY_LINE_COLOR)
    g.drawLine(1, 12, 12, 1)
    g.drawLine(2, 12, 12, 2)
    g.drawLine(3, 12, 12, 3)
    g.drawLine(6, 12, 12, 6)
    g.drawLine(7, 12, 12, 7)
    g.drawLine(8, 12, 12, 8)
    g.drawLine(11, 12, 12, 11)
    g.drawLine(12, 12, 12, 12)
  }
}