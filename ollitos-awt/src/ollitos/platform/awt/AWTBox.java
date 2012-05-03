package ollitos.platform.awt;

import ollitos.geom.IBRectangle;
import ollitos.gui.basic.BBox;
import ollitos.platform.IBCanvas;
import ollitos.platform.IBColor;



public class AWTBox extends BBox{

	public AWTBox(IBRectangle r, IBColor c ){
		super(r,c);
	}

	@Override
	protected void draw_internal(IBCanvas c) {
		c.drawBox(this, originalSize(), filled() );
	}

}
