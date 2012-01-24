package purethought.gui.basic;

import purethought.geom.BRectangle;
import purethought.geom.IBPoint;
import purethought.geom.IBRectangle;
import purethought.geom.IBTransform;
import purethought.util.BFactory;


public abstract class BSprite extends BRectangularDrawable{

	private IBRaster _raster;
	private double _alpha = 1;
	
	private static IBRectangle computeOriginalPosition(IBRaster ra){
		IBRectangle re = ra.originalSize();
		return new BRectangle( -re.w()/2, -re.h()/2, re.w(), re.h() );
	}
	
	/**
	 * 
	 * @param raster
	 */
	public BSprite(IBRaster raster){
		super( computeOriginalPosition(raster) );
		_raster = raster;
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
	
}
