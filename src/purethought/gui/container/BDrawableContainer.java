package purethought.gui.container;

import purethought.geom.BRectangle;
import purethought.geom.IBPoint;
import purethought.geom.IBTransform;
import purethought.gui.basic.BDrawable;
import purethought.gui.basic.IBCanvas;
import purethought.gui.event.BListenerList;
import purethought.gui.event.IBEvent;
import purethought.gui.event.IBEventListener;
import purethought.gui.event.IBEventSource;
import purethought.util.BFactory;
import purethought.util.BPointerSupport;

public abstract class BDrawableContainer extends BDrawable implements IBDrawableContainer{

	private BPointerSupport _ps = new BPointerSupport();
	
	private MyListenerList _listeners = new MyListenerList(this);
	
	
	protected BDrawableContainer(){
		_listeners.addListener(_ps);
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
	public boolean inside(IBPoint p, IBTransform aditionalTransform ){
		IBTransform t = transform();
		if( aditionalTransform != null ){
			IBTransform tt = BFactory.instance().identityTransform();
			tt.concatenate(t);
			tt.concatenate(aditionalTransform);
			t = tt;
		}
		
		IBTransform inverseT = t.inverse();
		
		IBPoint inverseP = inverseT.transform(p);		
		
		return BRectangle.inside( originalSize(), inverseP);
	}
	
	@Override
	protected void draw_internal(IBCanvas c, IBTransform t) {
		//_ps.draw_impl(c, t);
	}
}
