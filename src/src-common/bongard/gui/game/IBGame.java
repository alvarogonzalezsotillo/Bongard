package bongard.gui.game;

import bongard.animation.BAnimator;
import bongard.gui.basic.IBCanvas;

public interface IBGame{
	void restore(BState state);
	BState state();
	IBCanvas canvas();
	BAnimator animator();
	
}
