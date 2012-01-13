package purethought.animation;

import purethought.geom.IBTransform;

public abstract class BTransformAnimation extends BAnimation{

	public BTransformAnimation(int totalMillis, IBTransformAnimable ... a) {
		super(a);
		_totalMillis = totalMillis;
	}
	
	@Override
	public boolean endReached() {
		return currentMillis() >= totalMillis();
	}

	
	protected int currentMillis() {
		return _currentMillis;
	}

	protected int totalMillis() {
		return _totalMillis;
	}


	private int _totalMillis;
	private int _currentMillis;

	public final void stepTransform(long millis){
		_currentMillis += millis;
		if( _currentMillis > _totalMillis ){
			millis = _totalMillis-_currentMillis;
			_currentMillis = _totalMillis;
		}
	}
	
	public abstract IBTransform getTransform(IBTransformAnimable a);
	
	@Override
	public final void stepAnimation(long millis) {
		stepTransform(millis);
		for( IBAnimable a: animables() ){
			IBTransformAnimable ta = (IBTransformAnimable) a;
			IBTransform t = getTransform(ta);
			ta.setTemporaryTransform(t);
			if( endReached() ){
				ta.applyTemporaryTransform();
			}
		}
	}
}
