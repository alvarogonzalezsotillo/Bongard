package ollitos.gui.basic;

import ollitos.geom.IBPoint;
import ollitos.geom.IBRectangle;
import ollitos.geom.IBTransform;
import ollitos.platform.BCanvasContextDelegate;
import ollitos.platform.BPlatform;
import ollitos.platform.IBCanvas;
import ollitos.platform.IBRaster;
import ollitos.util.BTransformUtil;

public class BNonZoomableSprite extends BRectangularDrawable{
	
	private static final boolean USETEMPORARY = false;
	private IBRasterProvider _rasterProvider;
	private int _oldW;
	private int _oldH;
	private IBRaster _raster;
	private BCanvasContextDelegate _ccd = new BCanvasContextDelegate( canvasContext() );

	public BNonZoomableSprite(IBRasterProvider rp ){
		_rasterProvider = rp;
	}

	private IBRaster computeRaster(){
		BPlatform platform = BPlatform.instance();
		IBRectangle originalSize = originalSize();
		IBPoint topleft = platform.point(originalSize.x(),originalSize.y());
		IBPoint toprigth = platform.point(originalSize.x(),originalSize.y()+originalSize.w());
		IBPoint bottomleft = platform.point(originalSize.x()+originalSize.h(),originalSize.y());

		IBTransform t = USETEMPORARY ? transformWithTemporary() : transform();
		topleft = t.transform(topleft);
		toprigth = t.transform(toprigth);
		bottomleft = t.transform(bottomleft);
		
		int w = (int) BTransformUtil.distance(topleft, toprigth);
		int h = (int) BTransformUtil.distance(topleft, bottomleft);
		
		if( w == _oldW && h == _oldH && _raster != null ){
			return _raster;
		}
		
		IBRaster ret = _rasterProvider.raster(w, h);
		_oldW = w;
		_oldH = h;
		return ret;
	}
	
	@Override
	protected void draw_internal(IBCanvas c) {
		IBRaster r = computeRaster();
		
		int w = r.w();
		int h = r.h();
		
		IBTransform adjustRaster = BPlatform.instance().identityTransform();
		adjustRaster.scale(originalSize().w()/w, originalSize().h()/h);
		
		IBTransform t = canvasContext().transform();
		adjustRaster.concatenate(t);
		
		_ccd.setTransform( adjustRaster );
		
		c.drawRaster(_ccd, r, 0, 0);
	}

}

