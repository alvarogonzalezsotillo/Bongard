package ollitos.platform;

import java.io.IOException;
import java.io.InputStream;

import ollitos.animation.BFixedDurationAnimation;
import ollitos.geom.BRectangle;
import ollitos.geom.IBRectangle;
import ollitos.gui.basic.BBox;

public interface IBRasterUtil {
	IBRaster extract(IBRectangle r, IBRaster i);
	IBRaster raster(InputStream is, boolean transparent) throws IOException;
	IBRaster raster( IBRectangle r );
	IBRaster html( IBRectangle r, BResourceLocator rl ) throws IOException;
	
	public static class BProgressAnimation extends BFixedDurationAnimation{

		
		private IBRaster _r;
		private BBox _b;

		public BProgressAnimation(IBRaster r) {
			super(7*1000);
			_r = r;
			_b = new BBox( new BRectangle( 10, 10, 0, 5 ), BPlatform.instance().color("ffff00") );
		}

		@Override
		public void stepAnimation(long millis) {
			stepMillis(millis);
			_b.setOriginalSize( new BRectangle(10, 10, currentMillis()*(_r.w()-10*2)/totalMillis(), 5) );
			_r.canvas().drawBox(_b, _b.originalSize(), true );
		}
	}
}
