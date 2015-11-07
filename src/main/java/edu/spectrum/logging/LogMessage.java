package edu.spectrum.logging;

import java.util.logging.Level;
import java.util.logging.LogRecord;

public class LogMessage extends LogRecord {

	private static final long serialVersionUID = -2082466023939196435L;

	public LogMessage(Level level, String msg) {
		super(level, msg);
	}

	public LogMessage(Level level, String name, String msg) {
		super(level, msg);
		super.setLoggerName(name);
	}
}
