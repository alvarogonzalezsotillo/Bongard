package bongard.platform.awt;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Timer;

import bongard.animation.BAnimator;



public class AWTAnimator extends BAnimator{
	private int _millis;
	private Timer _timer;
	private long _lastMillis;
	private long _step;

	public AWTAnimator(){
		this(1);
	}
	
	public AWTAnimator(int millis){
		_millis = millis;
		_timer = createTimer();
		_timer.start();
		_lastMillis = currentMillis();
	}

	private Timer createTimer() {
		return new Timer(_millis, new ActionListener() {
			

			@Override
			public void actionPerformed(ActionEvent e) {
				long c = currentMillis();
				System.out.println(c);
				_step = c - _lastMillis;
				_lastMillis = c;
				
				long m = Math.min(10*_millis, _step);
				_step = m;
				
				boolean update = needsUpdate();
				stepAnimations(_step);
				
				if( update ){
					refresh();
				}
			}
		});
	}
	

	public long lastStep(){
		return _step;
	}
}
