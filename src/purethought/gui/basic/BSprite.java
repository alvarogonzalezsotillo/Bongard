package purethought.gui.basic;

import purethought.geom.BRectangle;
import purethought.geom.IBPoint;
import purethought.geom.IBRectangle;
import purethought.geom.IBTransform;
import purethought.util.BFactory;


public abstract class BSprite extends BDrawable{

	private IBRaster _raster;
	private IBRectangle _originalPosition;
	private double _alpha = 1;
	
	
	/**
	 * 
	 * @param raster
	 */
	public BSprite(IBRaster raster){
		_raster = raster;
	}
	
	
	public IBRaster raster(){
		return _raster;
	}
	
	public IBRectangle originalSize(){
		if (_originalPosition == null) {
			IBRaster ra = raster();
			IBRectangle re = ra.originalSize();
			_originalPosition = new BRectangle( -re.w()/2, -re.h()/2, re.w(), re.h() );
		}

		return _originalPosition;
	}
	
	@Override
	public boolean inside(IBPoint p, IBTransform aditionalTransform ){
		IBTransform t = transform();
		if( aditionalTransform != null ){
			IBTransform tt = BFactory.instance().identityTransform();
			tt.concatenate(t);
			tt.concatenate(aditionalTransform);
			t = tt;
		}
		
		IBTransform inverseT = t.inverse();
		
		IBPoint inverseP = inverseT.transform(p);		
		
		return BRectangle.inside( originalSize(), inverseP);
	}


	public void setAlfa(double _alfa) {
		this._alpha = _alfa;
	}


	public double alpha() {
		return _alpha;
	}
	
	
	
}
