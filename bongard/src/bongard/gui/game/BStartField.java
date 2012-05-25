package bongard.gui.game;

import ollitos.animation.BRunnableAnimation;
import ollitos.animation.IBAnimation;
import ollitos.geom.BRectangle;
import ollitos.geom.IBRectangle;
import ollitos.geom.IBTransform;
import ollitos.gui.basic.BButton;
import ollitos.gui.basic.BButton.ClickedListener;
import ollitos.gui.basic.BSprite;
import ollitos.gui.container.BDrawableContainer;
import ollitos.gui.container.BFlippableContainer;
import ollitos.gui.event.BRestartListener;
import ollitos.platform.BPlatform;
import ollitos.platform.BResourceLocator;
import ollitos.platform.BState;
import ollitos.platform.IBCanvas;
import ollitos.platform.IBRaster;
import ollitos.util.BTransformUtil;

public class BStartField extends BDrawableContainer{

	private BButton _helpGameSprite;
	private BButton _startGameSprite;
	private BButton _helpOriginalSprite;
	private BButton _startOriginalSprite;
	private ClickedListener _clickedListener = new ClickedListener() {
		@Override
		public void clicked(BButton b) {
			BStartField.this.clicked(b);
		}
	};

	@Override
	public IBRectangle originalSize() {
		return BGameField.computeOriginalSize();
	}
	

	private IBRectangle spritePosition(int row, int column, double width){
		IBRectangle s = originalSize();
		double x = s.x() + width/2 + (1+column*2)*(s.w())/5;
		double y = s.y() + width/2 + (1+row*2)*(s.h())/5;
		
		return new BRectangle(x-width/2, y-width/2,width, width);
	}
	
	private BButton createButton(int row, int column, String resource ){
		BPlatform f = platform();
		IBRaster ss = f.raster( new BResourceLocator(resource), false );
		BSprite s = new BSprite(ss);
		s.setAntialias(true);
		BButton ret = new BButton( s );
		ret.setClickedListener(_clickedListener );
		IBRectangle r = spritePosition(row,column, ss.w() );
		ret.setSizeTo(r, false, true);
		ret.install(this);
		return ret;
	}
	
	public BStartField() {
		_startGameSprite = createButton(0,0,"/images/start/startGame.png");
		_helpGameSprite = createButton(0,1,"/images/start/helpGame.png");
		_startOriginalSprite = createButton(1,0,"/images/start/startOriginal.png");
		_helpOriginalSprite = createButton(1,1,"/images/start/helpOriginal.png");
		
		BRestartListener.install();
	}


	protected void clicked(BButton b) {
		if( _helpGameSprite == b ){
			helpGamePressed();
		}

		if( _startGameSprite == b ){
			startGamePressed();
		}

		if( _helpOriginalSprite == b ){
			helpOriginalPressed();
		}

		if( _startOriginalSprite == b ){
			startOriginalPressed();
		}
	}
	
	@Override
	protected void draw_internal(IBCanvas c) {
		IBTransform t = canvasContext().transform();
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
		platform().game().animator().addAnimation(a);
	}

	protected void helpGamePressed() {
		IBAnimation a = new BRunnableAnimation(10, new Runnable(){
			@Override
			public void run() {
				platform().game().screen().setDrawable( new BExamplesField() );
			}
		});
		platform().game().animator().addAnimation(a);
	}
	
	protected void startOriginalPressed() {
		IBAnimation a = new BRunnableAnimation(10, new Runnable(){
			@Override
			public void run() {
				BBongardGameModel m = new BBongardGameModel();
				BFlippableContainer d = new BFlippableContainer( m );
				platform().game().screen().setDrawable( d );
			}
		});
		platform().game().animator().addAnimation(a);
	}

	protected void helpOriginalPressed() {
		IBAnimation a = new BRunnableAnimation(10, new Runnable(){
			@Override
			public void run() {
				platform().game().screen().setDrawable( new BGameHelp() );
			}
		});
		platform().game().animator().addAnimation(a);
	}
	
	@Override
	public BState save() {
		return null;
	}
}
