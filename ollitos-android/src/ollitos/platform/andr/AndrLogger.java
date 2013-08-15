package ollitos.platform.andr;

import ollitos.platform.IBLogger;
import android.util.Log;

public class AndrLogger implements IBLogger{

	@Override
	public void log(Object msg) {
		log( null, msg );
	}
	
	public void log(Object sender, Object msg) {
		String s = sender != null ? sender.getClass().getName() : "-";
		s = s.substring( s.lastIndexOf(".")+1, s.length() );
		Log.d(s, String.valueOf(msg));
	}
}
