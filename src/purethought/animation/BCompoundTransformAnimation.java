package purethought.animation;

import purethought.geom.IBTransform;
import purethought.util.BFactory;

public class BCompoundTransformAnimation implements IBAnimation {

	private BTransformAnimation[] _animations;

	public BCompoundTransformAnimation( BTransformAnimation ...animations ){
		this( new IBTransformAnimable[0], animations);
	}
	
	public BCompoundTransformAnimation( IBTransformAnimable[] a, BTransformAnimation ... animations ){
		_animations = animations;
		setAnimables(a);
	}

	public BCompoundTransformAnimation( BTransformAnimation[] animations, IBTransformAnimable... a){
		this( a, animations );
	}

	@Override
	public boolean needsUpdate() {
		return !endReached();
	}

	@Override
	public IBAnimable[] animables() {
		return _animations[0].animables();
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
			a.stepTransform(millis);
			for( IBAnimable an: animables() ){
				IBTransformAnimable ta = (IBTransformAnimable) an;
				IBTransform st = a.getTransform(ta);
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
		for( BTransformAnimation an: _animations ){
			an.setAnimables(a);
		}
	}
	
}
