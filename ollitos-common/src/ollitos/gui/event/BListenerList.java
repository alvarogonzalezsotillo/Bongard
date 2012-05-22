/**
 * 
 */
package ollitos.gui.event;

import java.util.ArrayList;

import ollitos.geom.IBPoint;
import ollitos.geom.IBTransform;
import ollitos.util.BException;



public class BListenerList implements IBEventListener{
	
	ArrayList<IBEventListener> _list = new ArrayList<IBEventListener>();

	IBEventListener[] _listAsArray;

	private IBEventSource _eventSource;
	
	public BListenerList(IBEventSource eventSource){
		_eventSource = eventSource;
	}
	
	public void addListener(IBEventListener listener){
		if( listener == null ){
			throw new BException("listener is null", null);
		}
		_list.add(listener);
		_listAsArray = null;
	}
	
	public void removeListener(IBEventListener listener){
		_list.remove(listener);
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
		return _eventSource.transform();
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
			IBEvent newEvent = new IBEvent( e.type(), p, e.rectangle() );
			boolean result = l.handle(newEvent);
			if( result ){
				return true;
			}
		}
		return false;
	}
}