package purethought.animation;

import purethought.gui.IBTransform;
import purethought.util.BFactory;

public class BScaleAnimation extends BTransformAnimation{
	
	
	private double _fx;
	private double _fy;
	private int _totalMillis;
	private int _currentMillis;

	public BScaleAnimation( double fx, double fy, int totalMillis, IBTransformAnimable ... a){
		super(a);
		_fx = fx;
		_fy = fy;
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
		double fx = 1+(_fx-1)*_currentMillis/_totalMillis;
		double fy = 1+(_fy-1)*_currentMillis/_totalMillis;
		t.scale(fx, fy);
		return t;
	}

	@Override
	public boolean endReached() {
		return _currentMillis >= _totalMillis;
	}

}
