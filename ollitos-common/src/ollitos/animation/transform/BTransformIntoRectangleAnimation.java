package ollitos.animation.transform;

import ollitos.animation.BFixedDurationAnimation;
import ollitos.animation.IBAnimable;
import ollitos.geom.BRectangle;
import ollitos.geom.IBPoint;
import ollitos.geom.IBRectangle;
import ollitos.geom.IBTransform;
import ollitos.gui.basic.BDrawable;
import ollitos.gui.basic.IBDrawable;
import ollitos.platform.BPlatform;
import ollitos.platform.IBCanvas;
import ollitos.util.BTransformUtil;

public class BTransformIntoRectangleAnimation extends BFixedDurationAnimation{

	private IBRectangle _dst;
	private IBRectangle _src;

	public BTransformIntoRectangleAnimation(IBRectangle src, IBRectangle dst, int totalMillis, IBDrawable ... a) {
		super(totalMillis, a);
		
		_src = src;
		_dst = dst;
	}

	
	@Override
	public void stepAnimation(long millis) {
		stepMillis(millis);
		double f = 1.0*currentMillis()/totalMillis();

		for( IBAnimable a : animables() ){
		
			IBDrawable rd = (BDrawable) a;
			IBRectangle os = rd.originalSize();
			
			IBRectangle r = scale( _src, f );
			
			IBTransform ret = rd.transform();
			BTransformUtil.setTo(ret, os, r, true, true);

            String log = "_dst:" + _dst + " -- f:" + f + " -- " + r;
			BPlatform.instance().logger().log( this, log );
        }
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
	
	public static BTransformIntoRectangleAnimation zoom( int millis, IBDrawable r, IBPoint beforeSamePoint, IBPoint afterSamePoint, double scale ){
		IBRectangle src = BTransformUtil.transform( r.transform(), r.originalSize() );
		IBRectangle dst = IBRectangle.Util.scale(src, scale);

		double fx = (beforeSamePoint.x() - src.x())/src.w();
		double fy = (beforeSamePoint.y() - src.y())/src.h();
		
		double x = afterSamePoint.x()-dst.w()*fx;
		double y = afterSamePoint.y()-dst.h()*fy;
		
		dst = new BRectangle(x,y,dst.w(),dst.h());
		
		
		return new BTransformIntoRectangleAnimation(src, dst, millis, r);
	}

    @Override
    public String toString() {
        double f = 1.0*currentMillis()/totalMillis();
        return "TransformIntoRectangle:" + f;
    }
}
