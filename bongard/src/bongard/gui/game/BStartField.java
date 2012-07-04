package bongard.gui.game;

import ollitos.animation.BRunnableAnimation;
import ollitos.animation.IBAnimation;
import ollitos.geom.BRectangle;
import ollitos.geom.IBRectangle;
import ollitos.gui.basic.BButton;
import ollitos.gui.basic.BButton.ClickedListener;
import ollitos.gui.basic.BCheckBox;
import ollitos.gui.basic.BSprite;
import ollitos.gui.basic.IBDrawable;
import ollitos.gui.container.BDrawableContainer;
import ollitos.gui.container.BSlidableContainer;
import ollitos.gui.event.BRestartListener;
import ollitos.gui.event.BZoomIntoDetailListener;
import ollitos.platform.BPlatform;
import ollitos.platform.BResourceLocator;
import ollitos.platform.BState;
import ollitos.platform.IBRaster;

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
		BButton ret = BButton.create(resource);
		ret.addClickedListener(_clickedListener );
		IBRectangle r = spritePosition(row,column, ret.drawable().originalSize().w() );
		ret.setSizeTo(r, false, true);
		return ret;
	}
	
	public BStartField() {
		super(BGameField.computeOriginalSize());
		_startGameSprite = createButton(0,0,"/images/start/startGame.png");
		_helpGameSprite = createButton(0,1,"/images/start/helpGame.png");
		_startOriginalSprite = createButton(1,0,"/images/start/startOriginal.png");
		_helpOriginalSprite = createButton(1,1,"/images/start/helpOriginal.png");
		
		addDrawable(_startGameSprite);
		addDrawable(_helpGameSprite);
		addDrawable(_startOriginalSprite);
		addDrawable(_helpOriginalSprite);
		
		
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
	
	protected void startGamePressed() {
		final IBDrawable d = BGameModel.goToInitialLevel();
		IBAnimation a = new BRunnableAnimation(10, new Runnable(){
			@Override
			public void run() {
				platform().game().screen().setDrawable(d);
			}
		});
		platform().game().animator().addAnimation(a);
	}

	protected void helpGamePressed() {
		final BExamplesField d = new BExamplesField();
		IBAnimation a = new BRunnableAnimation(10, new Runnable(){
			@Override
			public void run() {
				platform().game().screen().setDrawable( d );
			}
		});
		platform().game().animator().addAnimation(a);
	}
	
	protected void startOriginalPressed() {
		BBongardGameModel m = new BBongardGameModel();
		final BSlidableContainer d = new BSlidableContainer( BGameField.computeOriginalSize(), m );
		IBAnimation a = new BRunnableAnimation(10, new Runnable(){
			@Override
			public void run() {
				platform().game().screen().setDrawable( d );
			}
		});
		platform().game().animator().addAnimation(a);
	}

	protected void helpOriginalPressed() {
		final BGameHelp d = new BGameHelp();
		IBAnimation a = new BRunnableAnimation(10, new Runnable(){
			@Override
			public void run() {
				platform().game().screen().setDrawable( d );
			}
		});
		platform().game().animator().addAnimation(a);
	}
	
	@Override
	public BState save() {
		return null;
	}
}
