package purethought.util;

import purethought.animation.BAnimator;
import purethought.awt.AWTFactory;
import purethought.geom.IBPoint;
import purethought.geom.IBTransform;
import purethought.gui.BLabel;
import purethought.gui.BSprite;
import purethought.gui.IBGame;
import purethought.gui.IBRaster;
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
	public abstract BAnimator animator();
}
