package purethought.gui;

import java.util.ArrayList;

import purethought.geom.IBPoint;
import purethought.geom.IBTransform;
import purethought.util.BFactory;

public abstract class BCanvas implements IBCanvas{
	
	@SuppressWarnings("serial")
	public class ListenerList extends ArrayList<IBCanvasListener> implements IBCanvasListener{

		@Override
		public void pointerClick(IBPoint p) {
			for (IBCanvasListener i : this) i.pointerClick(p);
		}

		@Override
		public void pointerDown(IBPoint p) {
			for (IBCanvasListener i : this) i.pointerDown(p);
		}

		@Override
		public void pointerDrag(IBPoint p) {
			for (IBCanvasListener i : this) i.pointerDrag(p);
		}

		@Override
		public void pointerUp(IBPoint p) {
			for (IBCanvasListener i : this) i.pointerUp(p);
		}

		@Override
		public void zoomIn(IBPoint p) {
			for (IBCanvasListener i : this) i.zoomIn(p);
		}

		@Override
		public void zoomOut(IBPoint p) {
			for (IBCanvasListener i : this) i.zoomOut(p);
		}
		
	}

	private IBTransform _t = BFactory.instance().identityTransform();
	private IBCanvasDrawable _d;
	private ListenerList _listeners = new ListenerList();

	public IBTransform transform() {
		return _t;
	}

	public void setTransform(IBTransform t) {
		_t = t;
	}

	@Override
	public void setDrawable(IBCanvasDrawable d) {
		if( _d != null ){
			_d.addedTo(null);
		}
		_d = d;
		if( _d != null ){
			_d.addedTo(this);
		}
	}

	@Override
	public IBCanvasDrawable drawable() {
		return _d;
	}
	
	@Override
	public void addListener(IBCanvasListener l) {
		_listeners.add(l);
	}
	
	@Override
	public void removeListener(IBCanvasListener l) {
		_listeners.remove(l);
	}
	
	protected IBCanvasListener listeners(){
		return _listeners;
	}
	
}
