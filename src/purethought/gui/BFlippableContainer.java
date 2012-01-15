package purethought.gui;

import purethought.geom.BRectangle;
import purethought.geom.IBPoint;
import purethought.geom.IBRectangle;
import purethought.geom.IBTransform;
import purethought.util.BFactory;

public class BFlippableContainer extends BTopDrawable {
	private IBFlippableDrawable[][] _drawables;
	private int _x;
	private int _y;
	
	private BCanvas.ListenerList _publicListener;
	
	public IBCanvasListener listener(){
		if (_publicListener == null) {
			_publicListener = new BCanvas.ListenerList();
			_publicListener.add(_listener);
		}

		return _publicListener;
	}
	
	private IBCanvasListener _listener = new IBCanvasListener() {
		
		@Override
		public void zoomOut(IBPoint p) {
		}
		
		@Override
		public void zoomIn(IBPoint p) {
		}
		
		@Override
		public void resized() {
			adjustTransformToSize();
		}
		
		@Override
		public void pointerUp(IBPoint p) {
		}
		
		@Override
		public void pointerDrag(IBPoint p) {
		}
		
		@Override
		public void pointerDown(IBPoint p) {
		}
		
		@Override
		public void pointerClick(IBPoint p) {
		}
	};

	public BFlippableContainer(int x, int y, IBFlippableDrawable[][] drawables) {
		_drawables = drawables;
		setCurrent(x, y);
	}
	

	public void flipDown(){
		int y = (_y+1)%_drawables[_x].length;
		setCurrent(_x,y);
	}
	
	public void flipUp(){
		int y = (_y-1)%_drawables[_x].length;
		setCurrent(_x,y);
	}
	

	private void setCurrent(int x, int y) {
		IBFlippableDrawable current = current();

		if (current != null) {
			removeListener( current.listener() );
			current.setFlippableContainer(null);
		}

		_x = x;
		_y = y;

		current = current();
		if (current != null) {
			addListener( current.listener() );
			current.setFlippableContainer(this);
		}
		
		adjustTransformToSize();
	}

	public IBFlippableDrawable[][] drawables() {
		return _drawables.clone();
	}

	public IBFlippableDrawable current() {
		return drawables()[_x][_y];
	}

	@Override
	protected void draw_internal(IBCanvas c, IBTransform t) {
		current().draw(c, t);
	}
	
	public void adjustTransformToSize(){
		if( current() == null ){
			return;
		}
		IBRectangle origin = current().originalSize();
		IBRectangle destination = originalSize();
		transform().setTo(origin, destination);
	}


	@Override
	public IBRectangle originalSize() {
		return new BRectangle(0, 0, 240, 320);
	}

}
