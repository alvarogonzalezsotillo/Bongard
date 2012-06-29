package ollitos.geom;

import java.io.Serializable;

import ollitos.platform.BPlatform;

public interface IBPoint extends Serializable{
	
	public static final class Util{
		public static double distance(IBPoint p1, IBPoint p2 ){
			double y = p1.y() - p2.y();
			double x = p1.x() - p2.x();
			return Math.sqrt( y*y + x*x );
		}
		public static IBPoint normalize(IBPoint p){
			double d = p.y()*p.y() + p.x()*p.x();
			IBPoint ret = BPlatform.instance().point( p.x()/d, p.y()/d);
			return ret;
		}
		public static IBPoint vector(IBPoint p1, IBPoint p2){
			IBPoint ret = BPlatform.instance().point( p1.x()-p2.x(), p1.y()-p2.y());
			return ret;
		}
		
		public static IBPoint scale(IBPoint p, double scale){
			IBPoint ret = BPlatform.instance().point( p.x()*scale, p.y()*scale );
			return ret;
		}
		
		public static IBPoint add(IBPoint p1, IBPoint p2){
			IBPoint ret = BPlatform.instance().point( p1.x()+p2.x(), p1.y()+p2.y());
			return ret;
		}
	}
	
	public double x();
	public double y();
}
