package ollitos.animation.transform;

import ollitos.animation.BFixedDurationAnimation;
import ollitos.animation.IBAnimable;
import ollitos.geom.IBTransform;

public abstract class BTemporaryTransformAnimation extends BFixedDurationAnimation{

	public BTemporaryTransformAnimation(int totalMillis, IBTemporaryTransformAnimable ... a) {
		super(totalMillis, a);
	}
	

	
	public abstract IBTransform getTransform(IBTemporaryTransformAnimable a);
	
	@Override
	public final void stepAnimation(long millis) {
		stepMillis(millis);
		for( IBAnimable a: animables() ){
			IBTemporaryTransformAnimable ta = (IBTemporaryTransformAnimable) a;
			IBTransform t = getTransform(ta);
			setTemporaryTransform(ta, t);
			if( endReached() ){
				ta.applyTemporaryTransform();
			}
		}
	}


	protected void setTemporaryTransform(IBTemporaryTransformAnimable ta, IBTransform t) {
		ta.setTemporaryTransform(t);
	}
}
