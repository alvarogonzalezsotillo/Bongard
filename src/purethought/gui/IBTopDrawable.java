package purethought.gui;

import purethought.geom.IBRectangle;

public interface IBTopDrawable extends IBDrawable{
	void addedTo(IBCanvas c);
	IBRectangle originalSize();
}
