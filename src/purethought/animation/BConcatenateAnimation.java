package purethought.animation;

public class BConcatenateAnimation implements IBAnimation{

	private IBAnimable[] _a;
	private IBAnimation[] _animations;

	public BConcatenateAnimation( IBAnimable[] a, IBAnimation ... animations ){
		_animations = animations;
		setAnimables(a);
	}
	
	public BConcatenateAnimation( IBAnimation [] animations, IBAnimable ... a){
		this(a,animations);
	}
	
	
	@Override
	public IBAnimable[] animables() {
		return _a;
	}

	@Override
	public boolean endReached() {
		return currentAnimation() == null;
	}
	
	private IBAnimation currentAnimation(){
		for( IBAnimation ta : _animations ){
			if( !ta.endReached() ){
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
		_a = new IBTransformAnimable[a.length];
		System.arraycopy(a, 0, _a, 0, a.length);
	}

}
