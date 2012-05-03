package ollitos.platform;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import ollitos.geom.IBPoint;
import ollitos.geom.IBRectangle;
import ollitos.geom.IBTransform;
import ollitos.gui.basic.BBox;
import ollitos.gui.basic.BHTMLDrawable;
import ollitos.gui.basic.BLabel;
import ollitos.gui.basic.BSprite;
import ollitos.util.BException;


public abstract class BPlatform {
	
	public static final IBColor COLOR_WHITE = instance().color("ffffff");
	public static final IBColor COLOR_BLACK = instance().color("000000");
	public static final IBColor COLOR_DARKGRAY = instance().color("888888");
	public static final IBColor COLOR_GRAY = instance().color("999999");
	
	private static BPlatform _instance;
	
	public static BPlatform instance(){
		if (_instance == null) {
			_instance = createInstance();
		}
		return _instance;
	}


	private static BPlatform createInstance() {
		Class<?> c = null;
		try {
			c = Class.forName("ollitos.platform.awt.AWTPlatform");
		} 
		catch (Throwable e) {
			//e.printStackTrace();
		}
		
		try {
			c = Class.forName("ollitos.platform.andr.AndrPlatform");
		} 
		catch (Throwable e) {
			//e.printStackTrace();
		}
		
		if( c == null ){
			return null;
		}
		
		try{
			return (BPlatform) c.newInstance();
		}
		catch( Throwable e ){
			//e.printStackTrace();
		}
		return null;
	}

	

	protected BPlatform() {
	}
	
	public abstract IBRasterUtil rasterUtil();
	public abstract IBTransform identityTransform();
	public abstract IBPoint point(double x, double y);
	public abstract IBGame game();
	public abstract BSprite sprite( IBRaster raster );
	public abstract BLabel label( String text );
	public abstract BBox box( IBRectangle r, IBColor color );
	public abstract IBRaster raster(BResourceLocator test, boolean transparent);
	public abstract IBColor color(String c);
	public abstract IBLogger logger();
	public abstract BHTMLDrawable html();
	
	public BSprite sprite( BResourceLocator l ){
		return sprite( raster( l, true ) );
	}
	
	public abstract URL platformURL(BResourceLocator r);
	
	public InputStream open(BResourceLocator r){
		URL f = r.url();
		if( f == null ){
			return null;
		}
		logger().log(this, "open:" + f );
		try{
			return f.openStream();
		}
		catch( IOException e ){
			throw new BException( "Unable to open:" + f + "(" + r + ")", e );
		}
	}

	protected BCanvasContext createCanvasContext(){
		return new BCanvasContext();
	}

	public final IBCanvas.CanvasContext canvasContext(){
		BCanvasContext ret = createCanvasContext();
		ret.setAlpha(1);
		ret.setColor( COLOR_BLACK );
		ret.setTransform( identityTransform() );
		ret.setAntialias( false );
		return ret;
	}


}
