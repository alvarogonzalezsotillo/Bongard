package ollitos.animation;

import ollitos.geom.IBTransform;
import ollitos.platform.BPlatform;

public class BFlipAnimation extends BTransformAnimation{

	
	
	protected double _radMillis;

	public BFlipAnimation(double radMillis, int totalMillis, IBTransformAnimable ... a) {
		super(totalMillis,a);
		_radMillis = radMillis;
	}

	@Override
	public IBTransform getTransform(IBTransformAnimable a) {
		IBTransform t = BPlatform.instance().identityTransform();
		double angle = _radMillis*currentMillis();
		double c = Math.cos(angle);
		t.scale(c, 1);
		return t;
	}

}
