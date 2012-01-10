package purethought.animation;

import purethought.gui.BFactory;
import purethought.gui.IBTransform;

public class BFlipAnimation extends BTransformAnimation{

	
	
	protected int _totalMillis;
	protected int _currentMillis;
	protected double _radMillis;
	protected double _angle = 0;

	public BFlipAnimation(double radMillis, int totalMillis, IBTransformAnimable ... a) {
		super(a);
		_radMillis = radMillis;
		_totalMillis = totalMillis;
	}

	@Override
	public boolean endReached() {
		return _currentMillis >= _totalMillis;
	}

	@Override
	public IBTransform stepTransform(long millis) {
		_currentMillis += millis;
		if( _currentMillis > _totalMillis ){
			millis = _totalMillis-_currentMillis;
			_currentMillis = _totalMillis;
		}
		IBTransform t = BFactory.instance().identityTransform();
		_angle = _radMillis*_currentMillis;
		double c = Math.cos(_angle);
		t.scale(c, 1);
		return t;
	}

}
