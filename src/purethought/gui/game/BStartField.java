package purethought.gui.game;

import purethought.animation.BConcatenateAnimation;
import purethought.animation.BRunnableAnimation;
import purethought.animation.BScaleAnimation;
import purethought.animation.IBAnimation;
import purethought.geom.BRectangle;
import purethought.geom.IBPoint;
import purethought.geom.IBRectangle;
import purethought.geom.IBTransform;
import purethought.gui.basic.BLabel;
import purethought.gui.basic.BSprite;
import purethought.gui.basic.IBCanvas;
import purethought.gui.basic.IBRaster;
import purethought.gui.container.BDrawableContainer;
import purethought.gui.container.BFlippableContainer;
import purethought.gui.event.BEventAdapter;
import purethought.platform.BFactory;
import purethought.platform.BImageLocator;

public class BStartField extends BDrawableContainer{

	private BSprite _helpSprite;
	private BSprite _startSprite;

	@Override
	public IBRectangle originalSize() {
		return new BRectangle(0, 0, 240, 320);
	}
	
	public BStartField() {
		BFactory f = BFactory.instance();
		IBRaster ss = f.raster( new BImageLocator("/images/start/start.png"), false );
		_startSprite = f.sprite(ss);
		double x = originalSize().x() + (originalSize().w())/2;
		double y = originalSize().y() + (originalSize().h())/4;
		System.out.println( originalSize() );
		System.out.println( _startSprite.originalSize() );
		System.out.println( "x:" + x + "  y:" + y );
		_startSprite.transform().translate(x, y);
		
		IBRaster hs = f.raster( new BImageLocator("/images/start/help.png"), false );
		_helpSprite = f.sprite(hs);
		x = originalSize().x() + (originalSize().w())/2;
		y = originalSize().y() + 3*(originalSize().h())/4;
		_helpSprite.transform().translate(x, y);
		System.out.println( "x:" + x + "  y:" + y );
		
		
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
		super.draw_internal(c, t);
		BFactory.instance().box( BRectangle.grow( originalSize(), -3), "888888").draw(c, t);
		
		_startSprite.draw(c,t);
		_helpSprite.draw(c,t);
		
		{
			BLabel l = BFactory.instance().label("*");
			l.transform().translate(70,300);
			l.draw(c, t);
		}
		
	}

	private IBAnimation createPressedAnimation(BSprite s){
		return new BScaleAnimation(10, 10, 400, s);
	}
	
	protected void startPressed() {
		IBAnimation a = createPressedAnimation(_startSprite);
		a = new BConcatenateAnimation( a, new BRunnableAnimation(10, new Runnable(){
			@Override
			public void run() {
				BImageLocator[] problems = BFactory.instance().cardExtractor().randomProblems(7);
				BFactory.instance().game().canvas().setDrawable( new BFlippableContainer( new BGameModel(problems ) ) );
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
