package purethought.animation;

import purethought.gui.IBPoint;
import purethought.gui.IBTransform;

public interface IBTransformAnimable extends IBAnimable{
	IBTransform temporaryTransform();
	void setTemporaryTransform(IBTransform tt);
	void applyTemporaryTransform();
	IBPoint position();
}
