package ollitos.platform.raster;

import ollitos.platform.IBRaster;

public abstract class BRasterProviderFromRaster extends BRasterProvider{
	private IBRasterProvider _provider;

	protected BRasterProviderFromRaster( IBRasterProvider provider ){
		_provider = provider;
	}
	
	protected IBRasterProvider provider(){
		return _provider;
	}
	
	@Override
	protected final IBRaster createRaster(){
		IBRaster original = provider().raster();
		if( original == null ){
			return null;
		}
		return createRaster(original);
	}

	abstract protected IBRaster createRaster(IBRaster original);
}
