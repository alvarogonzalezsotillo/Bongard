package purethought.gui;

import purethought.geom.IBRectangle;
import purethought.geom.IBTransform;
import purethought.gui.event.IBEventSource;


public interface IBCanvas extends IBEventSource{
	void setTransform(IBTransform t);
	void refresh();
	IBRectangle originalSize();
	void setDrawable( IBDrawableContainer d );
	IBDrawableContainer drawable();
}