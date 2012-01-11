package purethought.animation;

import purethought.gui.IBTransform;

public abstract class BTransformAnimation extends BAnimation{

	public BTransformAnimation(IBTransformAnimable ... a) {
		super(a);
	}
	
	public abstract IBTransform stepTransform(long millis, IBTransformAnimable a);
	
	@Override
	public final void stepAnimation(long millis) {
		for( IBAnimable a: animables() ){
			IBTransformAnimable ta = (IBTransformAnimable) a;
			IBTransform t = stepTransform(millis, ta);
			ta.setTemporaryTransform(t);
			if( endReached() ){
				ta.applyTemporaryTransform();
			}
		}
	}
}
