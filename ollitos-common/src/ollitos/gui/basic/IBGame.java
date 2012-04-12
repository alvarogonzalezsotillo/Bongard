package ollitos.gui.basic;

import ollitos.animation.BAnimator;
import ollitos.geom.IBRectangle;
import ollitos.gui.container.IBDrawableContainer;

public interface IBGame{
	void setDefaultDrawable(IBDrawableContainer d);
	void restore(BState state);
	BState state();
	IBCanvas canvas();
	BAnimator animator();
	IBRectangle defaultScreenSize();
}
