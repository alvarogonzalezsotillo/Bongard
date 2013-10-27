package ollitos.platform.raster;

import java.io.IOException;
import java.io.InputStream;

import ollitos.animation.BFixedDurationAnimation;
import ollitos.geom.BRectangle;
import ollitos.geom.IBRectangle;
import ollitos.gui.basic.BBox;
import ollitos.platform.BPlatform;
import ollitos.platform.BResourceLocator;
import ollitos.platform.IBColor;
import ollitos.platform.IBRaster;
import ollitos.util.BException;

public interface IBRasterUtil {
	IBRaster extract(IBRectangle r, IBRaster i, IBColor color);
	IBRaster raster( InputStream is ) throws IOException;
	IBRaster raster( IBRectangle r );
	IBRaster html( IBRectangle r, BResourceLocator rl ) throws BException;
    IBRaster html( IBRectangle r, String html ) throws BException;
	
	public static class BProgressAnimation extends BFixedDurationAnimation{


        private static final boolean ENABLED = false;
        private IBRaster _r;
		private BBox _b;

		public BProgressAnimation(IBRaster r) {
			super(7*1000);
			_r = r;
			_b = new BBox( new BRectangle( 10, 10, 0, 5 ), BPlatform.COLOR_YELLOW );
		}

		@Override
		public void stepAnimation(long millis) {
			stepMillis(millis);
			_b.setOriginalSize( new BRectangle(10, 10, currentMillis()*(_r.w()-10*2)/totalMillis(), 5) );
			if( ENABLED ){
                _r.canvas().drawBox(_b, _b.originalSize(), true );
            }
		}
	}
}
