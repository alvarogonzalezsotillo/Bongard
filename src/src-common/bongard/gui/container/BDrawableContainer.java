package bongard.gui.container;

import ollitos.geom.IBTransformHolder;
import ollitos.gui.basic.BRectangularDrawable;
import ollitos.gui.event.BListenerList;
import ollitos.gui.event.IBEvent;
import ollitos.gui.event.IBEventListener;
import ollitos.util.BException;
import bongard.gui.game.BGameField;
import bongard.gui.game.BState;

public abstract class BDrawableContainer extends BRectangularDrawable implements IBDrawableContainer{

	private MyListenerList _listeners = new MyListenerList(this);
	
	
	protected BDrawableContainer(){
		super( BGameField.computeOriginalSize() );
	}
	
	class MyListenerList extends BListenerList{
		public MyListenerList(IBTransformHolder container) {
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
