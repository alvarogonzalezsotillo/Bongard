package ollitos.platform.raster;

import ollitos.geom.IBRectangle;
import ollitos.platform.BPlatform;
import ollitos.platform.IBRaster;

public class BRasterProviderFromRectangle extends BRasterProviderFromRaster{

	private IBRectangle _rectangle;


	public BRasterProviderFromRectangle(IBRasterProvider provider, IBRectangle r) {
		super(provider);
		_rectangle = r;
	}

	@Override
	protected IBRaster createRaster(IBRaster original) {
		return BPlatform.instance().rasterUtil().extract(_rectangle, original);
	}

	@Override
	public String key() {
		return "rectangle: " + _rectangle + " -- " + provider().key();
	}

}
