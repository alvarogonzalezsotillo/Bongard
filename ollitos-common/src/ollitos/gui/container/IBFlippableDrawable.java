package ollitos.gui.container;

import ollitos.gui.basic.IBDrawable;
import ollitos.gui.basic.IBRectangularDrawable;
import ollitos.platform.IBDisposable;

public interface IBFlippableDrawable extends IBDrawable.DrawableHolder, IBDisposable{
	BFlippableContainer flippableContainer();
	void setFlippableContainer( BFlippableContainer c );
	IBRectangularDrawable icon();
}
