package ollitos.gui.container;

import java.io.Serializable;

import ollitos.gui.basic.IBDrawable;
import ollitos.platform.BResourceLocator;


public interface IBSlidableModel extends Serializable{
	int width();
	IBSlidablePage page(int x);
	void dispose(int x);
	IBDrawable background();
}
