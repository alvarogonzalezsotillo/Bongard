package ollitos.gui.basic;

import ollitos.geom.BRectangle;
import ollitos.geom.IBRectangle;
import ollitos.platform.IBCanvas;
import ollitos.platform.IBRaster;
import ollitos.platform.raster.IBRasterProvider;
import ollitos.util.BException;


public class BSprite extends BRectangularDrawable{

	private IBRasterProvider _rasterProvider;
	private IBRaster _raster;
	private IBRectangle _initialPosition;

	
	private static IBRectangle computeOriginalPosition(IBRaster ra){
		return new BRectangle( -ra.w()/2, -ra.h()/2, ra.w(), ra.h() );
	}

	/**
	 * 
	 * @param raster
	 */
	public BSprite(IBRasterProvider rasterProvider){
		this(rasterProvider,null);
	}
	
	
	public BSprite(IBRasterProvider rasterProvider, IBRectangle initialPositon) {
		_rasterProvider = rasterProvider;
		_initialPosition = initialPositon;
		setOriginalSize(_initialPosition);
		loadRaster();
	}


	protected void loadRaster() {
		loadRasterImpl();
	}

	protected void loadRasterImpl() {
		_raster = _rasterProvider.raster();
		IBRectangle r = _initialPosition;
		if( r == null ){
			r = computeOriginalPosition(_raster);
		}
		setOriginalSize( r );
	}
	
	protected IBRaster raster(){
		return _raster;
	}
	
	public IBRasterProvider rasterProvider(){
		return _rasterProvider;
	}

	public void setAlfa(float alfa) {
		canvasContext().setAlpha( alfa );
	}


	public float alpha() {
		return canvasContext().alpha();
	}
	
	public boolean antialias(){
		return canvasContext().antialias();
	}
	
	public void setAntialias( boolean a ){
		canvasContext().setAntialias(a);
	}
	
	@Override
	protected void draw_internal(IBCanvas c) {
		if( raster().disposed() ){
			new BException("Raster is disposed", null).printStackTrace();
			return;
		}

		IBRaster raster = raster();
		int x = -raster.w()/2;
		int y = -raster.h()/2;
		
		c.drawRaster(this, raster, x, y);
	}
}
