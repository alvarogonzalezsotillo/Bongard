package bongard.platform.awt;

import bongard.platform.IBLogger;

public class AWTLogger implements IBLogger{

	@Override
	public void log(Object msg) {
		log( null, msg );
	}

	@Override
	public void log(Object sender, Object msg) {
		String s = sender != null ? sender.getClass().getName() : "-"; 
		System.out.println(s + ": " + msg);
	}

}
