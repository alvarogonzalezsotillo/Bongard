package bongard.gui.game;

import bongard.animation.BAnimator;
import bongard.gui.basic.IBCanvas;

public interface IBGame extends Runnable{
	abstract IBCanvas canvas();
	abstract BAnimator animator();
}