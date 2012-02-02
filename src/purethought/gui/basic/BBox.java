package purethought.gui.basic;

import purethought.geom.IBRectangle;

public abstract class BBox extends BRectangularDrawable{

	
	private boolean _filled = true;
	private IBColor _color;

	public BBox( IBRectangle r, IBColor color ){
		super(r);
		_color = color;
	}
	
	public IBColor color(){
		return _color;
	}
	
	public boolean filled(){
		return _filled;
	}
	
	public void setFilled(boolean f){
		_filled = f;
	}
	
	
	
}
