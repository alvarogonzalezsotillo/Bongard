package ollitos.platform.awt;

import ollitos.geom.IBTransform;
import ollitos.gui.basic.BSprite;
import ollitos.platform.IBCanvas;
import ollitos.platform.IBRaster;



public class AWTSprite extends BSprite{
	
	public AWTSprite(IBRaster raster) {
		super(raster);
	}

	@Override
	protected void draw_internal(IBCanvas c, IBTransform t) {
		
		IBRaster raster = raster();
		int x = -raster.w()/2;
		int y = -raster.h()/2;
		
		
		c.drawRaster(this, raster, x, y);
	}
}
