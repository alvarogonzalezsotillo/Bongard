package ollitos.animation;

import ollitos.geom.IBTransform;
import ollitos.platform.BPlatform;

public class BRotateAnimation extends BTransformAnimation{

	private double _radMillis;
	
	
	public BRotateAnimation( double radMillis, int totalMillis, IBTransformAnimable ... a ){
		super(totalMillis, a);
		_radMillis = radMillis;
	}
	
	@Override
	public IBTransform getTransform(IBTransformAnimable a) {
		IBTransform t = BPlatform.instance().identityTransform();
		double angle = _radMillis*currentMillis(); 
		t.rotate(angle);
		return t;
	}

}
