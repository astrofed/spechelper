package edu.spectrum.logging;

import edu.spectrum.SpecHelper;
import edu.spectrum.gui.SpecHelperFrame;
import edu.spectrum.gui.StatusBar;

public class Messager {
	
	private static volatile Messager instance;
	
	private Messager() { }

	public static Messager getInstance() {
		if (instance == null) {
			synchronized (Messager.class) {
				if (instance == null)
					instance = new Messager();
			}
		}
		return instance;
	}
	
	public static void publish(Object... arg) {
		LogPublisher publ = LogPublisher.getInstance();
		StatusBar status = SpecHelperFrame.statusBar();
		
		if(arg.length == 1 && arg[0] instanceof String) {
			String str = (String) arg[0];
			publ.info(str);
			status.setInfo(str);
		} else if(arg.length == 2) {
			if(arg[0] instanceof String && arg[1] instanceof String) {
				publ.publishMessage((String) arg[0], (String) arg[1]);
				status.setInfo((String) arg[0] + " - " + (String) arg[1]);
			} else if(arg[0] instanceof String && arg[1] instanceof Exception) {
				publ.handleException((String) arg[0], (Exception) arg[1]);
				status.setInfo((String) arg[0] + " - ERROR! See log window for details");
			}
		}
		
	}

}
