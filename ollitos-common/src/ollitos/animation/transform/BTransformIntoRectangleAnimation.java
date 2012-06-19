package ollitos.animation.transform;

import ollitos.geom.BRectangle;
import ollitos.geom.IBPoint;
import ollitos.geom.IBRectangle;
import ollitos.geom.IBTransform;
import ollitos.gui.basic.IBRectangularDrawable;
import ollitos.platform.BPlatform;
import ollitos.platform.IBLogger;
import ollitos.util.BTransformUtil;

public class BTransformIntoRectangleAnimation extends BTransformAnimation{

	private IBRectangle _dst;

	public BTransformIntoRectangleAnimation(IBRectangle dst, int totalMillis,  IBTransformAnimable ... a) {
		super(totalMillis, a);
		
		_dst = dst;
	}

	@Override
	public IBTransform getTransform(IBTransformAnimable a) {
		
		
		double f = 1.0*currentMillis()/totalMillis();

		IBRectangularDrawable rd = (IBRectangularDrawable) a;
		IBRectangle os = BTransformUtil.transform(rd.transform(), rd.originalSize() );
		
		IBRectangle r = scale( os, f );
		
		
		IBTransform ret = BPlatform.instance().identityTransform();
		BTransformUtil.setTo(ret, os, r, false, false);
		return ret;
	}
	
	private IBRectangle scale( IBRectangle os, double scale ){
		IBPoint tl_os = IBRectangle.Util.topLeft(os);
		IBPoint bl_os  = IBRectangle.Util.bottomLeft(os);
		IBPoint tr_os = IBRectangle.Util.topRigth(os);
		
		IBPoint tl_v = IBPoint.Util.vector( IBRectangle.Util.topLeft(_dst), tl_os );
		IBPoint bl_v = IBPoint.Util.vector( IBRectangle.Util.bottomLeft(_dst), bl_os );
		IBPoint tr_v = IBPoint.Util.vector( IBRectangle.Util.topRigth(_dst), tr_os );
		
		tl_v = IBPoint.Util.scale(tl_v, scale);
		bl_v = IBPoint.Util.scale(bl_v, scale);
		tr_v = IBPoint.Util.scale(tr_v, scale);
		
		IBPoint tl = IBPoint.Util.add( tl_os, tl_v );
		IBPoint bl = IBPoint.Util.add( bl_os, bl_v );
		IBPoint tr = IBPoint.Util.add( tr_os, tr_v );
		
		double x = tl.x();
		double y = tl.y();
		double w = tr.x() - x;
		double h = bl.y() - y;
		IBRectangle r = new BRectangle(x, y, w, h);
		
		return r;
	}

}
