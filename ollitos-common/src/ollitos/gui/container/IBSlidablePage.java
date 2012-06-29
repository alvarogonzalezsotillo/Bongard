package ollitos.gui.container;

import ollitos.gui.basic.IBDrawable;
import ollitos.gui.basic.IBRectangularDrawable;
import ollitos.platform.IBDisposable;

public interface IBSlidablePage extends IBDrawable.DrawableHolder, IBDisposable{
	IBRectangularDrawable icon();
}
