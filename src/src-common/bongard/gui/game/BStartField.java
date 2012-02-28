package bongard.gui.game;

import bongard.animation.BConcatenateAnimation;
import bongard.animation.BRunnableAnimation;
import bongard.animation.IBAnimation;
import bongard.geom.IBPoint;
import bongard.geom.IBRectangle;
import bongard.geom.IBTransform;
import bongard.gui.basic.BLabel;
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
		double x1 = originalSize().x() + (originalSize().w())/2;
		double y1 = originalSize().y() + (originalSize().h())/4;
		_startSprite.transform().translate(x1, y1);
		
		IBRaster hs = f.raster( new BResourceLocator("/images/start/help.png"), false );
		_helpSprite = f.sprite(hs);
		double x2 = originalSize().x() + (originalSize().w())/2;
		double y2 = originalSize().y() + 3*(originalSize().h())/4;
		_helpSprite.transform().translate(x2, y2);
		
		
		f.logger().log( "BStartField:" );
		f.logger().log( "  originalSize:" + originalSize() );
		f.logger().log( "  originalSize:" + originalSize() );
		f.logger().log( "  x1 y1:" + x1 + " "  + y1 );
		f.logger().log( "  _startSprite:" + _startSprite.position() );
		f.logger().log( "  x2 y2:" + x2 + " "  + y2 );
		f.logger().log( "  _helpSprite:" + _helpSprite.position() );
		
		listener().addListener(_adapter);
	}

	
	private BEventAdapter _adapter = new BEventAdapter(this) {
		@Override
		public boolean pointerDown(IBPoint pInMyCoordinates) {
			if( _helpSprite.inside(pInMyCoordinates, null) ){
				return true;
			}

			if( _startSprite.inside(pInMyCoordinates, null) ){
				return true;
			}
			return false;
		}
		@Override
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
		
		{
			IBRectangle os = originalSize();
			for( int i = 0 ; i < os.h() ; i += 50 ){
				BLabel l = BFactory.instance().label(i + "," + i);
				l.transform().translate(os.x() + i, os.y() + i );
				l.draw(c, t);
			}
		}
		
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
