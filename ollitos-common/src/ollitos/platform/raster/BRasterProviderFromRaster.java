package ollitos.platform.raster;

import ollitos.platform.IBRaster;


abstract class BRasterProviderFromRaster extends BRasterProvider{
	private IBRasterProvider _provider;
	private boolean _disposeOriginal;

	protected BRasterProviderFromRaster( IBRasterProvider provider, boolean disposeOriginal ){
		_provider = provider;
		_disposeOriginal = disposeOriginal;
	}
	
	public boolean disposeOriginal(){
		return _disposeOriginal;
	}
	
	protected IBRasterProvider provider(){
		return _provider;
	}
	
	@Override
	protected final IBRaster createRaster() {
		IBRaster raster = provider().raster();
		if( raster == null ){
			return null;
		}
		return createRasterFrom(raster);
	}

	protected abstract IBRaster createRasterFrom(IBRaster raster);
}
