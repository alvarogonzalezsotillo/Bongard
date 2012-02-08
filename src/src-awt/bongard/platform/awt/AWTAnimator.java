package bongard.platform.awt;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Timer;

import bongard.animation.BAnimator;



public class AWTAnimator extends BAnimator{
	private int _millis;
	private Timer _timer;
	private int _lastMillis;
	private int _step;

	public AWTAnimator(){
		this(1000/100);
	}
	
	public AWTAnimator(int millis){
		_millis = millis;
		_timer = createTimer();
		_timer.start();
		_lastMillis = (int) System.currentTimeMillis();
	}

	private Timer createTimer() {
		return new Timer(_millis, new ActionListener() {
			

			@Override
			public void actionPerformed(ActionEvent e) {
				int c = (int) System.currentTimeMillis();
				_step = c - _lastMillis;
				_lastMillis = c;
				
				int m = Math.min(10*_millis, _step);
				
				boolean update = needsUpdate();
				stepAnimations(m);
				
				if( update ){
					refresh();
				}
			}
		});
	}
	

	public int lastStep(){
		return _step;
	}
}
