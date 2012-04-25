package ollitos.platform;

import ollitos.animation.BAnimator;
import ollitos.geom.IBRectangle;
import ollitos.gui.container.IBDrawableContainer;

public interface IBGame{
	void setDefaultDrawable(IBDrawableContainer d);
	void restore(BState state);
	BState state();
	IBScreen screen();
	BAnimator animator();
	IBRectangle defaultScreenSize();
	IBDrawableContainer defaultDrawable();
}
