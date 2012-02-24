package bongard.platform;

import java.io.InputStream;

import bongard.geom.IBPoint;
import bongard.geom.IBRectangle;
import bongard.geom.IBTransform;
import bongard.gui.basic.BBox;
import bongard.gui.basic.BLabel;
import bongard.gui.basic.BSprite;
import bongard.gui.basic.IBColor;
import bongard.gui.basic.IBRaster;
import bongard.gui.game.IBGame;
import bongard.problem.BCardExtractor;


public abstract class BFactory {
	
	public static final IBColor COLOR_WHITE = instance().color("ffffff");
	public static final IBColor COLOR_BLACK = instance().color("000000");
	public static final IBColor COLOR_DARKGRAY = instance().color("888888");
	public static final IBColor COLOR_GRAY = instance().color("999999");
	
	private static BFactory _instance;
	
	public static BFactory instance(){
		if (_instance == null) {
			_instance = createInstance();
		}
		return _instance;
	}


	private static BFactory createInstance() {
		Class<?> c = null;
		try {
			c = Class.forName("bongard.platform.awt.AWTFactory");
		} 
		catch (Throwable e) {
			//e.printStackTrace();
		}
		
		try {
			c = Class.forName("bongard.platform.andr.AndrFactory");
		} 
		catch (Throwable e) {
			//e.printStackTrace();
		}
		
		if( c == null ){
			return null;
		}
		
		try{
			return (BFactory) c.newInstance();
		}
		catch( Throwable e ){
			//e.printStackTrace();
		}
		return null;
	}

	

	protected BFactory() {
	}
	
	public abstract IBTransform identityTransform();
	public abstract IBPoint point(double x, double y);
	public abstract BCardExtractor cardExtractor();
	public abstract IBGame game();
	public abstract BSprite sprite( IBRaster raster );
	public abstract BLabel label( String text );
	public abstract BBox box( IBRectangle r, IBColor color );
	public abstract IBRaster raster(BResourceLocator test, boolean transparent);
	public abstract InputStream open(BResourceLocator r);
	public abstract IBColor color(String c);
	public abstract IBLogger logger();
	
	@SuppressWarnings("deprecation")
	public static void main(String[] args) {
		System.getProperties().save(System.out, "");
	}
}
