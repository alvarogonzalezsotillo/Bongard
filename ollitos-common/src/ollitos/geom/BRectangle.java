package ollitos.geom;

import java.io.Serializable;

import ollitos.platform.BPlatform;



public class BRectangle implements IBRectangle, Serializable{
	private double _x;
	private double _y;
	private double _w;
	private double _h;
	
	public BRectangle(){
		this(0,0,0,0);
	}
	
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
	
	@Override
	public String toString() {
		return "(" + x() + "," + y() + "," + w() + "," + h() + ")";
	}

	public static IBRectangle scale(IBRectangle r, double f) {
		double w = r.w()*f;
		double h = r.h()*f;
		double x = (r.x()+r.w()/2)-w/2;
		double y = (r.y()+r.h()/2)-h/2;
		
		return new BRectangle( x, y, w, h );
	}
}