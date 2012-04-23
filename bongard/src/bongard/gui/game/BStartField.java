package bongard.gui.game;

import ollitos.animation.BRunnableAnimation;
import ollitos.animation.IBAnimation;
import ollitos.geom.IBPoint;
import ollitos.geom.IBRectangle;
import ollitos.geom.IBTransform;
import ollitos.gui.basic.BSprite;
import ollitos.gui.basic.BState;
import ollitos.gui.basic.IBCanvas;
import ollitos.gui.basic.IBRaster;
import ollitos.gui.container.BDrawableContainer;
import ollitos.gui.container.BFlippableContainer;
import ollitos.gui.event.BEventAdapter;
import ollitos.gui.event.BRestartListener;
import ollitos.platform.BPlatform;
import ollitos.platform.BResourceLocator;

public class BStartField extends BDrawableContainer{

	private BSprite _helpGameSprite;
	private BSprite _startGameSprite;
	private BSprite _helpOriginalSprite;
	private BSprite _startOriginalSprite;

	@Override
	public IBRectangle originalSize() {
		return BGameField.computeOriginalSize();
	}
	
	private IBPoint spritePosition(int row, int column, double width){
		IBRectangle s = originalSize();
		double x = s.x() + width/2 + (1+column*2)*(s.w())/5;
		double y = s.y() + width/2 + (1+row*2)*(s.h())/5;
		
		return BPlatform.instance().point(x, y);
	}
	
	private BSprite createSprite(int row, int column, String resource ){
		BPlatform f = BPlatform.instance();
		IBRaster ss = f.raster( new BResourceLocator(resource), false );
		BSprite ret = f.sprite(ss);
		ret.setAntialias(true);
		IBPoint p = spritePosition(row,column, ss.w() );
		ret.transform().translate(p.x(),p.y());
		return ret;
	}
	
	public BStartField() {
		_startGameSprite = createSprite(0,0,"/images/start/startGame.png");
		_helpGameSprite = createSprite(0,1,"/images/start/helpGame.png");
		_startOriginalSprite = createSprite(1,0,"/images/start/startOriginal.png");
		_helpOriginalSprite = createSprite(1,1,"/images/start/helpOriginal.png");
		
		BRestartListener.install();
		
		listener().addListener(_adapter);
	}

	
	private BEventAdapter _adapter = new BEventAdapter(this) {
		@Override
		public boolean pointerDown(IBPoint pInMyCoordinates) {
			if( _helpGameSprite.inside(pInMyCoordinates, null) ){
				return true;
			}

			if( _startGameSprite.inside(pInMyCoordinates, null) ){
				return true;
			}

			if( _helpOriginalSprite.inside(pInMyCoordinates, null) ){
				return true;
			}

			if( _startOriginalSprite.inside(pInMyCoordinates, null) ){
				return true;
			}
			
			return false;
		}
		@Override
		public boolean pointerClick(IBPoint pInMyCoordinates) {
			
			if( _helpGameSprite.inside(pInMyCoordinates, null) ){
				helpGamePressed();
			}

			if( _startGameSprite.inside(pInMyCoordinates, null) ){
				startGamePressed();
			}

			if( _helpOriginalSprite.inside(pInMyCoordinates, null) ){
				helpOriginalPressed();
			}

			if( _startOriginalSprite.inside(pInMyCoordinates, null) ){
				startOriginalPressed();
			}

			return false;
		};
	};
	
	@Override
	protected void draw_internal(IBCanvas c, IBTransform t) {
		_startGameSprite.draw(c,t);
		_helpGameSprite.draw(c,t);
		_startOriginalSprite.draw(c,t);
		_helpOriginalSprite.draw(c,t);
	}

	protected void startGamePressed() {
		IBAnimation a = new BRunnableAnimation(10, new Runnable(){
			@Override
			public void run() {
				BGameModel.goToInitialLevel();
			}
		});
		BPlatform.instance().game().animator().addAnimation(a);
	}

	protected void helpGamePressed() {
		IBAnimation a = new BRunnableAnimation(10, new Runnable(){
			@Override
			public void run() {
				BPlatform.instance().game().canvas().setDrawable( new BExamplesField() );
			}
		});
		BPlatform.instance().game().animator().addAnimation(a);
	}
	
	protected void startOriginalPressed() {
		IBAnimation a = new BRunnableAnimation(10, new Runnable(){
			@Override
			public void run() {
				BBongardGameModel m = new BBongardGameModel();
				BFlippableContainer d = new BFlippableContainer( m );
				BPlatform.instance().game().canvas().setDrawable( d );
			}
		});
		BPlatform.instance().game().animator().addAnimation(a);
	}

	protected void helpOriginalPressed() {
		IBAnimation a = new BRunnableAnimation(10, new Runnable(){
			@Override
			public void run() {
				BPlatform.instance().game().canvas().setDrawable( new BGameHelp() );
			}
		});
		BPlatform.instance().game().animator().addAnimation(a);
	}
	
	@Override
	public BState save() {
		return null;
	}
}
