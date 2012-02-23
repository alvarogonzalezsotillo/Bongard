package bongard.platform.andr;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import bongard.geom.IBRectangle;
import bongard.gui.basic.BBox;
import bongard.gui.basic.BLabel;
import bongard.gui.basic.BSprite;
import bongard.gui.basic.IBColor;
import bongard.gui.basic.IBRaster;
import bongard.platform.BFactory;
import bongard.platform.BResourceLocator;
import bongard.problem.BCardExtractor;
import bongard.util.BException;

public class AndrFactory extends BFactory{

	private static Context _context;
	private AndrGame _game;

	@Override
	public AndrTransform identityTransform() {
		return new AndrTransform();
	}

	@Override
	public AndrPoint point(double x, double y) {
		return new AndrPoint(x,y);
	}

	@Override
	public BCardExtractor cardExtractor() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AndrGame game() {
		if (_game == null) {
			_game = new AndrGame( context() );
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
	public BBox box(IBRectangle r, IBColor color) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IBRaster raster(BResourceLocator test, boolean transparent) {
		InputStream is = open(test);
		Bitmap b = BitmapFactory.decodeStream(is);
		return new AndrRaster(b);
	}

	@Override
	public InputStream open(BResourceLocator r) {
		try{
			URL f = r.getImpl(URL.class);
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
}
