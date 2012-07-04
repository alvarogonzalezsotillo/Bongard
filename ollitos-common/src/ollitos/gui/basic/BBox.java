package ollitos.gui.basic;

import ollitos.geom.IBRectangle;
import ollitos.platform.IBCanvas;
import ollitos.platform.IBColor;

public class BBox extends BRectangularDrawable{

	
	private boolean _filled = true;

	public BBox( IBRectangle r, IBColor color, boolean filled ){
		super(r);
		setColor(color);
		setFilled(filled);
	}
	
	public BBox( IBRectangle r, IBColor color ){
		this(r,color,true);
	}

	protected void setColor(IBColor color) {
		canvasContext().setColor( color );
	}
	
	public IBColor color(){
		return canvasContext().color();
	}
	
	public boolean filled(){
		return _filled;
	}
	
	public void setFilled(boolean f){
		_filled = f;
	}
	
	@Override
	protected void draw_internal(IBCanvas c) {
		c.drawBox(this, originalSize(), filled() );
	}

}
