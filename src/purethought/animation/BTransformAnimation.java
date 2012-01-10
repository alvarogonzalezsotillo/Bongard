package purethought.animation;

import purethought.gui.IBTransform;

public abstract class BTransformAnimation extends BAnimation{

	public BTransformAnimation(IBTransformAnimable ... a) {
		super(a);
	}
	
	public abstract IBTransform stepTransform(long millis);
	
	@Override
	public final void stepAnimation(long millis) {
		IBTransform t = stepTransform(millis);
		for( IBAnimable a: animables() ){
			IBTransformAnimable ta = (IBTransformAnimable) a;
			ta.setTemporaryTransform(t);
		}
	}
}
