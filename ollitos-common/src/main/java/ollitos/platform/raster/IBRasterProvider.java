package ollitos.platform.raster;

import ollitos.platform.IBDisposable;
import ollitos.platform.IBRaster;

public interface IBRasterProvider extends IBDisposable, Comparable<IBRasterProvider>{
	IBRaster raster();
	void updateLastMillis();
	long lastMillis();
	int w();
	int h();
	String key();
	IBRasterProvider getFromCache();
}
