package edu.spectrum.logging;

import java.util.logging.Level;
import java.util.logging.Logger;

public class LogPublisher {
	
	private static volatile LogPublisher logPublisher;

	private LogWindowHandler handler = null;
	private Logger logger = null;	

	private LogPublisher() {
		handler = LogWindowHandler.getInstance();
		logger = Logger.getLogger("logging.handler");
		logger.addHandler(handler);
	}
	
	public static LogPublisher getInstance() {
		if (logPublisher == null) {
			synchronized (LogPublisher.class) {
				if (logPublisher == null)
					logPublisher = new LogPublisher();
			}
		}
		return logPublisher;
	}	
	
	public void openWindow() {
		handler.getWindow().setVisible(true);
	}
	
	public void info(String message) {
		logger.info(message);
	}
	
	public void handleException(String title, Exception e) {
		handler.publish(new LogMessage(Level.WARNING, title, e.getMessage()));
		e.printStackTrace();
	}
	
	public void publishMessage(String title, String message) {
		handler.publish(new LogMessage(Level.INFO, title, message));
	}
}
