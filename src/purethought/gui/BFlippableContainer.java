package purethought.gui;

import purethought.geom.IBRectangle;
import purethought.geom.IBTransform;

public class BFlippableContainer extends BTopDrawable {
	private IBFlippableDrawable[][] _drawables;
	private int _x;
	private int _y;

	public BFlippableContainer(int x, int y, IBFlippableDrawable[][] drawables) {
		_drawables = drawables;
		setCurrent(x, y);
	}
	
	@Override
	public void addedTo(IBCanvas c) {
		if( canvas() != null ){
			current().addedTo(null);
		}
		super.addedTo(c);
		if( canvas() != null ){
			current().addedTo(canvas());
		}
	}
	
	public void flipDown(){
		int y = (_y+1)%_drawables[_x].length;
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

	@Override
	public IBRectangle originalSize() {
		return canvas().originalSize();
	}

}
