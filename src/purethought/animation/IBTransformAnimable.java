package purethought.animation;

import purethought.gui.IBTransform;

public interface IBTransformAnimable extends IBAnimable{
	IBTransform temporaryTransform(IBAnimation a);
	void setTemporaryTransform(IBTransform tt, IBAnimation a);

}
