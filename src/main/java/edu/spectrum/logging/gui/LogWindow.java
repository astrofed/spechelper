package edu.spectrum.logging.gui;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class LogWindow extends JFrame {
	private static final long serialVersionUID = 8353158704607050985L;

	private JTextArea textArea = null;

	private JScrollPane pane = null;

	public LogWindow(String title, int width, int height) {
		super(title);
		setSize(width, height);
		textArea = new JTextArea();
		pane = new JScrollPane(textArea);
		getContentPane().add(pane);
		setVisible(false);
	}

	/**
	 * This method appends the data to the text area.
	 * 
	 * @param data
	 *            the Logging information data
	 */
	public void showInfo(String data) {
		textArea.append(data);
		this.getContentPane().validate();
	}
}
