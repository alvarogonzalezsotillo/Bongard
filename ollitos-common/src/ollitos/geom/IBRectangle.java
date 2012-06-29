package ollitos.geom;

import ollitos.platform.BPlatform;

public interface IBRectangle{
	
	public static class Util{
		public static IBPoint topLeft(IBRectangle r){
			return BPlatform.instance().point(r.x(),r.y());
		}
		public static IBPoint topRigth(IBRectangle r){
			return BPlatform.instance().point(r.x()+r.w(),r.y());
		}
		public static IBPoint bottomLeft(IBRectangle r){
			return BPlatform.instance().point(r.x(),r.y()+r.h());
		}
		public static IBPoint bottomRigth(IBRectangle r){
			return BPlatform.instance().point(r.x()+r.w(),r.y()+r.h());
		}
		public static IBRectangle scale( IBRectangle os, IBRectangle dst, double scale ){
			IBPoint tl_os = IBRectangle.Util.topLeft(os);
			IBPoint bl_os  = IBRectangle.Util.bottomLeft(os);
			IBPoint tr_os = IBRectangle.Util.topRigth(os);
			
			IBPoint tl_v = IBPoint.Util.vector( IBRectangle.Util.topLeft(dst), tl_os );
			IBPoint bl_v = IBPoint.Util.vector( IBRectangle.Util.bottomLeft(dst), bl_os );
			IBPoint tr_v = IBPoint.Util.vector( IBRectangle.Util.topRigth(dst), tr_os );
			
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
	
	double x();
	double y();
	double w();
	double h();
}