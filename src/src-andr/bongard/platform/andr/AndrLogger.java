package bongard.platform.andr;

import android.util.Log;
import bongard.platform.IBLogger;

public class AndrLogger implements IBLogger{

	@Override
	public void log(Object msg) {
		log( null, msg );
	}
	
	public void log(Object sender, Object msg) {
		String s = sender != null ? sender.getClass().getName() : "-";
		s = s.substring( s.lastIndexOf(".")+1 );
		Log.d(s, String.valueOf(msg));
	}
}
