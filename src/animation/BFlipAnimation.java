package animation;

import purethought.gui.BFactory;
import purethought.gui.IBTransform;

public class BFlipAnimation extends BTransformAnimation{

	
	
	protected double _radSecond;
	protected double _angle = 0;
	protected double _axisAngle;

	public BFlipAnimation(double axisAngle, double radSecond, IBTransformAnimable[] a) {
		super(a);
		_radSecond = radSecond;
		_axisAngle = axisAngle;
	}

	@Override
	public boolean endReached() {
		return false;
	}

	@Override
	protected IBTransform stepTransform(long millis) {
		IBTransform t = BFactory.instance().identityTransform();
		double c = Math.cos(_angle);
		t.scale(c, 1);
		_angle += _radSecond*millis/1000;
		if( _angle > Math.PI*2){
			_angle -= Math.PI*2;
		}
		return t;
	}

}
