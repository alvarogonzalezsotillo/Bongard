package purethought.animation;

import purethought.geom.IBTransform;
import purethought.util.BFactory;

public class BCompoundTransformAnimation implements IBAnimation {

	private IBTransformAnimable[] _a;
	private BTransformAnimation[] _animations;

	public BCompoundTransformAnimation( IBTransformAnimable[] a, BTransformAnimation ... animations ){
		_animations = animations;
		setAnimables(a);
	}

	public BCompoundTransformAnimation( BTransformAnimation[] animations, IBTransformAnimable... a){
		this( a, animations );
	}

	@Override
	public IBAnimable[] animables() {
		return _a;
	}

	@Override
	public boolean endReached() {
		for (BTransformAnimation a : _animations) {
			if( !a.endReached() ){
				return false;
			}
		}
		return true;
	}

	@Override
	public void stepAnimation(long millis) {
		IBTransform t = BFactory.instance().identityTransform();
		for (BTransformAnimation a : _animations) {
			if( a.endReached() ){
				continue;
			}
			for( IBAnimable an: animables() ){
				IBTransformAnimable ta = (IBTransformAnimable) an;
				IBTransform st = a.stepTransform(millis, ta);
				t.concatenate(st);
				ta.setTemporaryTransform(t);
			}
		}
		
		if( endReached() ){
			for( IBAnimable an: animables() ){
				IBTransformAnimable ta = (IBTransformAnimable) an;
				ta.applyTemporaryTransform();
			}			
		}
	}
	
	@Override
	public final void setAnimables(IBAnimable...a){
		_a = new IBTransformAnimable[a.length];
		System.arraycopy(a, 0, _a, 0, a.length);
	}
	
}
