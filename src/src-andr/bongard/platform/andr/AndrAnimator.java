package bongard.platform.andr;

import android.os.Handler;
import bongard.animation.BAnimator;

public class AndrAnimator extends BAnimator{
	private Handler _handler;
	
	private Runnable _runnable = new Runnable(){
		public void run() {
			tick();
			_handler.postDelayed(_runnable, millis() );	
		};
	};
	
	public AndrAnimator(){
		_handler = new Handler();
		_handler.postDelayed(_runnable, millis() );
	}
}
