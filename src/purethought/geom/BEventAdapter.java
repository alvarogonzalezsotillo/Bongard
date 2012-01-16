package purethought.geom;

import purethought.gui.IBDrawable;
import purethought.gui.IBEvent;
import purethought.gui.IBEventListener;
import purethought.gui.IBEventSource;

public abstract class BEventAdapter {
	
	protected IBEventSource _source;

	public BEventAdapter(IBEventSource s){
		_source = s;
	}
	
	IBEventListener _listener = new IBEventListener() {
		@Override
		public boolean handle(IBEvent e) {
			switch( e.type() ){
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
			default:
				throw new UnsupportedOperationException(e.type().toString());
			}
		}
		
		@Override
		public IBEventSource source() {
			return _source;
		}
	};
	
	public boolean  containerResized(IBRectangle containerSizeInItsCoordiates){return false;}
	public boolean  pointerDown(IBPoint pInMyCoordinates){return false;}
	public boolean  pointerDrag(IBPoint pInMyCoordinates){return false;}
	public boolean  pointerUp(IBPoint pInMyCoordinates){return false;}
	public boolean  pointerClick(IBPoint pInMyCoordinates){return false;}
	public boolean  zoomIn(IBPoint pInMyCoordinates){return false;}
	public boolean  zoomOut(IBPoint pInMyCoordinates){return false;}
	
}
