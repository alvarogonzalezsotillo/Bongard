package ollitos.geom;

import java.io.Serializable;

public interface IBPoint extends Serializable{
	
	public static final class Util{
		public static double distance(IBPoint p1, IBPoint p2 ){
			double y = p1.y() - p2.y();
			double x = p1.x() - p2.x();
			return Math.sqrt( y*y + x*x );
		}
	}
	
	public double x();
	public double y();
}
