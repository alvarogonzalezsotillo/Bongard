package ollitos.gui.container;

import java.util.ArrayList;
import java.util.List;

import ollitos.geom.IBRectangle;
import ollitos.geom.IBTransform;
import ollitos.gui.basic.BRectangularDrawable;
import ollitos.gui.basic.IBDrawable;
import ollitos.gui.event.BListenerList;
import ollitos.gui.event.IBEvent;
import ollitos.gui.event.IBEventConsumer;
import ollitos.gui.event.IBEventListener;
import ollitos.platform.IBCanvas;
import ollitos.platform.IBDisposable;
import ollitos.platform.state.BState;
import ollitos.util.BException;

public abstract class BDrawableContainer extends BRectangularDrawable implements IBDrawableContainer, BState.Stateful{

	private transient BListenerList _listeners;
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
		
		if( d instanceof IBEventConsumer ){
			addEventConsumer((IBEventConsumer) d);
		}
	}
	
	public void removeDrawable(IBDrawable d){
		if( d == null ){
			return;
		}
		_drawables.remove(d);
		if( d instanceof IBDisposable ){
			IBDisposable disp = (IBDisposable) d;
			if( !disp.disposed() ){
				disp.dispose();
			}
		}
		
		if( d instanceof IBEventConsumer ){
			removeEventConsumer((IBEventConsumer) d);
		}
		
	}
	
	public void removeDrawables() {
		for( IBDrawable d : drawables() ){
			removeDrawable(d);
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
			listener().addListener(l);
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
			listener().removeListener(l);
		}
	}
	
	@Override
	public BListenerList listener() {
		if( _listeners == null ){
			_listeners = createListener();
		}
		return _listeners;
	}
	
	protected BListenerList createListener() {
		return new BListenerList(this);
	}

	@Override
	public BState save() {
		throw new BException("save must be implemented or not called:"+ getClass().getName(), null);
	}
	
	@Override
	protected void draw_internal(IBCanvas c) {
		IBTransform t = canvasContext().transform();
		draw_children(c, t);
	}

	protected void draw_children(IBCanvas c, IBTransform t) {
		for( IBDrawable d: _drawables ){
			d.draw(c, t);
		}
	}
	
	public IBDrawable[] drawables(){
		return _drawables.toArray(new IBDrawable[0] );
	}
}
