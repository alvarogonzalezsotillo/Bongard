package purethought.platform;

import purethought.geom.IBPoint;
import purethought.geom.IBRectangle;
import purethought.geom.IBTransform;
import purethought.gui.basic.BBox;
import purethought.gui.basic.BLabel;
import purethought.gui.basic.BSprite;
import purethought.gui.basic.IBRaster;
import purethought.gui.game.IBGame;
import purethought.platform.awt.AWTFactory;
import purethought.problem.BCardExtractor;

public abstract class BFactory {
	
	private static BFactory _instance;
	
	public static BFactory instance(){
		if (_instance == null) {
			_instance = new AWTFactory();
		}
		return _instance;
	}


	protected BFactory() {
	}
	
	public abstract IBTransform identityTransform();
	public abstract IBPoint point(double x, double y);
	public abstract BCardExtractor cardExtractor();
	public abstract IBGame game();
	public abstract BSprite sprite( IBRaster raster );
	public abstract BLabel label( String text );
	public abstract BBox box( IBRectangle r );
	public abstract IBRaster raster(BImageLocator test, boolean transparent);
	
}
