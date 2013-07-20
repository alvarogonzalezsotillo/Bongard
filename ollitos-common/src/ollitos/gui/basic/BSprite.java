package ollitos.gui.basic;

import ollitos.geom.BRectangle;
import ollitos.geom.IBRectangle;
import ollitos.platform.BPlatform;
import ollitos.platform.IBCanvas;
import ollitos.platform.IBColor;
import ollitos.platform.IBRaster;
import ollitos.platform.raster.IBRasterProvider;
import ollitos.util.BException;


public class BSprite extends BRectangularDrawable{

	private IBRasterProvider _rasterProvider;
	private IBRaster _raster;
	private IBRectangle _initialPosition;
    private IBColor _notAvailableBorderColor = BPlatform.COLOR_BLACK;
    private IBColor _notAvailableColor = BPlatform.COLOR_WHITE;

    public void setNotAvailableColor( IBColor color ){
        _notAvailableColor = color;
    }

    public void setNotAvailableBorderColor( IBColor color ){
        _notAvailableBorderColor = color;
    }

    private static IBRectangle computeOriginalPosition(IBRaster ra){
		return new BRectangle( -ra.w()/2, -ra.h()/2, ra.w(), ra.h() );
	}

	/**
	 * @param raster
	 */
	public BSprite(IBRasterProvider rasterProvider){
		this(rasterProvider,null);
	}


    /**
     * @param rasterProvider
     * @param initialPositon
     */
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

	protected void draw_rasterNotAvailable(IBCanvas c){
		IBRectangle os = originalSize();
//		int x = (int)os.x();
//		int y = (int)os.y();
//		int h = (int)os.h();
//		int w = (int)os.w();
//		c.drawLine(this, x, y, x+w, y+h );
//		c.drawLine(this, x, h+y, w+x, y );
        canvasContext().setColor(_notAvailableColor);
        c.drawBox(this, os, true);

        canvasContext().setColor(_notAvailableBorderColor);
        c.drawBox(this,os,false);
    }
	
	@Override
	protected void draw_internal(IBCanvas c) {
		
		if( raster() == null || raster().disposed() ){
			draw_rasterNotAvailable(c);
			return;
		}

		IBRaster raster = raster();
		int x = -raster.w()/2;
		int y = -raster.h()/2;
		
		c.drawRaster(this, raster, x, y);
	}
}
