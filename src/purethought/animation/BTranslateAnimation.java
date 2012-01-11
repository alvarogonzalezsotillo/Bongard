package purethought.animation;

import purethought.gui.IBPoint;
import purethought.gui.IBTransform;
import purethought.util.BFactory;

public class BTranslateAnimation extends BTransformAnimation{

	private IBPoint _dest;
	private int _totalMillis;
	private int _currentMillis;
	
	
	public BTranslateAnimation( IBPoint dest, int totalMillis, IBTransformAnimable ... a ){
		super(a);
		_dest = dest;
		_totalMillis = totalMillis;
	}
	
	@Override
	public IBTransform stepTransform(long millis, IBTransformAnimable a) {
		_currentMillis += millis;
		if( _currentMillis > _totalMillis ){
			millis = _totalMillis-_currentMillis;
			_currentMillis = _totalMillis;
		}
		
		IBPoint origin = a.position();
		double tx = _currentMillis*(_dest.x() - origin.x())/_totalMillis;
		double ty = _currentMillis*(_dest.y() - origin.y())/_totalMillis;
		
		IBTransform ret = BFactory.instance().identityTransform();
		ret.translate(tx, ty);
		
		return ret;
	}

	@Override
	public boolean endReached() {
		return _currentMillis >= _totalMillis;
	}

}
