package purethought.gui.game;

import purethought.animation.BAnimator;
import purethought.animation.BRunnableAnimation;
import purethought.geom.IBPoint;
import purethought.geom.IBRectangle;
import purethought.geom.IBTransform;
import purethought.gui.basic.IBCanvas;
import purethought.gui.container.BDrawableContainer;
import purethought.gui.container.BFlippableContainer;
import purethought.gui.event.BEventAdapter;
import purethought.platform.BFactory;
import purethought.platform.BImageLocator;

public class BExamplesField extends BDrawableContainer{

	private BGameField _gameField = new BGameField();
	
	@Override
	public IBRectangle originalSize() {
		return _gameField.originalSize();
	}
	
	@Override
	protected void draw_internal(IBCanvas c, IBTransform t) {
		_gameField.draw(c,t);
	}
	
	private BImageLocator[] _problems = BFactory.instance().cardExtractor().exampleProblems();
	private int _problemIndex = 0;
	private boolean _autosolving = false;
	
	private BEventAdapter _adapter = new BEventAdapter(this) {
		private BRunnableAnimation _avanceSampleAnimation;

		@Override
		public boolean pointerUp(IBPoint pInMyCoordinates) {
			
			BAnimator a = BFactory.instance().game().animator();
			
			if( _autosolving ){
				_avanceSampleAnimation.cancel();
				advanceSample();
			}
			
			_gameField.autoSolve();
			_autosolving = true;			
			
			_avanceSampleAnimation = new BRunnableAnimation(2000, new Runnable(){
				public void run() {
					advanceSample();
				}
			});
			
			a.addAnimation(	_avanceSampleAnimation );
			return true;
		};
		
		private void advanceSample(){
			_autosolving = false;
			_problemIndex++;
			if( _problemIndex >= _problems.length ){
				BImageLocator[] problems = BFactory.instance().cardExtractor().randomProblems(7);
				BFactory.instance().game().canvas().setDrawable( new BFlippableContainer( new BGameModel(problems ) ) );
			}
			else{
				_gameField.setProblem(_problems[_problemIndex]);
			}
		}
	};
	
	public BExamplesField() {
		listener().addListener( _adapter );
		_gameField.setProblem(_problems[_problemIndex]);
	}

}
