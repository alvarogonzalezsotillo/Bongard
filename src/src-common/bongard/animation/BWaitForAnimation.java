package bongard.animation;

import java.util.Arrays;

public class BWaitForAnimation implements IBAnimation{

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
	
	@Override
	public String toString() {
		return "BWaitForAnimation(" + _animation + ", " + Arrays.asList(_waitFor) + ")";
	}

}
