package ollitos.gui.container;

import ollitos.gui.basic.BRectangularDrawable;
import ollitos.gui.event.BListenerList;
import ollitos.gui.event.IBEvent;
import ollitos.gui.event.IBEventConsumer;
import ollitos.gui.event.IBEventListener;
import ollitos.platform.BState;
import ollitos.util.BException;

public abstract class BDrawableContainer extends BRectangularDrawable implements IBDrawableContainer{

	private BListenerList _listeners = new BListenerList(this);

	@Override
	public void addEventConsumer(IBEventConsumer c) {
		if( c != null ){
			addListener( c.listener() );
		}
	}
	
	@Override
	public void removeEventConsumer(IBEventConsumer c) {
		if( c != null ){
			removeListener( c.listener() );
		}
	}
	
	public void addListener(IBEventListener l) {
		if( l != null ){
			_listeners.addListener(l);
		}
	}

	/**
	 * 
	 * @param e
	 * @return
	 */
	@Override
	public boolean preHandleEvent(IBEvent e) {
		return false;
	}

	@Override
	public boolean postHandleEvent(IBEvent e) {
		return false;
	}

	
	@Override
	public void removeListener(IBEventListener l) {
		if( l != null ){
			_listeners.removeListener(l);
		}
	}
	
	@Override
	public BListenerList listener() {
		return _listeners;
	}
	
	@Override
	public BState save() {
		throw new BException("save must be implemented or not called:"+ getClass().getName(), null);
	}

}
