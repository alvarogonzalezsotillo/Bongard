package purethought.gui;


public interface IBCanvas{
	IBTransform transform();
	void setTransform(IBTransform t);
	void refresh();
	IBRectangle size();
	void setDrawable( IBCanvasDrawable d );
	IBCanvasDrawable drawable();
}