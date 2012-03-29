package bongard.gui.container;

import java.io.Serializable;

import bongard.platform.BResourceLocator;

public interface IBFlippableModel extends Serializable{
	int width();
	IBFlippableDrawable drawable(int x);
	void dispose(int x);
	BResourceLocator background();
}
