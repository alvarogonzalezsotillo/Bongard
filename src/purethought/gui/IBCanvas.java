package purethought.gui;

import purethought.geom.IBRectangle;
import purethought.geom.IBTransform;


public interface IBCanvas{
	IBTransform transform();
	void setTransform(IBTransform t);
	void refresh();
	IBRectangle originalSize();
	void setDrawable( IBTopDrawable d );
	IBTopDrawable drawable();
	void addListener(IBCanvasListener l);
	void removeListener(IBCanvasListener l);
}