package purethought.gui.container;

import purethought.animation.IBTransformAnimable;

public interface IBFlippableDrawable extends IBDrawableContainer, IBTransformAnimable{
	BFlippableContainer flippableContainer();
	void setFlippableContainer( BFlippableContainer c );
	void showed();
	void hided();
}
