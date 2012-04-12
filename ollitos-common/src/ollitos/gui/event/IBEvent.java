package ollitos.gui.event;

import ollitos.geom.IBPoint;
import ollitos.geom.IBRectangle;
import ollitos.gui.event.IBEvent.Type;

public class IBEvent {
	public static enum Type { 
		containerResized,
		pointerDown,
		pointerUp,
		pointerClick,
		pointerDragged,
		back,
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
	
	/**
	 * 
	 * @return size of the container, in its own coordinate system
	 */
	public IBRectangle rectangle(){
		return _r;
	}

	public IBEvent( Type t ){
		this( t, null, null );
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
	
	@Override
	public String toString() {
		return "IBEvent(" + type() + " -- " + point() + " -- " + rectangle() + ")";
	}
}
