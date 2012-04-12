package ollitos.gui.container;

import ollitos.animation.IBTransformAnimable;
import ollitos.gui.basic.IBDisposable;
import ollitos.gui.basic.IBRectangularDrawable;

public interface IBFlippableDrawable extends IBDrawableContainer, IBTransformAnimable, IBDisposable{
	BFlippableContainer flippableContainer();
	void setFlippableContainer( BFlippableContainer c );
	IBRectangularDrawable icon();
}
