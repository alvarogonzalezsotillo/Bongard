package ollitos.platform.raster;

import java.io.IOException;
import java.io.InputStream;

import ollitos.platform.BPlatform;
import ollitos.platform.BResourceLocator;
import ollitos.platform.IBRaster;
import ollitos.util.BException;

class BRasterProviderFromResource extends BRasterProvider{
	
	private BResourceLocator _resource;
	private int _w;
	private int _h;

	public BRasterProviderFromResource(BResourceLocator l, int w, int h ){
		_resource = l;
		_w = w;
		_h = h;
	}
	
	@Override
	public int w(){
		if( !disposed() ){
			return raster().w();
		}
		return _w;
	}
	
	@Override
	public int h(){
		if( !disposed() ){
			return raster().h();
		}
		return _h;
	}

	@Override
	protected IBRaster createRaster(){
		IBRaster ret = null;
		try {
			ret = readRaster();
		} 
		catch (IOException e){
			BPlatform.instance().logger().log( e.toString() );
			e.printStackTrace();
		}
		
		return ret;
	}

	private IBRaster readRaster() throws IOException {
		InputStream is = BPlatform.instance().open(_resource);
		if( is == null ){
			return null;
		}
		IBRaster raster = BPlatform.instance().rasterUtil().raster(is);
		is.close();
		return raster;
	}

	@Override
	public String key() {
		return "from resource:" + _resource.toString();
	}
}
