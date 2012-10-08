package ollitos.platform.raster;

import ollitos.platform.IBDisposable;
import ollitos.platform.IBRaster;

public interface IBRasterProvider extends IBDisposable{
	IBRaster raster();
	String key();
}
