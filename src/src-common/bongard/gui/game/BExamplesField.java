package bongard.gui.game;

import java.io.InputStreamReader;
import java.io.Reader;

import bongard.animation.BConcatenateAnimation;
import bongard.animation.BRunnableAnimation;
import bongard.animation.BWaitAnimation;
import bongard.animation.IBAnimation;
import bongard.geom.IBRectangle;
import bongard.geom.IBTransform;
import bongard.gui.basic.IBCanvas;
import bongard.gui.container.BDrawableContainer;
import bongard.gui.container.BFlippableContainer;
import bongard.gui.event.BLogListener;
import bongard.gui.event.BLogListener.ReplayAnimation;
import bongard.platform.BFactory;
import bongard.platform.BResourceLocator;


public class BExamplesField extends BDrawableContainer{

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
		BResourceLocator[] problems = BFactory.instance().cardExtractor().exampleProblems();
		_fc = new BFlippableContainer( new BGameModel(true,problems ) );
		
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
