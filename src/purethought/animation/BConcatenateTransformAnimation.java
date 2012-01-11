package purethought.animation;

public class BConcatenateTransformAnimation implements IBAnimation{

	private IBTransformAnimable[] _a;
	private BTransformAnimation[] _animations;
	private int _index;
	private boolean _aborted;

	public BConcatenateTransformAnimation( IBTransformAnimable[] a, BTransformAnimation ... animations ){
		_a = a;
		_animations = animations;
	}
	
	@Override
	public void abortAnimation() {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean aborted() {
		return _aborted;
	}

	@Override
	public IBAnimable[] animables() {
		return _a;
	}

	@Override
	public void applyAnimation() {
		for( IBAnimable a: animables() ){
			IBTransformAnimable ta = (IBTransformAnimable) a;
			ta.applyAnimation(this);
		}
	}

	@Override
	public boolean endReached() {
		return currentAnimation() == null;
	}
	
	private BTransformAnimation currentAnimation(){
		if( _index >= _animations.length ){
			return null;
		}
		return _animations[_index];
	}

	@Override
	public void stepAnimation(long millis) {
		BTransformAnimation a = _animations[_index];
	}

}
