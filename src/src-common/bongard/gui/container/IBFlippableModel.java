package bongard.gui.container;

import bongard.platform.BResourceLocator;

public interface IBFlippableModel{
	int width();
	IBFlippableDrawable drawable(int x);
	BResourceLocator background();
}
