package purethought.animation;

public class BWaitForAnimation implements IBAnimation{

	private IBAnimation[] _waitFor;
	private IBAnimation _animation;

	public BWaitForAnimation( IBAnimation animation, IBAnimation ... waitFor ){
		_waitFor = waitFor;
		_animation = animation;
	}
	
	@Override
	public void stepAnimation(long millis) {
		for (IBAnimation w : _waitFor) {
			if( !w.endReached() ){
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
