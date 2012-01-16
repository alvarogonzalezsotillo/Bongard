package purethought.gui.event;

import purethought.geom.IBPoint;
import purethought.geom.IBRectangle;

public class IBEvent {
	public static enum Type { 
		containerResized,
		pointerDown,
		pointerUp,
		pointerClick,
		pointerDragged,
		zoomIn,
		zoomOut, 
	}

	private IBRectangle _r;
	private IBPoint _p;
	private Type _t;
	
	public Type type(){
		return _t;
	}
	
	public IBPoint point(){
		return _p;
	}
	
	public IBRectangle rectangle(){
		return _r;
	}
	
	public IBEvent( Type t, IBPoint p ){
		this( t, p, null );
	}
	
	public IBEvent( Type t, IBRectangle r ){
		this( t, null, r );
	}

	public IBEvent(Type t, IBPoint p, IBRectangle r) {
		_t = t;
		_p = p;
		_r = r;
	}
	
}
