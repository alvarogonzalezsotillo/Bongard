package ollitos.gui.basic;

import ollitos.platform.IBRaster;

public interface IBRasterProvider {
	IBRaster raster();
	IBRaster raster(int w, int h);
}
