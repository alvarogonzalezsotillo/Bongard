package purethought.gui.basic;

import purethought.geom.IBRectangle;

public abstract class BBox extends BRectangularDrawable{

	
	private boolean _filled = true;

	public BBox( IBRectangle r ){
		super(r);
	}
	
	public boolean filled(){
		return _filled;
	}
	
	public void setFilled(boolean f){
		_filled = f;
	}
	
	
}
