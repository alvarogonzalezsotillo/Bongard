package purethought.gui;

import purethought.util.BFactory;


public abstract class BSprite extends BDrawable{

	private IBRaster _raster;
	private IBPoint[] _originalHull;
	
	
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
	
	@Override
	public IBPoint[] originalHull() {
		if (_originalHull == null) {
			IBRaster raster = raster();
			double w = raster.originalSize().w();
			double h = raster.originalSize().h();
			BFactory f = BFactory.instance();
			
			_originalHull = new IBPoint[4];
			
			_originalHull[0] = f.point(-w/2, -h/2);
			_originalHull[1] = f.point(w/2, -h/2);
			_originalHull[2] = f.point(w/2, h/2);
			_originalHull[3] = f.point(-w/2, h/2);
		}
		return _originalHull;
	}
	
	
}
