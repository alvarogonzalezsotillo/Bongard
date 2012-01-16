package purethought.gui;

import purethought.geom.IBRectangle;
import purethought.gui.event.IBEventListener;
import purethought.gui.event.IBEventSource;

public interface IBDrawableContainer extends IBDrawable, IBEventSource{
	IBEventListener listener();
	IBRectangle originalSize(); // TODO: maybe move to IBDrawable
}
