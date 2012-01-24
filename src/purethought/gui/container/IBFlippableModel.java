package purethought.gui.container;

import purethought.platform.BImageLocator;

public interface IBFlippableModel{
	int width();
	IBFlippableDrawable drawable(int x);
	BImageLocator background();
}
