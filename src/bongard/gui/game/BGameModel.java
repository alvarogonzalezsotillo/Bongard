package bongard.gui.game;

import bongard.animation.BRunnableAnimation;
import bongard.gui.container.BFlippableContainer;
import bongard.gui.container.IBFlippableModel;
import bongard.platform.BFactory;
import bongard.platform.BResourceLocator;
import bongard.problem.BCardExtractor;

public class BGameModel implements IBFlippableModel{
	
	private static final int MAX_WIDTH = 13;
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
			int width = width()+2;
			if( !allCorrectAnswered() ){
				width = 3;
			}
			final int goTo = width;
			BFactory.instance().game().animator().addAnimation( new BRunnableAnimation(1000, new Runnable(){
				@Override
				public void run() {
					goToLevel(_demo,goTo,true);
				}
			}));
		}
	}
	
	public static void goToLevel(boolean demo, int width, boolean limitDificulty) {
		
		if( width > MAX_WIDTH ){
			width = MAX_WIDTH;
		}
		
		BCardExtractor ce = BFactory.instance().cardExtractor();
		BResourceLocator[] problems = null;
		if( limitDificulty ){
			if( width <= 3 ){
				problems = ce.randomProblems(width,10);
			}
			else if( width <= 5 ){
				problems = ce.randomProblems(width,30);
			}
		}
		
		if( problems == null ){
			problems = ce.randomProblems(width);
		}
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
