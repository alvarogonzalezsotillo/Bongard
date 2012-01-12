package purethought.gui;

import purethought.geom.IBRectangle;
import purethought.geom.IBTransform;

public interface IBCanvasDrawable {
	void addedTo(IBCanvas c);
	void draw(IBCanvas c, IBTransform aditionalTransform);
	IBRectangle size();
}
