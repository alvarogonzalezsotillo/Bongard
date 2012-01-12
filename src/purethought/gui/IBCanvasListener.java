package purethought.gui;

import purethought.geom.IBPoint;

public interface IBCanvasListener {
	void pointerDown(IBPoint p);
	void pointerDrag(IBPoint p);
	void pointerUp(IBPoint p);
	void pointerClick(IBPoint p);
	void zoomIn(IBPoint p);
	void zoomOut(IBPoint p);
}
