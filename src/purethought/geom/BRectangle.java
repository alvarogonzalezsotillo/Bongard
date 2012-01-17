package purethought.geom;



public class BRectangle implements IBRectangle{
	private double _x;
	private double _y;
	private double _w;
	private double _h;
	public BRectangle( double x, double y, double width, double height){
		_x = x;
		_y = y;
		_w = width;
		_h = height;
	}

	public double x(){ return _x; }
	public double y(){ return _y; }
	public double w() { return _w; }
	public double h() { return _h; }
	
	public static boolean inside( IBRectangle r, IBPoint p){
		if( p.x() < r.x() || p.x() > r.x()+r.w() ){
			return false;
		}
		if( p.y() < r.y() || p.y() > r.y()+r.h() ){
			return false;
		}
		return true;
	}
}