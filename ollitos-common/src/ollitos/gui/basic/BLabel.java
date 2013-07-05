package ollitos.gui.basic;

import ollitos.geom.BRectangle;
import ollitos.geom.IBPoint;
import ollitos.geom.IBRectangle;
import ollitos.geom.IBTransform;
import ollitos.platform.BPlatform;
import ollitos.platform.IBCanvas;
import ollitos.platform.IBColor;
import ollitos.platform.IBRaster;

public class BLabel extends BDrawable{
	
	private String _text;
    private IBRectangle _originalSize;

    public BLabel(String text){
		setText(text);
		setColor(BPlatform.COLOR_WHITE);
	}
	
	public IBColor color(){
		return canvasContext().color();
	}
	
	public void setColor(IBColor c){
		canvasContext().setColor(c);
	}

	@Override
	public boolean inside(IBPoint p, IBTransform aditionalTransform) {
		return false;
	}

	public String text() {
		return _text;
	}

	public void setText(String text){
        IBRaster tmp = platform().rasterUtil().raster(new BRectangle(0, 0, 10, 10));
        IBCanvas c = tmp.canvas();
        IBRectangle r = c.stringBounds(this, text, null);
        tmp.dispose();
        _originalSize = new BRectangle(0,-r.h(),r.w(),r.h());

        _text = text;
	}

	@Override
	protected void draw_internal(IBCanvas c) {
		c.drawString(this, text(), 0, 0);
	}

	@Override
	public IBRectangle originalSize(){
        if( _originalSize == null ){
            throw new IllegalArgumentException();
        }
		return _originalSize;
	}

}
