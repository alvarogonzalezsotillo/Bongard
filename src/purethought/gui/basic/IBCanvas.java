package purethought.gui.basic;

import purethought.geom.IBRectangle;
import purethought.geom.IBTransform;
import purethought.gui.container.IBDrawableContainer;
import purethought.gui.event.IBEventSource;


public interface IBCanvas extends IBEventSource{
	void setTransform(IBTransform t);
	void refresh();
	IBRectangle originalSize();
	void setDrawable( IBDrawableContainer d );
	IBDrawableContainer drawable();
}