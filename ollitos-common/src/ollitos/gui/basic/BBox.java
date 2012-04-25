package ollitos.gui.basic;

import ollitos.geom.IBRectangle;
import ollitos.platform.IBColor;

public abstract class BBox extends BRectangularDrawable{

	
	private boolean _filled = true;

	public BBox( IBRectangle r, IBColor color ){
		super(r);
		setColor(color);
	}

	protected void setColor(IBColor color) {
		canvasContext().color = color;
	}
	
	public IBColor color(){
		return canvasContext().color;
	}
	
	public boolean filled(){
		return _filled;
	}
	
	public void setFilled(boolean f){
		_filled = f;
	}
}
