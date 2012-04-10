package ollitos.gui.basic;

import ollitos.geom.BRectangle;
import ollitos.geom.IBRectangle;
import ollitos.geom.IBTransform;
import ollitos.util.BException;


public abstract class BSprite extends BRectangularDrawable{

	private IBRaster _raster;
	private double _alpha = 1;
	private boolean _antialias = false;
	
	private static IBRectangle computeOriginalPosition(IBRaster ra){
		return new BRectangle( -ra.w()/2, -ra.h()/2, ra.w(), ra.h() );
	}
	
	/**
	 * 
	 * @param raster
	 */
	public BSprite(IBRaster raster){
		super( computeOriginalPosition(raster) );
		_raster = raster;
		setAlfa(1);
	}
	
	
	public IBRaster raster(){
		return _raster;
	}
	

	public void setAlfa(double _alfa) {
		this._alpha = _alfa;
	}


	public double alpha() {
		return _alpha;
	}
	
	public boolean antialias(){
		return _antialias;
	}
	
	public void setAntialias( boolean a ){
		_antialias = a;
	}
	
	@Override
	public void draw(IBCanvas c, IBTransform aditionalTransform) {
		if( raster().disposed() ){
			throw new BException("Raster is disposed", null);
		}
		super.draw(c, aditionalTransform);
	}
}
