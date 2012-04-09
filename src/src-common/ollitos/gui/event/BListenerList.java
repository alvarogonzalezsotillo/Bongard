/**
 * 
 */
package ollitos.gui.event;

import java.util.ArrayList;

import ollitos.geom.IBPoint;
import ollitos.util.BException;



public class BListenerList implements IBEventListener{
	
	ArrayList<IBEventListener> _list = new ArrayList<IBEventListener>();

	IBEventListener[] _listAsArray;

	private IBEventSource _container;
	
	public BListenerList(IBEventSource container){
		_container = container;
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
		if( l.source() == null ){
			return p;
		}
		return l.source().transform().inverse().transform(p);
	}
	


	private IBEventListener[] listAsArray() {
		if (_listAsArray == null) {
			_listAsArray = _list.toArray(new IBEventListener[0]);
		}

		return _listAsArray;
	}

	@Override
	public IBEventSource source() {
		return _container;
	}

	@Override
	public boolean handle(IBEvent e) {
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