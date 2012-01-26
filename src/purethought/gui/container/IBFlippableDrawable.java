package purethought.gui.container;

import purethought.animation.IBTransformAnimable;
import purethought.gui.basic.IBRectangularDrawable;

public interface IBFlippableDrawable extends IBDrawableContainer, IBTransformAnimable{
	BFlippableContainer flippableContainer();
	void setFlippableContainer( BFlippableContainer c );
	IBRectangularDrawable icon();
}
