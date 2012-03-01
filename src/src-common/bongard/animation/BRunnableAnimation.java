package bongard.animation;


public class BRunnableAnimation extends BFixedDurationAnimation{

	private boolean _runned;
	private Runnable _runnable;
	
	public BRunnableAnimation( int totalMillis, Runnable r ){
		super(totalMillis, (IBAnimable)null );
		_runnable = r;
	}

	@Override
	public void stepAnimation(long millis) {
		stepMillis(millis);
		if( endReached() && !_runned ){
			_runned = true;
			_runnable.run();
		}
	}
	
	public void cancel(){
		_runned = true;
	}
	
	@Override
	public String toString() {
		return "BRunnableAnimation(" + totalMillis() + ")";
	}

}
