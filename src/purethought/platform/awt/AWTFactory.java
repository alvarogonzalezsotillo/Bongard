package purethought.platform.awt;

import purethought.geom.IBRectangle;
import purethought.gui.basic.BLabel;
import purethought.gui.basic.BSprite;
import purethought.gui.basic.IBRaster;
import purethought.platform.BFactory;

public class AWTFactory extends BFactory {

	private AWTGame _game;
	
	/**
	 * 
	 * @return
	 */
	public AWTGame game(){
		if (_game == null) {
			_game = new AWTGame();
		}
		return _game;
	}
	
	/**
	 * 
	 */
	@Override
	public AWTTransform identityTransform() {
		return new AWTTransform();
	}

	/**
	 * 
	 */
	@Override
	public AWTPoint point(double x, double y) {
		return new AWTPoint(x, y);
	}

	@Override
	public AWTCardExtractor cardExtractor() {
		return new AWTCardExtractor();
	}


	@Override
	public AWTSprite sprite(IBRaster raster) {
		return new AWTSprite(raster);
	}


	@Override
	public AWTLabel label(String text) {
		return new AWTLabel(text);
	}

	@Override
	public AWTBox box(IBRectangle r) {
		return new AWTBox(r);
	}
	

}
