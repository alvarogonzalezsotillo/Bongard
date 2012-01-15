package purethought.gui;

import java.util.ArrayList;

import purethought.geom.IBPoint;
import purethought.geom.IBRectangle;
import purethought.geom.IBTransform;
import purethought.util.BFactory;

public abstract class BCanvas implements IBCanvas{
	
	@SuppressWarnings("serial")
	public static class ListenerList extends ArrayList<IBCanvasListener> implements IBCanvasListener{

		@Override
		public void pointerClick(IBPoint p) {
			for (IBCanvasListener i : this.toArray(new IBCanvasListener[0])) i.pointerClick(p);
		}

		@Override
		public void pointerDown(IBPoint p) {
			for (IBCanvasListener i : this.toArray(new IBCanvasListener[0])) i.pointerDown(p);
		}

		@Override
		public void pointerDrag(IBPoint p) {
			for (IBCanvasListener i : this.toArray(new IBCanvasListener[0])) i.pointerDrag(p);
		}

		@Override
		public void pointerUp(IBPoint p) {
			for (IBCanvasListener i : this.toArray(new IBCanvasListener[0])) i.pointerUp(p);
		}

		@Override
		public void zoomIn(IBPoint p) {
			for (IBCanvasListener i : this.toArray(new IBCanvasListener[0])) i.zoomIn(p);
		}

		@Override
		public void zoomOut(IBPoint p) {
			for (IBCanvasListener i : this.toArray(new IBCanvasListener[0])) i.zoomOut(p);
		}

		@Override
		public void resized() {
			for (IBCanvasListener i : this.toArray(new IBCanvasListener[0])) i.resized();
		}
		
	}

	private IBTransform _t = BFactory.instance().identityTransform();
	private IBTopDrawable _d;
	private ListenerList _listeners = new ListenerList();

	public IBTransform transform() {
		return _t;
	}

	public void setTransform(IBTransform t) {
		_t = t;
	}

	@Override
	public void setDrawable(IBTopDrawable d) {
		if( _d != null ){
			removeListener(_d.listener());
		}
		_d = d;
		if( _d != null ){
			addListener(_d.listener());
		}
	}

	@Override
	public IBTopDrawable drawable() {
		return _d;
	}
	
	@Override
	public void addListener(IBCanvasListener l) {
		if( l != null ){
			_listeners.add(l);
		}
	}
	
	@Override
	public void removeListener(IBCanvasListener l) {
		if( l != null ){
			_listeners.remove(l);
		}
	}
	
	protected IBCanvasListener listeners(){
		return _listeners;
	}

	
	public void adjustTransformToSize(){
		IBRectangle origin = drawable().originalSize();
		IBRectangle destination = originalSize();
		
		transform().setTo(origin, destination);
	}

}
