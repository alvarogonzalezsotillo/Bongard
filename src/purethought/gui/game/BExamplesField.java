package purethought.gui.game;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
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
import purethought.platform.BFactory;
import purethought.platform.BImageLocator;

public class BExamplesField extends BDrawableContainer{

	private BFlippableContainer _fc;
	
	@Override
	public IBRectangle originalSize() {
		return _fc.originalSize();
	}
	
	@Override
	protected void draw_internal(IBCanvas c, IBTransform t) {
		_fc.draw(c,t);
	}
	
	public BExamplesField() {
		BImageLocator[] problems = BFactory.instance().cardExtractor().exampleProblems();
		_fc = new BFlippableContainer( new BGameModel(problems ) );
		
		try {
			Reader r = new InputStreamReader( new FileInputStream("test.events") );
			IBAnimation replayAnimation = new BLogListener.ReplayAnimation(r, _fc.listener() );
			IBAnimation backToStartAnimation = new BRunnableAnimation(1000, new Runnable(){
				@Override
				public void run() {
					BFactory.instance().game().canvas().setDrawable( new BStartField() );
				}
			});
			IBAnimation a = new BConcatenateAnimation(replayAnimation,backToStartAnimation);
			BFactory.instance().game().animator().addAnimation( a );
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
}
