package purethought.animation;

import purethought.gui.IBTransform;
import purethought.util.BFactory;

public class BRotateAnimation extends BTransformAnimation{

	private double _radMillis;
	private int _totalMillis;
	private int _currentMillis;
	
	
	public BRotateAnimation( double radMillis, int totalMillis, IBTransformAnimable ... a ){
		super(a);
		_radMillis = radMillis;
		_totalMillis = totalMillis;
	}
	
	@Override
	public IBTransform stepTransform(long millis) {
		_currentMillis += millis;
		if( _currentMillis > _totalMillis ){
			millis = _totalMillis-_currentMillis;
			_currentMillis = _totalMillis;
		}

		IBTransform t = BFactory.instance().identityTransform();
		double angle = _radMillis*_currentMillis; 
		t.rotate(angle);
		return t;
	}

	@Override
	public boolean endReached() {
		return _currentMillis >= _totalMillis;
	}
}
