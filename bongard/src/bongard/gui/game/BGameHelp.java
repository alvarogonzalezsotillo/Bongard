package bongard.gui.game;

import ollitos.bot.map.BTestRoomReader;
import ollitos.bot.map.IBMapReader;
import ollitos.bot.map.bsh.BBeanShellMapReader;
import ollitos.bot.view.isoview.BIsoView;
import ollitos.geom.BRectangle;
import ollitos.geom.IBRectangle;
import ollitos.gui.basic.BButton;
import ollitos.gui.basic.BSprite;
import ollitos.gui.basic.IBDrawable;
import ollitos.gui.container.BSlidableContainer;
import ollitos.gui.container.BZoomDrawable;
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
			
		private final class SlidablePageForIsoView implements IBSlidablePage {
			private IBDrawable _drawable;
			private IBMapReader _mapReader;
			
			public SlidablePageForIsoView(IBMapReader mapReader){
				_mapReader = mapReader;
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
				_drawable = new BZoomDrawable( new BIsoView(_mapReader) );
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
			return 5;
		}
		
		public static IBDrawable internal(int x){
            String locator = String.format("/examples/help%02d.html", x);
			BResourceLocator l = new BResourceLocator(locator );
            IBRectangle os = BGameField.computeOriginalSize();
            IBRectangle.Util.scale(os,os,0.8);
            IBRectangle htmlRectangle = new BRectangle(0,0,320,480);
			IBRasterProvider r = BRasterProviderCache.instance().getFromHTML(l, htmlRectangle);
			BSprite sprite = new BSprite(r);
			sprite.setAntialias(true);
			return sprite;
		}
		
		@Override
		public IBSlidablePage page(final int x) {
			if( _fd[x] != null ){
				return _fd[x];
			}
			
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
                        _d = internal(x);
                    }
                    return _d;
                }

                @Override
                public IBDrawable icon() {
                    return null;
                }
            };

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
