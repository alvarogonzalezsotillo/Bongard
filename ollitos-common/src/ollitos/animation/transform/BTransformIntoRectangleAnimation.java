package ollitos.animation.transform;

import ollitos.geom.IBPoint;
import ollitos.geom.IBRectangle;
import ollitos.geom.IBTransform;
import ollitos.gui.basic.IBRectangularDrawable;
import ollitos.util.BTransformUtil;

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
		IBRectangle os = BTransformUtil.transform(rd.transform(), rd.originalSize() );
		
		IBPoint tl_v = IBPoint.Util.vector( IBRectangle.Util.topLeft(os), IBRectangle.Util.topLeft(_dst) );
		
	}

}
