package purethought.awt;

import purethought.gui.BLabel;
import purethought.gui.BSprite;
import purethought.gui.IBRaster;
import purethought.util.BFactory;

public class AWTFactory extends BFactory {

	private AWTGame _game;
	private AWTAnimator _animator;
	
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
	public BSprite sprite(IBRaster raster) {
		return new AWTSprite(raster);
	}

	@Override
	public AWTAnimator animator() {
		if (_animator == null) {
			_animator = new AWTAnimator();
		}

		return _animator;
	}

	@Override
	public BLabel label(String text) {
		return new AWTLabel(text);
	}
	

}
