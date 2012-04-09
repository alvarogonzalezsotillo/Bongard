package bongard.gui.game;

import ollitos.animation.BAnimator;
import ollitos.gui.basic.IBCanvas;

public interface IBGame{
	void restore(BState state);
	BState state();
	IBCanvas canvas();
	BAnimator animator();
	
}
