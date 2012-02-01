package purethought.gui.container;

import purethought.platform.BResourceLocator;

public interface IBFlippableModel{
	int width();
	IBFlippableDrawable drawable(int x);
	BResourceLocator background();
}
