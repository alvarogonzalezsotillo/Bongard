package ollitos.platform.andr;

import java.io.IOException;
import java.io.InputStream;

import ollitos.geom.IBRectangle;
import ollitos.platform.BResourceLocator;
import ollitos.platform.IBRaster;
import ollitos.platform.IBRasterUtil;
import ollitos.util.BException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class AndrRasterUtil implements IBRasterUtil{

	@Override
	public IBRaster extract(IBRectangle r, IBRaster i) {
		Bitmap src = ((AndrRaster)i).bitmap();
		Bitmap b = Bitmap.createBitmap(src, (int)r.x(), (int)r.y(), (int)r.w(), (int)r.h(), null, false );
		return new AndrRaster(b);
	}

	@Override
	public IBRaster raster(InputStream is, boolean transparent)	throws IOException {
		Bitmap b = BitmapFactory.decodeStream(is);
		is.close();
		if( b == null ){
			throw new BException("Cant open image", null);
		}
		if( transparent ){
			return new AndrRaster(b);
		}
		Bitmap.Config config = b.getConfig();
		if( config == null ){
			config = defaultBitmapConfig();
		}
		Bitmap ret = b.copy(config, true);
		Canvas c = new Canvas(ret);
		Paint p = new Paint();
		p.setColor( Color.WHITE );
		c.drawRect(0, 0, ret.getWidth(), ret.getHeight(), p);
		c.drawBitmap(b, 0, 0, p);
		return new AndrRaster(ret);
	}

	private Bitmap.Config defaultBitmapConfig() {
		Bitmap.Config config;
		config = Bitmap.Config.ARGB_8888;
		return config;
	}

	@Override
	public IBRaster raster(IBRectangle r) {
		Bitmap b = Bitmap.createBitmap((int)r.w(), (int)r.h(), defaultBitmapConfig() );
		return new AndrRaster(b);
	}

	@Override
	public IBRaster html(IBRectangle r, BResourceLocator rl) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

}
