package ollitos.animation;

public class BRefreshAnimation extends BAnimation{

	private boolean _end = false;
	private String _msg;
	
	
	private BRefreshAnimation() {
		this("");
	}
	
	public BRefreshAnimation(String msg) {
		_msg = msg;
	}
	
	@Override
	public void stepAnimation(long millis) {
		_end = true;
	}

	@Override
	public boolean endReached() {
		return _end;
	}
	
	@Override
	public String toString() {
		return getClass().getName() + ": " + _msg;
	}

}
