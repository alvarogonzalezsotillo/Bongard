package purethought.gui;

import purethought.geom.IBRectangle;

public interface IBDrawableContainer extends IBDrawable, IBEventSource{
	IBEventListener listener();
	IBRectangle originalSize(); // TODO: maybe move to IBDrawable
}
