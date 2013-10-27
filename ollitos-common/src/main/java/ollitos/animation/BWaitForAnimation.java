package ollitos.animation;

import java.util.Arrays;

import ollitos.platform.BPlatform;
import ollitos.util.BException;

public class BWaitForAnimation implements IBAnimation{

	private IBAnimation[] _waitFor;
	private IBAnimation _animation;

	public BWaitForAnimation( IBAnimation animation, IBAnimation ... waitFor ){
		_waitFor = waitFor;
		_animation = animation;
		for( IBAnimation a: _waitFor ){
			if( a == this ){
				throw new BException("waiting for myself", null );
			}
		}
		if( _animation == this ){
			throw new BException("waiting for myself", null );
		}
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
	
	private static int __ = 0;
	
	@Override
	public String toString() {
		try{
			__++;
			if( __ > 10 ){
				new Exception().printStackTrace();
			}
			return "BWaitForAnimation(" + _animation + ", " + Arrays.asList(_waitFor) + ")";
		}
		finally{
			__--;
		}
	}

}
