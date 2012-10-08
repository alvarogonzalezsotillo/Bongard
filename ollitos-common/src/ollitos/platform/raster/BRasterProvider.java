package ollitos.platform.raster;

import ollitos.platform.IBRaster;

public abstract class BRasterProvider implements IBRasterProvider{

	private IBRaster _raster;

	
	@Override
	public void setUp() {
		raster();
	}

	@Override
	public void dispose() {
		_raster.dispose();
		_raster = null;
	}

	@Override
	public boolean disposed() {
		return _raster == null || _raster.disposed();
	}

	@Override
	public final IBRaster raster(){
		if( disposed() ){
			_raster = createRaster();
		}
		return _raster;

	}
	
	abstract protected IBRaster createRaster();
}
