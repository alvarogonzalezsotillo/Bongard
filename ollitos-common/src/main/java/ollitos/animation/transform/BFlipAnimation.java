package ollitos.animation.transform;

import ollitos.geom.IBTransform;
import ollitos.platform.BPlatform;

public class BFlipAnimation extends BTemporaryTransformAnimation{

	
	
	protected double _radMillis;

	public BFlipAnimation(double radMillis, int totalMillis, IBTemporaryTransformAnimable ... a) {
		super(totalMillis,a);
		_radMillis = radMillis;
	}

	@Override
	public IBTransform getTransform(IBTemporaryTransformAnimable a) {
		IBTransform t = BPlatform.instance().identityTransform();
		double angle = _radMillis*currentMillis();
		double c = Math.cos(angle);
		t.scale(c, 1);
		return t;
	}

}
