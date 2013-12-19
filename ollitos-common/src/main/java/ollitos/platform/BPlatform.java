package ollitos.platform;

import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;

import ollitos.geom.IBPoint;
import ollitos.geom.IBRectangle;
import ollitos.geom.IBTransform;
import ollitos.gui.basic.BBox;
import ollitos.gui.basic.BLabel;
import ollitos.gui.basic.BSprite;
import ollitos.platform.raster.BRasterProviderCache;
import ollitos.platform.raster.IBRasterProvider;
import ollitos.platform.raster.IBRasterUtil;
import ollitos.platform.state.BStateManager;
import ollitos.platform.state.IBKeyValueDatabase;
import ollitos.util.BException;


public abstract class BPlatform {
	
	public static final IBColor COLOR_WHITE = instance().color("ffffff");
	public static final IBColor COLOR_YELLOW = instance().color("ffff00");
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

    public abstract boolean openInExternalApplication(BResourceLocator l);
	

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
			throw new BException( "Can't find implementation", null );
		}
		
		try{
			return (BPlatform) c.newInstance();
		}
		catch( Throwable e ){
			throw new BException( "Unexpected error", e );
		}
	}

	

	protected BPlatform() {
	}
	
	public abstract IBRasterUtil rasterUtil();
	public abstract IBTransform identityTransform();
	public abstract IBPoint point(double x, double y);
	public abstract IBGame game();
	public abstract IBColor color(String c);
	public abstract IBLogger logger();
	
	public BLabel label( String text ){
		return new BLabel(text);
	}
	

	public BBox box( IBRectangle r, IBColor color ){
		return new BBox( r, color );
	}

		
	
	public abstract URL platformURL(BResourceLocator r);
	
	public InputStream open(BResourceLocator r) throws IOException{
		URL f = r.url();
		if( f == null ){
			return null;
		}
		logger().log(this, "open:" + f );
		return f.openStream();
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


	private BStateManager _stateManager;

	public BStateManager stateManager() {
		if (_stateManager == null) {
			_stateManager = new BStateManager();
		}

		return _stateManager;
	}

	
	public abstract IBKeyValueDatabase database(String database);


}
