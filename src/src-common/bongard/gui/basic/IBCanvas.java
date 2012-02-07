package bongard.gui.basic;

import bongard.geom.IBRectangle;
import bongard.geom.IBTransform;
import bongard.gui.container.IBDrawableContainer;
import bongard.gui.event.IBEventSource;


public interface IBCanvas extends IBEventSource{
	void setTransform(IBTransform t);
	void refresh();
	IBRectangle originalSize();
	void setDrawable( IBDrawableContainer d );
	IBDrawableContainer drawable();
}