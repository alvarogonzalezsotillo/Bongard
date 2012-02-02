package purethought.gui.container;

import purethought.geom.BRectangle;
import purethought.geom.IBPoint;
import purethought.geom.IBTransform;
import purethought.gui.basic.BRectangularDrawable;
import purethought.gui.event.BListenerList;
import purethought.gui.event.IBEvent;
import purethought.gui.event.IBEventListener;
import purethought.gui.event.IBEventSource;
import purethought.platform.BFactory;

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
}
