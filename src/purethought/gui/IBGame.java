package purethought.gui;

import purethought.animation.BAnimator;

public interface IBGame extends Runnable{
	abstract IBCanvas canvas();
	abstract BAnimator animator();
}
