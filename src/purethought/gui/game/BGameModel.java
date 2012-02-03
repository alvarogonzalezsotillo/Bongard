package purethought.gui.game;

import purethought.animation.BRunnableAnimation;
import purethought.gui.container.BFlippableContainer;
import purethought.gui.container.IBFlippableDrawable;
import purethought.gui.container.IBFlippableModel;
import purethought.platform.BFactory;
import purethought.platform.BResourceLocator;

public class BGameModel implements IBFlippableModel{
	
	private BGameField[] _drawables;
	private boolean _demo;

	public BGameModel( boolean demo, BResourceLocator[] problems ){
		setProblems(problems);
		_demo = demo;
	}

	private void setProblems(BResourceLocator[] problems) {
		_drawables = new BGameField[problems.length];
		for (int i = 0; i < problems.length; i++) {
			BResourceLocator l = problems[i];
			_drawables[i] = new BGameField(l,this);
		}
	}

	@Override
	public BGameField drawable(int x) {
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

	public void answered(BGameField bGameField) {
		if( _demo ){
			return;
		}
		if( allAnswered() ){
			int width = width();
			if( allCorrectAnswered() ){
				width += 2;
			}
			final int goTo =width;
			BFactory.instance().game().animator().addAnimation( new BRunnableAnimation(1000, new Runnable(){
				@Override
				public void run() {
					goToLevel(_demo,goTo);
				}
			}));
		}
	}
	
	public static void goToLevel(boolean demo, int width) {
		BResourceLocator[] problems = BFactory.instance().cardExtractor().randomProblems(width);
		goToProblems(demo,problems);
	}

	private static void goToProblems(boolean demo, BResourceLocator[] problems) {
		BGameModel m = new BGameModel(demo,problems );
		BFlippableContainer d = new BFlippableContainer( m );
		BFactory.instance().game().canvas().setDrawable( d );
	}

	private boolean allAnswered(){
		for( int i = 0 ; i < width() ; i++ ){
			BGameField d = drawable(i);
			if( !d.badAnswer() && !d.correctAnswer() ){
				return false;
			}
		}
		return true;
	}

	private boolean allCorrectAnswered(){
		for( int i = 0 ; i < width() ; i++ ){
			BGameField d = drawable(i);
			if( !d.correctAnswer() ){
				return false;
			}
		}
		return true;
	}

}
