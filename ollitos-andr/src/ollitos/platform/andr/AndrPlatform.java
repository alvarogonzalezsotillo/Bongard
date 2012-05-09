package ollitos.platform.andr;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import ollitos.geom.IBRectangle;
import ollitos.gui.basic.BHTMLDrawable;
import ollitos.gui.basic.BLabel;
import ollitos.gui.basic.BSprite;
import ollitos.platform.BCanvasContext;
import ollitos.platform.BPlatform;
import ollitos.platform.BResourceLocator;
import ollitos.platform.IBColor;
import ollitos.platform.IBRaster;
import ollitos.platform.IBRasterUtil;
import ollitos.platform.IBCanvas.CanvasContext;
import ollitos.util.BException;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class AndrPlatform extends BPlatform{

	private static Context _context;
	private AndrGame _game;
	private AndrLogger _logger;
	private IBRasterUtil _rasterUtil;

	@Override
	public AndrTransform identityTransform() {
		return new AndrTransform();
	}

	@Override
	public AndrPoint point(double x, double y) {
		return new AndrPoint(x,y);
	}


	@Override
	public AndrGame game() {
		if (_game == null) {
			_game = new AndrGame();
		}

		return _game;
	}

	@Override
	public IBRaster raster(BResourceLocator test, boolean transparent) {
		InputStream is = open(test);
		Bitmap b = BitmapFactory.decodeStream(is);
		try {
			is.close();
		}
		catch (IOException e) {
			throw new BException( "cant close:" + test, e);
		}
		if( b == null ){
			throw new BException("Cant open image:" + test, null);
		}
		if( transparent ){
			return new AndrRaster(b);
		}
		Bitmap.Config config = b.getConfig();
		if( config == null ){
			config = Bitmap.Config.ARGB_8888;
		}
		Bitmap ret = b.copy(config, true);
		Canvas c = new Canvas(ret);
		Paint p = new Paint();
		p.setColor( Color.WHITE );
		c.drawRect(0, 0, ret.getWidth(), ret.getHeight(), p);
		c.drawBitmap(b, 0, 0, p);
		return new AndrRaster(ret);
	}

	@Override
	public URL platformURL(BResourceLocator r) {
		String assetPrefix = "file:///android_asset";
		try {
			return new URL( assetPrefix + r.string() );
		}
		catch (MalformedURLException e) {
			throw new BException( "bad resource:" + r, e );
		} 
	}

	@Override
	public InputStream open(BResourceLocator l) {
		InputStream ret = null;
		if( l.string() != null ){
			try {
				String s = l.string().substring(1);
				ret = AndrPlatform.context().getAssets().open(s);
				return ret;
			}
			catch (IOException e) {
				throw new BException("Unable to open:" + l.string(), e);
			}
		}
		return super.open(l);
	}

	
	@Override
	public IBColor color(String c) {
		return new AndrColor( Color.parseColor("#" + c ) );
	}

	public static Context context() {
		return _context;
	}

	public static void initContext(Context context) {
		_context = context;
	}

	@Override
	public AndrLogger logger() {
		if (_logger == null) {
			_logger = new AndrLogger();
		}
		return _logger;
	}

	@Override
	public BHTMLDrawable html() {
		BHTMLDrawable ret = new AndrHTMLDrawable();
		return ret;
	}

	@Override
	public IBRasterUtil rasterUtil() {
		if( _rasterUtil == null ){
			_rasterUtil = new AndrRasterUtil();
		}
		return _rasterUtil;
	}
	
	@Override
	protected BCanvasContext createCanvasContext() {
		return new AndrCanvasContext();
	}

}
