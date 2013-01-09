package ollitos.platform.raster;

import ollitos.geom.IBRectangle;
import ollitos.platform.BPlatform;
import ollitos.platform.IBColor;
import ollitos.platform.IBRaster;
import ollitos.util.BException;

class BRasterProviderFromRectangle extends BRasterProviderFromRaster{

	private IBRectangle _rectangle;
	private IBColor _color;

	public BRasterProviderFromRectangle(IBRasterProvider provider, IBRectangle r, IBColor color, boolean disposeOriginal) {
		super(provider, disposeOriginal);
		_rectangle = r;
		_color = color;
	}
	
	@Override
	public int w(){
		return (int) _rectangle.w();
	}

	@Override
	public int h(){
		return (int) _rectangle.h();
	}
	

	@Override
	protected IBRaster createRasterFrom(IBRaster raster) {
		if( raster == null ){
			return null;
		}
		try{
			return BPlatform.instance().rasterUtil().extract(_rectangle, raster, _color );
		}
		finally{
			if( disposeOriginal() ){
				provider().dispose();
			}
		}
	}

	@Override
	public String key() {
		return "rectangle: " + _rectangle + " -- " + provider().key();
	}
	
	@Override
	public String toString() {
		return getClass().getName() + key();
	}

}
