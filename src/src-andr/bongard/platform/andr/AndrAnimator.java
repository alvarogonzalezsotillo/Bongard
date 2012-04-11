package bongard.platform.andr;

import ollitos.animation.BAnimator;
import ollitos.animation.IBAnimation;
import android.os.Handler;

public class AndrAnimator extends BAnimator{
	private Handler _handler;
	
	private Runnable _runnable = new Runnable(){
		public void run() {
			tick();
			_handler.postDelayed(_runnable, millis() );	
		};
	};
	
	public AndrAnimator(){
		super(1);
		_handler = new Handler();
		_handler.postDelayed(_runnable, millis() );
	}

	@Override
	public void post(Runnable r) {
		_handler.post(r);
	}
	
	@Override
	public void addAnimation(final IBAnimation a) {
		post( new Runnable(){
			public void run(){
				AndrAnimator.super.addAnimation(a);
			}
		});
	}
}
