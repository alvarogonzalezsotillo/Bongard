package purethought.awt;

import java.io.File;
import java.util.Formatter;
import java.util.Random;

import purethought.gui.BFactory;
import purethought.gui.BSprite;
import purethought.gui.IBRaster;
import purethought.problem.BProblemLocator;

public class AWTFactory extends BFactory {

	private AWTGame _game;
	private AWTCanvas _canvas;
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
	public AWTCanvas canvas(){
		if (_canvas == null) {
			_canvas = new AWTCanvas( game().canvas() );
		}

		return _canvas;
	}

	@Override
	public BSprite create(IBRaster raster) {
		return new AWTSprite(raster);
	}

	@Override
	public BProblemLocator randomProblem() {
		Random r = new Random();
		Formatter f = new Formatter();
		f.format("p%03d.png", r.nextInt(280)+1 );
		File file = new File("2bpp-png/" + f.toString() );
		return new BProblemLocator(file);
	}

	@Override
	public AWTAnimator animator() {
		if (_animator == null) {
			_animator = new AWTAnimator();
		}

		return _animator;
	}
	

}
