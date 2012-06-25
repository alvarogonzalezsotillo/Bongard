package ollitos.animation.transform;

import ollitos.geom.IBTransform;
import ollitos.platform.BPlatform;

public class BScaleAnimation extends BTemporaryTransformAnimation{
	
	
	private double _fx;
	private double _fy;

	public BScaleAnimation( double fx, double fy, int totalMillis, IBTemporaryTransformAnimable ... a){
		super(totalMillis, a);
		_fx = fx;
		_fy = fy;
	}

		
	public IBTransform getTransform(IBTemporaryTransformAnimable a) {
		IBTransform t = BPlatform.instance().identityTransform();
		double fx = 1+(_fx-1)*currentMillis()/totalMillis();
		double fy = 1+(_fy-1)*currentMillis()/totalMillis();
		t.scale(fx, fy);
		return t;
	}


}
