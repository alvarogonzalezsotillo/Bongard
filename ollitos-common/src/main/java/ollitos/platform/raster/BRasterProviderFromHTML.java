package ollitos.platform.raster;

import java.io.IOException;

import ollitos.geom.IBRectangle;
import ollitos.platform.BPlatform;
import ollitos.platform.BResourceLocator;
import ollitos.platform.IBRaster;
import ollitos.util.BException;

class BRasterProviderFromHTML extends BRasterProvider{

    private String _str;
    private IBRectangle _r;
	private BResourceLocator _rl;
	
	public BRasterProviderFromHTML( IBRectangle r, BResourceLocator rl ){
		_r = r;
		_rl = rl;
	}

    public BRasterProviderFromHTML( IBRectangle r, String str ){
        _r = r;
        _str = str;
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
		return getClass().getSimpleName() + "-" + String.valueOf(_rl) + "-" + String.valueOf(_str).hashCode();
	}

	@Override
	protected IBRaster createRaster() {
		BPlatform instance = BPlatform.instance();

        if( _rl != null ){
            return instance.rasterUtil().html(_r, _rl);
        }
        else{
            return instance.rasterUtil().html(_r, _str);
        }
	}

}
