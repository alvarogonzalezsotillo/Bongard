package bongard.gui.game;

import java.io.InputStreamReader;
import java.io.Reader;

import bongard.problem.BCardExtractor;

import ollitos.animation.BConcatenateAnimation;
import ollitos.animation.BRunnableAnimation;
import ollitos.animation.BWaitAnimation;
import ollitos.animation.IBAnimation;
import ollitos.geom.IBRectangle;
import ollitos.geom.IBTransform;
import ollitos.gui.basic.IBCanvas;
import ollitos.gui.container.BDrawableContainer;
import ollitos.gui.container.BFlippableContainer;
import ollitos.gui.event.BLogListener;
import ollitos.gui.event.BLogListener.ReplayAnimation;
import ollitos.platform.BFactory;
import ollitos.platform.BResourceLocator;



public class BExamplesField extends BDrawableContainer{

	private static final long EXAMPLES_SEED = 2;
	private BFlippableContainer _fc;
	private ReplayAnimation _replayAnimation;
	
	@Override
	public IBRectangle originalSize() {
		return _fc.originalSize();
	}
	
	@Override
	protected void draw_internal(IBCanvas c, IBTransform t) {
		_fc.draw(c,t);
		if( _replayAnimation != null && _replayAnimation.cursor() != null ){
			_replayAnimation.cursor().draw(c, t);
		}
	}
	
	public BExamplesField() {
		BResourceLocator[] problems = BCardExtractor.exampleProblems();
		_fc = new BFlippableContainer( new BGameModel(true,problems,EXAMPLES_SEED) );
		
		BResourceLocator rl = new BResourceLocator("/images/examples/examples.events");
		Reader r = new InputStreamReader( BFactory.instance().open(rl) );
		_replayAnimation = new BLogListener.ReplayAnimation(r, _fc.listener() );
		IBAnimation backToStartAnimation = new BRunnableAnimation(2000, new Runnable(){
			@Override
			public void run() {
				BFactory.instance().game().canvas().setDrawable( new BStartField() );
			}
		});
		IBAnimation a = new BConcatenateAnimation(new BWaitAnimation(1000), _replayAnimation,backToStartAnimation);
		BFactory.instance().game().animator().addAnimation( a );
	}
}
