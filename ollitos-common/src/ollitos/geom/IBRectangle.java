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
	}
	
	double x();
	double y();
	double w();
	double h();
}