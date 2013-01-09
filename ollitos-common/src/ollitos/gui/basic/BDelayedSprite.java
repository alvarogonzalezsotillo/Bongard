package ollitos.gui.basic;

import ollitos.animation.BRefreshAnimation;
import ollitos.geom.BRectangle;
import ollitos.geom.IBRectangle;
import ollitos.platform.IBCanvas;
import ollitos.platform.IBRaster;
import ollitos.platform.raster.IBRasterProvider;

public class BDelayedSprite extends BSprite{
	
	volatile private boolean _loading = false;
	
	final protected Runnable _loader = new Runnable(){
		@Override
		public void run(){
			try{
				_loading = true;
				loadRasterImpl();
				platform().game().animator().addAnimation( new BRefreshAnimation(rasterProvider().key()) );
			}
			finally{
				_loading = false;
			}
		}
	};
	
	
	public BDelayedSprite(IBRasterProvider r ){
		super(r, new BRectangle(-r.w()/2, -r.h()/2, r.w(), r.h()) );
	}
	
	protected BDelayedSprite(IBRasterProvider rasterProvider, IBRectangle r){
		super(rasterProvider,r);
	}
	
	@Override
	protected void loadRaster() {
		if( !_loading ){
			Thread t = new Thread( _loader );
			t.start();
		}
	}

	@Override
	protected IBRaster raster(){
		IBRaster ret = super.raster();
		if( ret == null ){
			loadRaster();
		}
		return ret;
	}
}
