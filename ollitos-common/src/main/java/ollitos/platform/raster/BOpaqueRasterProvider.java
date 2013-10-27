package ollitos.platform.raster;

import ollitos.geom.BRectangle;
import ollitos.platform.BCanvasContext;
import ollitos.platform.BPlatform;
import ollitos.platform.IBCanvas;
import ollitos.platform.IBColor;
import ollitos.platform.IBRaster;

class BOpaqueRasterProvider extends BRasterProviderFromRectangle{

	private IBColor _background;

	public BOpaqueRasterProvider(IBRasterProvider provider, IBColor background, boolean disposeOriginal ){
		super(provider, new BRectangle(0,0,provider.w(),provider.h()), background, disposeOriginal);
	}
	
	@Override
	public String key() {
		return "opaque: " + _background + " -- " + provider().key();
	}

}
