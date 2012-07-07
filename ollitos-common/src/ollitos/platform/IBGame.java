package ollitos.platform;


import ollitos.animation.BAnimator;
import ollitos.geom.IBRectangle;
import ollitos.gui.container.IBDrawableContainer;
import ollitos.platform.state.BState;

public interface IBGame{
	void setDefaultDrawable(IBDrawableContainer d);
	IBScreen screen();
	BAnimator animator();
	IBRectangle defaultScreenSize();
	IBDrawableContainer defaultDrawable();
	void restore();
	void saveState();
}
