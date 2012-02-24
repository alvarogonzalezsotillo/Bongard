package bongard.platform.awt;

import bongard.platform.IBLogger;

public class AWTLogger implements IBLogger{

	@Override
	public void log(Object msg) {
		System.out.println(msg);
	}

}
