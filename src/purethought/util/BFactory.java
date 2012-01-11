package purethought.util;

import purethought.animation.BAnimator;
import purethought.awt.AWTFactory;
import purethought.gui.BGameField;
import purethought.gui.BSprite;
import purethought.gui.IBCanvas;
import purethought.gui.IBGame;
import purethought.gui.IBPoint;
import purethought.gui.IBRaster;
import purethought.gui.IBTransform;
import purethought.problem.BCardExtractor;
import purethought.problem.BProblemLocator;

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
	public abstract IBCanvas canvas();
	public abstract IBGame game();
	public abstract BSprite create( IBRaster raster );
	public abstract BAnimator animator();
	
	public abstract BProblemLocator randomProblem();
}
