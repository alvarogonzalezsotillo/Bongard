/**
 * 
 */
package ollitos.gui.event;

import java.util.ArrayList;

import ollitos.geom.IBPoint;
import ollitos.geom.IBTransform;
import ollitos.geom.IBTransformHolder;
import ollitos.platform.BPlatform;
import ollitos.util.BException;



public class BListenerList implements IBEventListener{
	
	ArrayList<IBEventListener> _list = new ArrayList<IBEventListener>();

	IBEventListener[] _listAsArray;

	private IBEventSource _eventSource;

	private IBTransformHolder _th;
	
	public BListenerList(IBEventSource eventSource){
		this( eventSource, (IBTransformHolder)eventSource );
	}

	public BListenerList(IBEventSource eventSource, IBTransformHolder th){
		_eventSource = eventSource;
		_th = th;
	}

	public void addListener(IBEventListener listener){
		if( listener == null ){
			BPlatform.instance().logger().log( this, "listener is null" );
			return;
		}
		if( !_list.contains(listener) ){
			_list.add(listener);
			_listAsArray = null;
		}
	}
	
	public void removeListener(IBEventListener listener){
		_list.remove(listener);
		_listAsArray = null;
	}
	
	public void removeListeners(){
		_list.clear();
		_listAsArray = null;
	}
	
	
	private IBPoint pointInChildCoordinates( IBEventListener l, IBPoint p ){
		if( p == null ){
			return null;
		}
		if( l.transform() == null ){
			return p;
		}
		return l.transform().inverse().transform(p);
	}
	


	private IBEventListener[] listAsArray() {
		if (_listAsArray == null) {
			_listAsArray = _list.toArray(new IBEventListener[0]);
		}

		return _listAsArray;
	}

	@Override
	public IBTransform transform() {
		return _th.transform();
	}

	@Override
	public boolean handle(IBEvent e) {
		if( _eventSource.preHandleEvent(e) ){
			return true;
		}
		if( dispatchEvent(e) ){
			return true;
		}
		return _eventSource.postHandleEvent(e);
	}

	private boolean dispatchEvent(IBEvent e) {
		for( IBEventListener l: listAsArray() ){
			IBPoint p = pointInChildCoordinates( l, e.point() );
			IBEvent newEvent = new IBEvent( e.type(), p, e.rectangle(), e.key() );
			boolean result = l.handle(newEvent);
			if( result ){
				return true;
			}
		}
		return false;
	}
}