package ollitos.animation.transform;

import ollitos.geom.IBRectangle;
import ollitos.geom.IBTransform;
import ollitos.gui.basic.IBRectangularDrawable;

public class BTransformIntoRectangleAnimation extends BTransformAnimation{

	private IBRectangle _dst;

	public BTransformIntoRectangleAnimation(IBRectangle dst, int totalMillis,  IBRectangularDrawable[] a) {
		super(totalMillis, a);
		_dst = dst;
	}

	@Override
	public IBTransform getTransform(IBTransformAnimable a) {
		double f = currentMillis()/totalMillis();

		IBRectangularDrawable rd = (IBRectangularDrawable) a;
		
		double x = rd.
	}

}
