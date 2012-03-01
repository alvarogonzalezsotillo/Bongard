package bongard.platform.andr;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import bongard.geom.IBRectangle;
import bongard.gui.basic.BLabel;
import bongard.gui.basic.BSprite;
import bongard.gui.basic.IBColor;
import bongard.gui.basic.IBRaster;
import bongard.platform.BFactory;
import bongard.platform.BResourceLocator;
import bongard.util.BException;

public class AndrFactory extends BFactory{

	private static Context _context;
	private AndrGame _game;
	private AndrLogger _logger;
	private AndrCardExtractor _extractor;

	@Override
	public AndrTransform identityTransform() {
		return new AndrTransform();
	}

	@Override
	public AndrPoint point(double x, double y) {
		return new AndrPoint(x,y);
	}

	@Override
	public AndrCardExtractor cardExtractor() {
		if (_extractor == null) {
			_extractor = new AndrCardExtractor();
			
		}

		return _extractor;
	}

	@Override
	public AndrGame game() {
		if (_game == null) {
			_game = new AndrGame();
		}

		return _game;
	}

	@Override
	public BSprite sprite(IBRaster raster) {
		return new AndrSprite(raster);
	}

	@Override
	public BLabel label(String text) {
	  return new AndrLabel(text);
	}

	@Override
	public AndrBox box(IBRectangle r, IBColor color) {
		return new AndrBox(r,color);
	}

	@Override
	public IBRaster raster(BResourceLocator test, boolean transparent) {
		InputStream is = open(test);
		Bitmap b = BitmapFactory.decodeStream(is);
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
	public InputStream open(BResourceLocator r) {
		try{
			URL f = r.url();
			if( f != null ){
				return f.openStream();
			}
			String s = r.toString();
			if( s.startsWith("/") ){
				s = s.substring(1);
			}
			return context().getAssets().open(s);
		}
		catch( IOException e ){
			throw new BException("Unable to open:" + r, e );
		}
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
}
