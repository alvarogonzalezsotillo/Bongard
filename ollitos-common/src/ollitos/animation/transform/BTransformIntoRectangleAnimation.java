package ollitos.animation.transform;

import ollitos.geom.BRectangle;
import ollitos.geom.IBPoint;
import ollitos.geom.IBRectangle;
import ollitos.geom.IBTransform;
import ollitos.gui.basic.IBRectangularDrawable;
import ollitos.platform.BPlatform;
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
		
		IBPoint tl_os = IBRectangle.Util.topLeft(os);
		IBPoint bl_os  = IBRectangle.Util.bottomLeft(os);
		IBPoint tr_os = IBRectangle.Util.topRigth(os);
		
		IBPoint tl_v = IBPoint.Util.vector( tl_os, IBRectangle.Util.topLeft(_dst) );
		IBPoint bl_v = IBPoint.Util.vector( bl_os , IBRectangle.Util.bottomLeft(_dst) );
		IBPoint tr_v = IBPoint.Util.vector( tr_os, IBRectangle.Util.topRigth(_dst) );
		
		tl_v = IBPoint.Util.scale(tl_v, f);
		bl_v = IBPoint.Util.scale(tl_v, f);
		tr_v = IBPoint.Util.scale(tl_v, f);
		
		IBPoint tl = IBPoint.Util.add( tl_os, tl_v );
		IBPoint bl = IBPoint.Util.add( bl_os, bl_v );
		IBPoint tr = IBPoint.Util.add( tr_os, tr_v );
		
		double x = tl.x();
		double y = tl.y();
		double w = tr.x()-x;
		double h = bl.y() - y;
		IBRectangle r = new BRectangle(x, y, w, h);
		
		IBTransform ret = BPlatform.instance().identityTransform();
		BTransformUtil.setTo(ret, os, r, false, false);
		return ret;
	}

}
