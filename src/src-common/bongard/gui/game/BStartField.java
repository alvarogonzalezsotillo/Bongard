package bongard.gui.game;

import bongard.animation.BConcatenateAnimation;
import bongard.animation.BRunnableAnimation;
import bongard.animation.IBAnimation;
import bongard.geom.IBPoint;
import bongard.geom.IBRectangle;
import bongard.geom.IBTransform;
import bongard.gui.basic.BSprite;
import bongard.gui.basic.IBCanvas;
import bongard.gui.basic.IBRaster;
import bongard.gui.container.BDrawableContainer;
import bongard.gui.event.BEventAdapter;
import bongard.platform.BFactory;
import bongard.platform.BResourceLocator;

public class BStartField extends BDrawableContainer{

	private BSprite _helpSprite;
	private BSprite _startSprite;

	@Override
	public IBRectangle originalSize() {
		return BGameField.computeOriginalSize();
	}
	
	public BStartField() {
		BFactory f = BFactory.instance();
		IBRaster ss = f.raster( new BResourceLocator("/images/start/start.png"), false );
		_startSprite = f.sprite(ss);
		double x = originalSize().x() + (originalSize().w())/2;
		double y = originalSize().y() + (originalSize().h())/4;
		_startSprite.transform().translate(x, y);
		
		IBRaster hs = f.raster( new BResourceLocator("/images/start/help.png"), false );
		_helpSprite = f.sprite(hs);
		x = originalSize().x() + (originalSize().w())/2;
		y = originalSize().y() + 3*(originalSize().h())/4;
		_helpSprite.transform().translate(x, y);
		
		
		listener().addListener(_adapter);
	}

	
	private BEventAdapter _adapter = new BEventAdapter(this) {
		public boolean pointerClick(IBPoint pInMyCoordinates) {
			
			if( _helpSprite.inside(pInMyCoordinates, null) ){
				helpPressed();
			}

			if( _startSprite.inside(pInMyCoordinates, null) ){
				startPressed();
			}

			return false;
		};
	};
	
	@Override
	protected void draw_internal(IBCanvas c, IBTransform t) {
		_startSprite.draw(c,t);
		_helpSprite.draw(c,t);
		
	}

	private IBAnimation createPressedAnimation(BSprite s){
		//return new BScaleAnimation(10, 10, 400, s);
		return null;
	}
	
	protected void startPressed() {
		IBAnimation a = createPressedAnimation(_startSprite);
		a = new BConcatenateAnimation( a, new BRunnableAnimation(10, new Runnable(){
			@Override
			public void run() {
				BGameModel.goToLevel(false,3,true);
			}
		}));
		BFactory.instance().game().animator().addAnimation(a);
	}

	protected void helpPressed() {
		IBAnimation a = createPressedAnimation(_helpSprite);
		a = new BConcatenateAnimation( a, new BRunnableAnimation(10, new Runnable(){
			@Override
			public void run() {
				BFactory.instance().game().canvas().setDrawable( new BExamplesField() );
			}
		}));
		BFactory.instance().game().animator().addAnimation(a);
	}
}
