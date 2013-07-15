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
	
	public BRectangle( double width, double height ){
		this( -width/2, -height/2, width, height );
	}
	
	public BRectangle( double x, double y, double width, double height){
        set(x,y,width,height);
    }

    public void set( double x, double y, double width, double height){
		_x = x;
		_y = y;
		_w = width;
		_h = height;
	}

	public double x(){ return _x; }
	public double y(){ return _y; }
	public double w() { return _w; }
	public double h() { return _h; }

    @Override
	public String toString() {
		return "(" + x() + "," + y() + "," + w() + "," + h() + ")";
	}

    @Override
    public boolean equals(Object obj){
        if( this == obj ){
           return true;
        }
        if( obj instanceof IBRectangle ){
            IBRectangle t = (IBRectangle) obj;
            return _x == t.x() && _y == t.y() && _w == t.w() && _h == t.h();
        }

        return false;
    }
}