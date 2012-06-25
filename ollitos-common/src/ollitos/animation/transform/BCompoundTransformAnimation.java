package ollitos.animation.transform;

import ollitos.animation.IBAnimable;
import ollitos.animation.IBAnimation;
import ollitos.geom.IBTransform;
import ollitos.platform.BPlatform;

public class BCompoundTransformAnimation implements IBAnimation {

	private BTemporaryTransformAnimation[] _animations;

	public BCompoundTransformAnimation( BTemporaryTransformAnimation ...animations ){
		this( new IBTemporaryTransformAnimable[0], animations);
	}
	
	public BCompoundTransformAnimation( IBTemporaryTransformAnimable[] a, BTemporaryTransformAnimation ... animations ){
		_animations = animations;
		setAnimables(a);
	}

	public BCompoundTransformAnimation( BTemporaryTransformAnimation[] animations, IBTemporaryTransformAnimable... a){
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
		for (BTemporaryTransformAnimation a : _animations) {
			if( !a.endReached() ){
				return false;
			}
		}
		return true;
	}

	@Override
	public void stepAnimation(long millis) {
		IBTransform t = BPlatform.instance().identityTransform();
		for (BTemporaryTransformAnimation a : _animations) {
			if( a.endReached() ){
				continue;
			}
			a.stepMillis(millis);
			for( IBAnimable an: animables() ){
				IBTemporaryTransformAnimable ta = (IBTemporaryTransformAnimable) an;
				IBTransform st = a.getTransform(ta);
				t.concatenate(st);
				ta.setTemporaryTransform(t);
			}
		}
		
		if( endReached() ){
			for( IBAnimable an: animables() ){
				IBTemporaryTransformAnimable ta = (IBTemporaryTransformAnimable) an;
				ta.applyTemporaryTransform();
			}			
		}
	}
	
	@Override
	public final void setAnimables(IBAnimable...a){
		for( BTemporaryTransformAnimation an: _animations ){
			an.setAnimables(a);
		}
	}
	
}
