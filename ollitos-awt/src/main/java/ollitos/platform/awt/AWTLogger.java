package ollitos.platform.awt;

import ollitos.gui.container.BSlidableContainer;
import ollitos.platform.IBLogger;

public class AWTLogger implements IBLogger{

	private long _lastMillis;
	
	@Override
	public void log(Object msg) {
		log( null, msg );
	}

	@Override
	public void log(Object sender, Object msg) {
		if( BSlidableContainer.LOG_EVENTS ){
			return;
		}
		String s = sender != null ? sender.getClass().getName() : "-"; 
		s = s.substring( s.lastIndexOf(".")+1, s.length() );
		long millis = System.currentTimeMillis();
		long m = millis - _lastMillis;
		_lastMillis = millis;
		System.out.println(m + " -- " + s + ": " + msg);
	}

}
