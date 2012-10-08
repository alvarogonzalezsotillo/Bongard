package ollitos.platform.raster;

import java.io.IOException;
import java.io.InputStream;

import ollitos.platform.BPlatform;
import ollitos.platform.BResourceLocator;
import ollitos.platform.IBRaster;

public class BRasterProviderFromResource extends BRasterProvider{
	
	private BResourceLocator _resource;

	public BRasterProviderFromResource(BResourceLocator l){
		_resource = l;
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
			return null;
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
