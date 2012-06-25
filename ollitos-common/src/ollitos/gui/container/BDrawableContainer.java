package ollitos.gui.container;

import java.util.ArrayList;
import java.util.List;

import ollitos.geom.IBRectangle;
import ollitos.gui.basic.BRectangularDrawable;
import ollitos.gui.basic.IBDrawable;
import ollitos.gui.event.BListenerList;
import ollitos.gui.event.IBEvent;
import ollitos.gui.event.IBEventConsumer;
import ollitos.gui.event.IBEventListener;
import ollitos.platform.BState;
import ollitos.platform.IBCanvas;
import ollitos.util.BException;

public abstract class BDrawableContainer extends BRectangularDrawable implements IBDrawableContainer, BState.Stateful{

	private transient BListenerList _listeners = new BListenerList(this);
	private transient List<IBDrawable> _drawables = new ArrayList<IBDrawable>();

	@Override
	public void addEventConsumer(IBEventConsumer c) {
		if( c != null ){
			addListener( c.listener() );
		}
	}
	
	public BDrawableContainer(){
	}
	
	public BDrawableContainer(IBRectangle r){
		super(r);
	}
	
	public void addDrawable(IBDrawable d){
		if( d == null ){
			return;
		}
		if( !_drawables.contains(d) ){
			_drawables.add(d);
		}
	}
	
	public void removeDrawable(IBDrawable d){
		if( d == null ){
			return;
		}
		_drawables.remove(d);
	}
	
	public void removeDrawables() {
		_drawables.clear();
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
	
	@Override
	protected void draw_internal(IBCanvas c) {
		for( IBDrawable d: _drawables ){
			d.draw(c, canvasContext().transform());
		}
	}
}
