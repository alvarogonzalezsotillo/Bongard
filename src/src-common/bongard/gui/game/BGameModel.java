package bongard.gui.game;

import bongard.animation.BRunnableAnimation;
import bongard.gui.container.BFlippableContainer;
import bongard.gui.container.IBFlippableModel;
import bongard.platform.BFactory;
import bongard.platform.BResourceLocator;
import bongard.problem.BCardExtractor;
import bongard.problem.BProblem;

@SuppressWarnings("serial")
public class BGameModel implements IBFlippableModel{
	
	public static final int MAX_WIDTH = 12;
	private static final int INITIAL_WIDTH = 12;
	private static final int WIDTH_INCREMENT = 1;
	transient private BResourceLocator _background;
	private BGameField[] _drawables;
	private boolean _demo;
	private BResourceLocator[] _problems;

	public BGameModel( boolean demo, BResourceLocator[] problems ){
		setProblems(problems);
		_demo = demo;
	}

	private void setProblems(BResourceLocator[] problems) {
		_problems = problems;
		_drawables = null;
	}

	private BGameField[] drawables(){
		if( _drawables == null ){
			_drawables = new BGameField[_problems.length];
			for (int i = 0; i < _problems.length; i++) {
				BResourceLocator l = _problems[i];
				BProblem p = new BProblem(l);
				_drawables[i] = new BGameField(p,this);
			}
		}
		return _drawables;
	}
	
	@Override
	public BGameField drawable(int x) {
		return drawables()[x];
	}

	@Override
	public int width() {
		return drawables().length;
	}

	@Override
	public BResourceLocator background(){
		if( _background == null ){
			_background = new BResourceLocator( "/images/backgrounds/arrecibo.png" );
		}
		return _background;
	}

	public void answered(BGameField bGameField) {
		if( _demo ){
			return;
		}
		if( allAnswered() ){
			int width = width()+WIDTH_INCREMENT;
			if( !allCorrectAnswered() ){
				width = INITIAL_WIDTH;
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
	
	private static void goToLevel(boolean demo, int width, boolean limitDificulty) {
		
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

	public static void goToInitialLevel() {
		goToLevel(false,INITIAL_WIDTH,true);
	}
	
	@Override
	public void dispose(int x) {
		drawable(x).dispose();
	}
}
