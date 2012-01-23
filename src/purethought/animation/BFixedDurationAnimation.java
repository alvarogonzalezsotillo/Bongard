package purethought.animation;

public abstract class BFixedDurationAnimation extends BAnimation{

	protected int _totalMillis;
	protected int _currentMillis;
	
	protected int currentMillis() {
		return _currentMillis;
	}

	protected int totalMillis() {
		return _totalMillis;
	}

	@Override
	public boolean endReached() {
		return currentMillis() >= totalMillis();
	}

	
	public final void stepMillis(long millis){
		_currentMillis += millis;
		if( _currentMillis > _totalMillis ){
			millis = _totalMillis-_currentMillis;
			_currentMillis = _totalMillis;
		}
	}

	
	
	public BFixedDurationAnimation(int totalMillis, IBAnimable ... a) {
		super(a);
		_totalMillis = totalMillis;
	}

}
