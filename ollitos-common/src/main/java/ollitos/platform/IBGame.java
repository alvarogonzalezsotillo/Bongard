package ollitos.platform;


import ollitos.animation.BAnimator;
import ollitos.geom.IBRectangle;
import ollitos.gui.basic.IBDrawable;
import ollitos.gui.container.IBDrawableContainer;

public interface IBGame{
	void setDefaultDrawable(IBDrawable d);
	IBScreen screen();
	BAnimator animator();
	IBRectangle defaultScreenSize();
	IBDrawable defaultDrawable();
	void restore();
	void saveState();
}
