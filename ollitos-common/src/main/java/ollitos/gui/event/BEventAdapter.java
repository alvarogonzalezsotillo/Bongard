package ollitos.gui.event;

import ollitos.geom.IBPoint;
import ollitos.geom.IBRectangle;
import ollitos.geom.IBTransform;
import ollitos.geom.IBTransformHolder;

public class BEventAdapter implements IBEventListener{
	
	protected IBTransformHolder _transformHolder;

	public BEventAdapter(){
		this(null);
	}
	
	public BEventAdapter(IBTransformHolder s){
		_transformHolder = s;
	}
	
	@Override
	public boolean handle(IBEvent e) {
		switch( e.type() ){
		case keyPressed:
			return keyPressed(e);
		case containerResized:
			return containerResized(e.rectangle());
		case pointerClick:
			return pointerClick(e.point());
		case pointerDown:
			return pointerDown(e.point());
		case pointerUp:
			return pointerUp(e.point());
		case zoomIn:
			return zoomIn(e.point());
		case zoomOut:
			return zoomOut(e.point());
		case pointerDragged:
			return pointerDrag(e.point());
		case back:
			return back();
		default:
			throw new UnsupportedOperationException(e.type().toString());
		}
	}
	

	
	@Override
	public IBTransform transform() {
		if( _transformHolder == null ){
			return null;
		}
		return _transformHolder.transform();
	}
	
	public boolean  containerResized(IBRectangle containerSizeInItsCoordiates){return false;}
	public boolean  pointerDown(IBPoint pInMyCoordinates){return false;}
	public boolean  pointerDrag(IBPoint pInMyCoordinates){return false;}
	public boolean  pointerUp(IBPoint pInMyCoordinates){return false;}
	public boolean  pointerClick(IBPoint pInMyCoordinates){return false;}
	public boolean  zoomIn(IBPoint pInMyCoordinates){return false;}
	public boolean  zoomOut(IBPoint pInMyCoordinates){return false;}
	public boolean  back(){return false;}
	public boolean  keyPressed(IBEvent e) {return false;}

}
