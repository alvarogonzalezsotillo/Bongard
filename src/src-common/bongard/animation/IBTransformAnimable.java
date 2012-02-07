package bongard.animation;

import bongard.geom.IBPoint;
import bongard.geom.IBTransform;

public interface IBTransformAnimable extends IBAnimable{
	IBTransform temporaryTransform();
	void setTemporaryTransform(IBTransform tt);
	void applyTemporaryTransform();
	IBPoint position();
}
