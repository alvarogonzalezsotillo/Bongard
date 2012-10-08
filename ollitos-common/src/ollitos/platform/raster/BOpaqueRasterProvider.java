package ollitos.platform.raster;

import ollitos.geom.BRectangle;
import ollitos.platform.BCanvasContext;
import ollitos.platform.BPlatform;
import ollitos.platform.IBCanvas;
import ollitos.platform.IBColor;
import ollitos.platform.IBRaster;

public class BOpaqueRasterProvider extends BRasterProviderFromRaster{

	private IBColor _background;

	public BOpaqueRasterProvider(IBRasterProvider provider, IBColor background) {
		super(provider);
		_background = background;
	}

	@Override
	protected IBRaster createRaster(IBRaster original) {
		BRectangle r = new BRectangle(0, 0, original.w(), original.h() );
		IBRaster ret = BPlatform.instance().rasterUtil().raster(r);
		IBCanvas canvas = ret.canvas();
		BCanvasContext cc = (BCanvasContext) BPlatform.instance().canvasContext();
		cc.setColor(_background);
		canvas.drawBox(cc, r, true);
		canvas.drawRaster(cc, original, 0, 0);
		return ret;
	}

	@Override
	public String key() {
		return "opaque: " + _background + " -- " + provider().key();
	}

}
