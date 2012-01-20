package purethought.gui.game;

import purethought.gui.container.IBFlippableDrawable;
import purethought.gui.container.IBFlippableModel;
import purethought.problem.BProblemLocator;

public class BGameModel implements IBFlippableModel{
	
	
	private BProblemLocator[] _problems;
	private BGameField[] _drawables;

	public BGameModel( BProblemLocator[] problems ){
		setProblems(problems);
	}

	private void setProblems(BProblemLocator[] problems) {
		_problems = problems;
		_drawables = new BGameField[problems.length];
		for (int i = 0; i < problems.length; i++) {
			BProblemLocator l = problems[i];
			_drawables[i] = new BGameField(l);
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

}
