package purethought.animation;

import purethought.geom.IBPoint;
import purethought.geom.IBTransform;

public interface IBTransformAnimable extends IBAnimable{
	IBTransform temporaryTransform();
	void setTemporaryTransform(IBTransform tt);
	void applyTemporaryTransform();
	IBPoint position();
}
