package ollitos.gui.basic;

import ollitos.geom.IBRectangle;
import ollitos.geom.IBTransform;
import ollitos.gui.event.IBEventSource;
import bongard.gui.container.IBDrawableContainer;


public interface IBCanvas extends IBEventSource{
	void setTransform(IBTransform t);
	void refresh();
	IBRectangle originalSize();
	void setDrawable( IBDrawableContainer d );
	IBDrawableContainer drawable();
}