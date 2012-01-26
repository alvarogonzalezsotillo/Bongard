package purethought.gui.basic;

import purethought.geom.IBRectangle;

public abstract class BBox extends BRectangularDrawable{

	
	private boolean _filled = true;
	private String _color;

	public BBox( IBRectangle r, String color ){
		super(r);
		_color = color;
	}
	
	public String color(){
		return _color;
	}
	
	public boolean filled(){
		return _filled;
	}
	
	public void setFilled(boolean f){
		_filled = f;
	}
	
	
}
