package bongard.animation;

import java.util.Arrays;

public class BConcatenateAnimation implements IBAnimation{

	private IBAnimation[] _animations;

	public BConcatenateAnimation( IBAnimable[] a, IBAnimation ... animations ){
		_animations = animations;
		setAnimables(a);
	}
	
	public BConcatenateAnimation( IBAnimation [] animations, IBAnimable ... a){
		this(a,animations);
	}
	
	public BConcatenateAnimation(IBAnimation ... animations ){
		_animations = animations;
	}
	
	
	@Override
	public boolean endReached() {
		return currentAnimation() == null;
	}
	
	private IBAnimation currentAnimation(){
		for( IBAnimation ta : _animations ){
			if( ta != null && !ta.endReached() ){
				return ta;
			}
		}
		return null;
	}

	@Override
	public void stepAnimation(long millis) {
		IBAnimation a = currentAnimation();
		if( a == null ){
			return;
		}
		a.stepAnimation(millis);
	}

	@Override
	public final void setAnimables(IBAnimable...a){
		for( IBAnimation an : _animations ){
			an.setAnimables(a);
		}
	}

	@Override
	public IBAnimable[] animables() {
		return _animations[0].animables();
	}

	@Override
	public boolean needsUpdate() {
		return !endReached();
	}

	@Override
	public String toString() {
		return "BConcatenateAnimation(" + Arrays.asList(_animations).toString() + ")";
	}
}
