package ollitos.platform;

import java.io.IOException;
import java.io.InputStream;

import ollitos.geom.IBRectangle;

public interface IBRasterUtil {
	IBRaster extract(IBRectangle r, IBRaster i);
	IBRaster raster(InputStream is, boolean transparent) throws IOException;
	IBRaster raster( IBRectangle r );
	IBRaster html( IBRectangle r, BResourceLocator rl ) throws IOException;
}
