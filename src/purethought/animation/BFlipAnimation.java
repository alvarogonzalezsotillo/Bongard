package purethought.animation;

import purethought.gui.IBTransform;
import purethought.util.BFactory;

public class BFlipAnimation extends BTransformAnimation{

	
	
	protected int _totalMillis;
	protected int _currentMillis;
	protected double _radMillis;

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
	public IBTransform stepTransform(long millis, IBTransformAnimable a) {
		_currentMillis += millis;
		if( _currentMillis > _totalMillis ){
			millis = _totalMillis-_currentMillis;
			_currentMillis = _totalMillis;
		}
		IBTransform t = BFactory.instance().identityTransform();
		double angle = _radMillis*_currentMillis;
		double c = Math.cos(angle);
		t.scale(c, 1);
		return t;
	}

}
