package bongard.platform.andr;

import java.io.InputStream;

import android.content.Context;
import bongard.geom.IBPoint;
import bongard.geom.IBRectangle;
import bongard.geom.IBTransform;
import bongard.gui.basic.BBox;
import bongard.gui.basic.BLabel;
import bongard.gui.basic.BSprite;
import bongard.gui.basic.IBColor;
import bongard.gui.basic.IBRaster;
import bongard.gui.game.IBGame;
import bongard.platform.BFactory;
import bongard.platform.BResourceLocator;
import bongard.problem.BCardExtractor;

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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BLabel label(String text) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BBox box(IBRectangle r, IBColor color) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IBRaster raster(BResourceLocator test, boolean transparent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public InputStream open(BResourceLocator r) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IBColor color(String c) {
		// TODO Auto-generated method stub
		return null;
	}

	public static Context context() {
		return _context;
	}

}
