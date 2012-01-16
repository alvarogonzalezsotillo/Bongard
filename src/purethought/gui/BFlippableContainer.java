package purethought.gui;

import purethought.animation.BAnimator;
import purethought.geom.BRectangle;
import purethought.geom.IBPoint;
import purethought.geom.IBRectangle;
import purethought.geom.IBTransform;
import purethought.gui.event.IBEvent;
import purethought.util.BFactory;

public class BFlippableContainer extends BDrawableContainer {
	private IBFlippableDrawable[][] _drawables;
	private int _x;
	private int _y;
	

	public BFlippableContainer(int x, int y, IBFlippableDrawable[][] drawables) {
		_drawables = drawables;
		setCurrent(x, y);
	}
	

	public void flipDown(){
		System.out.println( "flipDown" );
		int y = Math.min(_y+1,_drawables.length);
		setCurrent(_x,y);
	}
	
	public void flipUp(){
		System.out.println( "flipUp" );
		int y = Math.max(_y-1,0);
		setCurrent(_x,y);
	}
	

	private void setCurrent(int x, int y) {
		System.out.println( "setCurrent:" + x + "," + y );
		System.out.println( " previous:" + current() );
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
		
		System.out.println( " last:" + current() );
		BFactory.instance().game().canvas().refresh();
	}

	public IBFlippableDrawable[][] drawables() {
		return _drawables.clone();
	}

	public IBFlippableDrawable current() {
		return drawables()[_x][_y];
	}

	@Override
	protected void draw_internal(IBCanvas c, IBTransform t) {
		IBFlippableDrawable current = current();
		System.out.println( System.currentTimeMillis() + ":" + current );
		current.draw(c, t);
	}

	@Override
	protected boolean handleEvent(IBEvent e) {
		if( e.type() == IBEvent.Type.containerResized ){
			adjustTransformToSize();
			return true;
		}
		return false;
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
