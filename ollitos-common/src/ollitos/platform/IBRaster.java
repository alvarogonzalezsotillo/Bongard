package ollitos.platform;

import ollitos.platform.raster.IBRasterProvider;



public interface IBRaster extends IBDisposable, IBRasterProvider{
	int w();
	int h();
	IBCanvas canvas();
}