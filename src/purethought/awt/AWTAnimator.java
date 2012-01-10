package purethought.awt;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Timer;

import purethought.animation.BAnimator;
import purethought.gui.BFactory;


public class AWTAnimator extends BAnimator{
	private int _millis;
	private Timer _timer;

	public AWTAnimator(){
		this(1000/20);
	}
	
	public AWTAnimator(int millis){
		_millis = millis;
		_timer = createTimer();
		_timer.start();
	}

	private Timer createTimer() {
		return new Timer(_millis, new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				stepAnimations(_millis);
				BFactory.instance().canvas().refresh();
			}
		});
	}
}
