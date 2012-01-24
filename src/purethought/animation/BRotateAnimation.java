package purethought.animation;

import purethought.geom.IBTransform;
import purethought.platform.BFactory;

public class BRotateAnimation extends BTransformAnimation{

	private double _radMillis;
	
	
	public BRotateAnimation( double radMillis, int totalMillis, IBTransformAnimable ... a ){
		super(totalMillis, a);
		_radMillis = radMillis;
	}
	
	@Override
	public IBTransform getTransform(IBTransformAnimable a) {
		IBTransform t = BFactory.instance().identityTransform();
		double angle = _radMillis*currentMillis(); 
		t.rotate(angle);
		return t;
	}

}
