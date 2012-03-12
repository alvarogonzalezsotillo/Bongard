package bongard.gui.container;

import bongard.geom.BRectangle;
import bongard.gui.basic.BRectangularDrawable;
import bongard.gui.event.BListenerList;
import bongard.gui.event.IBEvent;
import bongard.gui.event.IBEventListener;
import bongard.gui.event.IBEventSource;
import bongard.gui.game.BState;
import bongard.util.BException;

public abstract class BDrawableContainer extends BRectangularDrawable implements IBDrawableContainer{

	private MyListenerList _listeners = new MyListenerList(this);
	
	
	protected BDrawableContainer(){
		super( new BRectangle(0, 0, 0, 0) );
	}
	
	class MyListenerList extends BListenerList{
		public MyListenerList(IBEventSource container) {
			super(container);
		}

		@Override
		public boolean handle(IBEvent e){
			return BDrawableContainer.this.handleEvent(e);
		}
		
		public boolean handleOfTheListenerList(IBEvent e){
			return super.handle(e);
		}
	};
	
	@Override
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
	protected boolean handleEvent(IBEvent e) {
		return _listeners.handleOfTheListenerList(e);
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
