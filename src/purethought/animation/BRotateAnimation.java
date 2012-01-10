package purethought.animation;

import purethought.gui.BFactory;
import purethought.gui.IBTransform;

public class BRotateAnimation extends BTransformAnimation{

	private double _radSecond;
	private double _angle;
	
	
	public BRotateAnimation( double radSecond, IBTransformAnimable[] a ){
		super(a);
		_radSecond = radSecond;
		_angle = 0;
	}
	
	@Override
	protected IBTransform stepTransform(long millis) {
		IBTransform t = BFactory.instance().identityTransform();
		t.rotate(_angle);
		_angle += _radSecond*millis/1000;
		return t;
	}

	@Override
	public boolean endReached() {
		return false;
	}
}
