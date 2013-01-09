package ollitos.platform.raster;

import java.io.IOException;

import ollitos.geom.IBRectangle;
import ollitos.platform.BPlatform;
import ollitos.platform.BResourceLocator;
import ollitos.platform.IBRaster;
import ollitos.util.BException;

class BRasterProviderFromHTML extends BRasterProvider{

	private IBRectangle _r;
	private BResourceLocator _rl;
	
	public BRasterProviderFromHTML( IBRectangle r, BResourceLocator rl ){
		_r = r;
		_rl = rl;
	}
	
	@Override
	public int w() {
		return (int) _r.w();
	}

	@Override
	public int h() {
		return (int) _r.h();
	}

	@Override
	public String key() {
		return getClass().getSimpleName() + "-" + _rl.toString();
	}

	@Override
	protected IBRaster createRaster() {
		BPlatform instance = BPlatform.instance();
		try{
			return instance.rasterUtil().html(_r, _rl);
		}
		catch( IOException e){
			throw new BException("can't create html", e);
		}
	}

}
