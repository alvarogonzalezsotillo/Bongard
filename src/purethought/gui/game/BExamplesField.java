package purethought.gui.game;

import java.io.InputStreamReader;
import java.io.Reader;

import purethought.animation.BConcatenateAnimation;
import purethought.animation.BRunnableAnimation;
import purethought.animation.IBAnimation;
import purethought.geom.IBRectangle;
import purethought.geom.IBTransform;
import purethought.gui.basic.IBCanvas;
import purethought.gui.container.BDrawableContainer;
import purethought.gui.container.BFlippableContainer;
import purethought.gui.event.BLogListener;
import purethought.gui.event.BLogListener.ReplayAnimation;
import purethought.platform.BFactory;
import purethought.platform.BResourceLocator;

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
		IBAnimation a = new BConcatenateAnimation(_replayAnimation,backToStartAnimation);
		BFactory.instance().game().animator().addAnimation( a );
	}
}
