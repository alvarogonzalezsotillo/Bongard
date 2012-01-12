package purethought.awt;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Timer;

import purethought.animation.BAnimator;
import purethought.util.BFactory;


public class AWTAnimator extends BAnimator{
	private int _millis;
	private Timer _timer;
	private int _lastMillis;
	private int _step;

	public AWTAnimator(){
		this(1000/20);
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
				int m = Math.min(2*_millis, _step);
				
				stepAnimations(m);
				BFactory.instance().canvas().refresh();
			}
		});
	}
	
	public int lastStep(){
		return _step;
	}
}
