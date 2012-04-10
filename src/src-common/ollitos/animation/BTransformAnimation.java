package ollitos.animation;

import ollitos.geom.IBTransform;

public abstract class BTransformAnimation extends BFixedDurationAnimation{

	public BTransformAnimation(int totalMillis, IBTransformAnimable ... a) {
		super(totalMillis, a);
	}
	

	
	public abstract IBTransform getTransform(IBTransformAnimable a);
	
	@Override
	public final void stepAnimation(long millis) {
		stepMillis(millis);
		for( IBAnimable a: animables() ){
			IBTransformAnimable ta = (IBTransformAnimable) a;
			IBTransform t = getTransform(ta);
			ta.setTemporaryTransform(t);
			if( endReached() ){
				ta.applyTemporaryTransform();
			}
		}
	}
}
