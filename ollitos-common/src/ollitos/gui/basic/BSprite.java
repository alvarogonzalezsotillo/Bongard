package ollitos.gui.basic;

import ollitos.geom.BRectangle;
import ollitos.geom.IBRectangle;
import ollitos.geom.IBTransform;
import ollitos.platform.IBCanvas;
import ollitos.platform.IBRaster;
import ollitos.util.BException;


public abstract class BSprite extends BRectangularDrawable{

	private IBRaster _raster;

	
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
	

	public void setAlfa(float alfa) {
		canvasContext().alpha = alfa;
	}


	public float alpha() {
		return canvasContext().alpha;
	}
	
	public boolean antialias(){
		return canvasContext().antialias;
	}
	
	public void setAntialias( boolean a ){
		canvasContext().antialias = a;
	}
	
	@Override
	public void draw(IBCanvas c, IBTransform aditionalTransform) {
		if( raster().disposed() ){
			new BException("Raster is disposed", null).printStackTrace();
			return;
		}
		
		super.draw(c, aditionalTransform);
	}
}
