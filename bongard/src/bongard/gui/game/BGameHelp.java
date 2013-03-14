package bongard.gui.game;

import ollitos.bot.map.BTestRoomReader;
import ollitos.bot.view.isoview.BIsoView;
import ollitos.geom.BRectangle;
import ollitos.geom.IBRectangle;
import ollitos.gui.basic.BButton;
import ollitos.gui.basic.BSprite;
import ollitos.gui.basic.IBDrawable;
import ollitos.gui.container.BSlidableContainer;
import ollitos.gui.container.IBSlidableModel;
import ollitos.gui.container.IBSlidablePage;
import ollitos.platform.BResourceLocator;
import ollitos.platform.IBCanvas;
import ollitos.platform.raster.BRasterProviderCache;
import ollitos.platform.raster.IBRasterProvider;
import ollitos.platform.state.BState;



public class BGameHelp extends BSlidableContainer implements BState.Stateful{

	public BGameHelp(){
		super(BGameField.computeOriginalSize(),createModel());
	}
	
	private static IBSlidableModel createModel() {
		return new Model();
	}
	
	private static class Model implements IBSlidableModel{
			
		private final class SlidablePageForIsoView implements	IBSlidablePage {
			private IBDrawable _drawable;
			private String[][] _roomData;
			
			public SlidablePageForIsoView(String[][] roomData){
				_roomData = roomData;
			}

			@Override
			public void setUp() {
			}

			@Override
			public boolean disposed() {
				return false;
			}

			@Override
			public void dispose() {
			}

			@Override
			public IBDrawable drawable() {
				if( _drawable != null ){
					return _drawable;
				}
				_drawable = new BIsoView(_roomData);
				return _drawable;
			}

			@Override
			public IBDrawable icon() {
				return null;
			}
		}

		private IBSlidablePage _fd[];
		
		public Model(){
			_fd = new IBSlidablePage[width()];
		}

		@Override
		public int width(){
			return 2;
		}
		
		public static IBDrawable internal(){
			BResourceLocator l = new BResourceLocator("/examples/help.html" );
			IBRectangle htmlRectangle = new BRectangle(0, 0, 240, 320);
			IBRasterProvider r = BRasterProviderCache.instance().getFromHTML(l, htmlRectangle);
			BSprite sprite = new BSprite(r);
			sprite.setAntialias(true);
			BButton b = new BButton(sprite);
			return b;
		}
		
		@Override
		public IBSlidablePage page(int x) {
			if( _fd[x] != null ){
				return _fd[x];
			}
			
			if( x == 0 ){
				_fd[x] = new SlidablePageForIsoView(BTestRoomReader.BIGROOM);
			}
			else{
				_fd[x] = new IBSlidablePage() {
					
					private IBDrawable _d;
	
					@Override
					public void setUp() {
					}
					
					@Override
					public boolean disposed() {
						return false;
					}
					
					@Override
					public void dispose() {
					}
					
					@Override
					public IBDrawable drawable() {
						if( _d == null ){
							_d = internal();
						}
						return _d;
					}
					
					@Override
					public IBDrawable icon() {
						return null;
					}
				};
			}
			
			return _fd[x];
		}
		
		@Override
		public void dispose(int x) {
		}
		
		@Override
		public IBDrawable background() {
			return null;
		}
	};
			
	@Override
	protected void draw_internal(IBCanvas c) {
		super.draw_internal(c);
		c.drawBox(this, originalSize(), false);
	}
	
	@SuppressWarnings("serial")
	private static class MyState extends BState<BGameHelp>{
		@Override
		public BGameHelp create() {
			return new BGameHelp();
		}
	}
	
	@Override
	public BState save() {
		return new MyState();
	}
}
