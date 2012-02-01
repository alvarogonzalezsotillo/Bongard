package purethought.gui.game;

import purethought.gui.container.IBFlippableDrawable;
import purethought.gui.container.IBFlippableModel;
import purethought.platform.BResourceLocator;

public class BGameModel implements IBFlippableModel{
	
	
	private BGameField[] _drawables;

	public BGameModel( BResourceLocator[] problems ){
		setProblems(problems);
	}

	private void setProblems(BResourceLocator[] problems) {
		_drawables = new BGameField[problems.length];
		for (int i = 0; i < problems.length; i++) {
			BResourceLocator l = problems[i];
			_drawables[i] = new BGameField(l,this);
		}
	}

	@Override
	public IBFlippableDrawable drawable(int x) {
		return _drawables[x];
	}

	@Override
	public int width() {
		return _drawables.length;
	}

	@Override
	public BResourceLocator background() {
		return new BResourceLocator( "/images/backgrounds/arrecibo.png" );
	}

}
