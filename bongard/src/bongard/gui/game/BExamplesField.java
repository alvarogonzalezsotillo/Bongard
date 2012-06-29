package bongard.gui.game;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;

import ollitos.animation.BConcatenateAnimation;
import ollitos.animation.BRunnableAnimation;
import ollitos.animation.BWaitAnimation;
import ollitos.animation.IBAnimation;
import ollitos.gui.basic.BRectangularDrawable;
import ollitos.gui.container.BSlidableContainer;
import ollitos.gui.event.BLogListener;
import ollitos.gui.event.BLogListener.ReplayAnimation;
import ollitos.platform.BPlatform;
import ollitos.platform.BResourceLocator;
import ollitos.platform.IBCanvas;
import ollitos.util.BException;
import bongard.problem.BCardExtractor;



public class BExamplesField extends BRectangularDrawable{

	private static final long EXAMPLES_SEED = 2;
	private BSlidableContainer _fc;
	private ReplayAnimation _replayAnimation;
	
	@Override
	protected void draw_internal(IBCanvas c) {
		_fc.draw(c,canvasContext().transform());
		if( _replayAnimation != null && _replayAnimation.cursor() != null ){
			_replayAnimation.cursor().draw(c, canvasContext().transform());
		}
	}
	
	public BExamplesField() {
		super( BGameField.computeOriginalSize() );
		BResourceLocator[] problems = BCardExtractor.exampleProblems();
		_fc = new BSlidableContainer( originalSize(), new BGameModel(true,problems,EXAMPLES_SEED) );
		
		BResourceLocator rl = new BResourceLocator("/examples/examples.events");
		Reader r;
		try {
			r = new InputStreamReader( platform().open(rl) );
		}
		catch (IOException ex) {
			throw new BException("Cant open:" + rl, ex );
		}
		_replayAnimation = new BLogListener.ReplayAnimation(r, _fc.listener() );
		IBAnimation backToStartAnimation = new BRunnableAnimation(2000, new Runnable(){
			@Override
			public void run() {
				BPlatform.instance().game().screen().setDrawable( new BStartField() );
			}
		});
		IBAnimation a = new BConcatenateAnimation(new BWaitAnimation(1000), _replayAnimation,backToStartAnimation);
		platform().game().animator().addAnimation( a );
	}
}
