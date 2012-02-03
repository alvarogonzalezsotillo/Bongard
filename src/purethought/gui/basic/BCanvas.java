package purethought.gui.basic;


import purethought.geom.IBRectangle;
import purethought.geom.IBTransform;
import purethought.gui.container.IBDrawableContainer;
import purethought.gui.event.BListenerList;
import purethought.gui.event.IBEventListener;
import purethought.platform.BFactory;

public abstract class BCanvas implements IBCanvas{
	
	private IBTransform _t = BFactory.instance().identityTransform();
	private IBDrawableContainer _d;
	private BListenerList _listeners = new BListenerList(this);

	public IBTransform transform() {
		return _t;
	}

	public void setTransform(IBTransform t) {
		_t = t;
	}

	@Override
	public void setDrawable(IBDrawableContainer d) {
		if( _d != null ){
			removeListener(_d.listener());
		}
		_d = d;
		if( _d != null ){
			addListener(_d.listener());
			adjustTransformToSize();
		}
	}

	@Override
	public IBDrawableContainer drawable() {
		return _d;
	}
	
	@Override
	public void addListener(IBEventListener l) {
		if( l != null ){
			_listeners.addListener(l);
		}
	}
	
	@Override
	public void removeListener(IBEventListener l) {
		if( l != null ){
			_listeners.removeListener(l);
		}
	}
	
	protected IBEventListener listeners(){
		return _listeners;
	}

	
	public void adjustTransformToSize(){
		if( drawable() == null ){
			return;
		}
		IBRectangle origin = drawable().originalSize();
		IBRectangle destination = originalSize();
		
		transform().setTo(origin, destination, true, true);
	}

	public IBColor backgroundColor() {
		return BFactory.COLOR_DARKGRAY;
	}


}
