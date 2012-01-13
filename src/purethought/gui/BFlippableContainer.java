package purethought.gui;

import purethought.geom.IBPoint;
import purethought.geom.IBRectangle;
import purethought.geom.IBTransform;

public class BFlippableContainer extends BTopDrawable {
	private IBFlippableDrawable[][] _drawables;
	private int _x;
	private int _y;
	
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
	
	@Override
	public void addedTo(IBCanvas c) {
		if( canvas() != null ){
			canvas().removeListener(_listener);
			current().addedTo(null);
		}
		super.addedTo(c);
		if( canvas() != null ){
			canvas().addListener(_listener);
			current().addedTo(canvas());
		}
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
			current.addedTo(null);
			current.setFlippableContainer(null);
		}

		_x = x;
		_y = y;

		current = current();
		if (current != null) {
			current.addedTo(canvas());
			current.setFlippableContainer(this);
		}
		
		adjustTransformToSize();
		if( canvas() != null ){
			canvas().refresh();
		}
	}

	public IBFlippableDrawable[][] drawables() {
		return _drawables.clone();
	}

	public IBFlippableDrawable current() {
		return drawables()[_x][_y];
	}

	@Override
	protected void draw_internal(IBCanvas c, IBTransform t) {
		current().draw(canvas(), t);
	}
	
	public void adjustTransformToSize(){
		if( current() == null || canvas() == null ){
			return;
		}
		IBRectangle origin = current().originalSize();
		IBRectangle destination = originalSize();
		transform().setTo(origin, destination);
	}


	@Override
	public IBRectangle originalSize() {
		return canvas().originalSize();
	}

}
