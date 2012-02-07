package bongard.animation;

public class BWaitForAnimation implements IBAnimation{

	public static class RunnableAnimation extends BAnimation{
		private boolean _endReached;
		private Runnable _runnable;
		
		public RunnableAnimation(Runnable r){
			_runnable = r;
		}

		@Override
		public void stepAnimation(long millis) {
			if( !_endReached ){
				_runnable.run();
			}
			_endReached = true;
		}

		@Override
		public boolean endReached() {
			return _endReached;
		}
	}

	
	private IBAnimation[] _waitFor;
	private IBAnimation _animation;

	public BWaitForAnimation( IBAnimation animation, IBAnimation ... waitFor ){
		_waitFor = waitFor;
		_animation = animation;
	}
	
	@Override
	public boolean needsUpdate() {
		return !endReached();
	}
	
	@Override
	public void stepAnimation(long millis) {
		for (IBAnimation w : _waitFor) {
			if( w != null && !w.endReached() ){
				return;
			}
		}
		_animation.stepAnimation(millis);
	}

	@Override
	public IBAnimable[] animables() {
		return _animation.animables();
	}

	@Override
	public void setAnimables(IBAnimable ... a) {
		_animation.setAnimables(a);
	}

	@Override
	public boolean endReached() {
		return _animation.endReached();
	}

}
