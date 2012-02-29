package bongard.platform.awt;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.SwingUtilities;
import javax.swing.Timer;

import bongard.animation.BAnimator;



public class AWTAnimator extends BAnimator{
	private Timer _timer;

	public AWTAnimator(){
		_timer = createTimer();
		_timer.start();
	}

	private Timer createTimer() {
		return new Timer( millis(), new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				tick();
			}
		});
	}

	@Override
	public void post(Runnable r) {
		SwingUtilities.invokeLater(r);
	}

}
