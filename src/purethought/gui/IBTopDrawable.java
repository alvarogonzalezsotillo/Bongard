package purethought.gui;

import purethought.geom.IBRectangle;

public interface IBTopDrawable extends IBDrawable{
	IBCanvasListener listener();
	IBRectangle originalSize();
}
