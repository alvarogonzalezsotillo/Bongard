package bongard.gui.container;

import bongard.animation.IBTransformAnimable;
import bongard.gui.basic.IBRectangularDrawable;

public interface IBFlippableDrawable extends IBDrawableContainer, IBTransformAnimable, IBDisposable{
	BFlippableContainer flippableContainer();
	void setFlippableContainer( BFlippableContainer c );
	IBRectangularDrawable icon();
}
