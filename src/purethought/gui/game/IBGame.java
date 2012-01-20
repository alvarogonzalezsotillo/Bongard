package purethought.gui.game;

import purethought.animation.BAnimator;
import purethought.gui.basic.IBCanvas;

public interface IBGame extends Runnable{
	abstract IBCanvas canvas();
	abstract BAnimator animator();
}
