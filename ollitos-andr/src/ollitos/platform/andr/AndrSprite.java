package ollitos.platform.andr;

import ollitos.gui.basic.BSprite;
import ollitos.platform.IBCanvas;
import ollitos.platform.IBRaster;

public class AndrSprite extends BSprite {

	

	public AndrSprite(IBRaster raster) {
		super(raster);
	}
	
	@Override
	protected void draw_internal(IBCanvas c) {
		
		IBRaster raster = raster();
		int x = -raster.w()/2;
		int y = -raster.h()/2;
		
		c.drawRaster(this, raster, x, y);
	}

}
