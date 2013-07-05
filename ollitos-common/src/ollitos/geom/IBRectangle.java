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

        public static boolean inside( IBRectangle r, IBPoint p){
            if( p.x() < r.x() || p.x() > r.x()+r.w() ){
                return false;
            }
            if( p.y() < r.y() || p.y() > r.y()+r.h() ){
                return false;
            }
            return true;
        }

        public static IBRectangle grow( IBRectangle r, double d ){
            double y = r.y()-d;
            double x = r.x()-d;
            double w = r.w()+d*2;
            double h = r.h()+d*2;
            IBRectangle ret = new BRectangle(x, y, w, h);
            return ret;
        }

        public static IBRectangle centerAtOrigin(IBRectangle r){
            return new BRectangle(-r.w()/2, -r.h()/2, r.w(), r.h() );
        }

        public static IBPoint center(IBRectangle r){
            return BPlatform.instance().point( r.x()+r.w()/2, r.y()+r.h()/2);
        }

        public static IBRectangle centerAt(IBRectangle r, IBPoint center){
            return new BRectangle( center.x()-r.w()/2, center.y()-r.h()/2, r.w(), r.h() );
        }

        public static IBRectangle resize( IBRectangle r, double d ){
            double w = r.w()*d;
            double h = r.h()*d;
            double x = r.x()+(r.w()-w);
            double y = r.y()+(r.h()-h);
            IBRectangle ret = new BRectangle(x, y, w, h);
            return ret;
        }

        public static IBRectangle scale(IBRectangle r, double f) {
            double w = r.w()*f;
            double h = r.h()*f;
            double x = (r.x()+r.w()/2)-w/2;
            double y = (r.y()+r.h()/2)-h/2;

            return new BRectangle( x, y, w, h );
        }
    }
	
	double x();
	double y();
	double w();
	double h();
}