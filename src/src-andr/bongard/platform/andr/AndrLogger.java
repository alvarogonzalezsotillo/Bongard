package bongard.platform.andr;

import android.util.Log;
import bongard.platform.IBLogger;

public class AndrLogger implements IBLogger{

	@Override
	public void log(Object msg) {
		Log.d("-", String.valueOf(msg));
	}

}
