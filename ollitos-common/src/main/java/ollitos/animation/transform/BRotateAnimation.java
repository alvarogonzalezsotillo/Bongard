package ollitos.animation.transform;

import ollitos.geom.IBTransform;
import ollitos.platform.BPlatform;

public class BRotateAnimation extends BTemporaryTransformAnimation{

	private double _radMillis;
	
	
	public BRotateAnimation( double radMillis, int totalMillis, IBTemporaryTransformAnimable ... a ){
		super(totalMillis, a);
		_radMillis = radMillis;
	}
	
	@Override
	public IBTransform getTransform(IBTemporaryTransformAnimable a) {
		IBTransform t = BPlatform.instance().identityTransform();
		double angle = _radMillis*currentMillis(); 
		t.rotate(angle);
		return t;
	}

}
