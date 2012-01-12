package purethought.gui;

import purethought.geom.IBRectangle;
import purethought.geom.IBTransform;


public interface IBCanvas{
	IBTransform transform();
	void setTransform(IBTransform t);
	void refresh();
	IBRectangle size();
	void setDrawable( IBCanvasDrawable d );
	IBCanvasDrawable drawable();
	void addListener(IBCanvasListener l);
	void removeListener(IBCanvasListener l);
}