package bongard.gui.game;

import ollitos.bot.map.IBMapReader;
import ollitos.bot.view.isoview.BIsoView;
import ollitos.geom.BRectangle;
import ollitos.geom.IBRectangle;
import ollitos.gui.basic.BButton;
import ollitos.gui.basic.BLabel;
import ollitos.gui.basic.BSprite;
import ollitos.gui.basic.IBDrawable;
import ollitos.gui.container.*;
import ollitos.platform.BPlatform;
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

        private static class HelpPage implements IBSlidablePage {

            private final int _x;
            private IBDrawable _d;
            private IBDrawable _icon;

            public HelpPage(int x) {
                _x = x;
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
                if( _d == null ){
                    _d = internal(_x);
                }
                return _d;
            }

            @Override
            public IBDrawable icon() {
                if (_icon == null) {
                    _icon = new BLabel(String.valueOf(_x+1)){
                        @Override
                        public IBRectangle originalSize() {
                            return new BRectangle(-2,-2,4,4);
                        }
                    };
                }
                return _icon;
            }

            public IBDrawable internal(int x){
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

        }

        private static class AboutPage extends HelpPage{

            public AboutPage(int x) {
                super(x);
            }

            private String tabletize(String s){
                return "<body bgcolor=888888><table width=100% bgcolor=#ffffff border=1><tr><td valign=middle><center>" + s + "</center></td></tr></table></body>";
            }

            @Override
            public IBDrawable internal(int x){
                BRectangle os = BGameField.computeOriginalSize();

                BLabel authorL = new BLabel("Sitio web del autor");
                BButton authorSiteButton = new BButton(authorL, IBRectangle.Util.centerAtOrigin(authorL.originalSize()) );

                IBRectangle foundalisR = new BRectangle(os.w()/8,2*os.h()/3,6*os.w()/8,os.h()/8);

                IBRasterProvider rp = BRasterProviderCache.instance().getFromHTML(tabletize("Sitio de Harry Foundalis"),foundalisR);
                BSprite foundalisL = new BSprite(rp);
                foundalisL.setAntialias(true);
                BButton foundalisSiteButton = new BButton(foundalisL, IBRectangle.Util.centerAtOrigin(foundalisL.originalSize()) );

                BDrawableContainer ret = new BDrawableContainer(os);

                authorSiteButton.transform().translate(os.w()/2,os.h()/3);
                foundalisSiteButton.transform().translate(os.w()/2,2*os.h()/3);



                ret.addDrawable(authorSiteButton);
                ret.addDrawable(foundalisSiteButton);

                return ret;
            }
        }

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

		private transient IBSlidablePage _fd[];
		
		public Model(){
			_fd = new IBSlidablePage[width()];
		}

		@Override
		public int width(){
			return 7;
		}
		

		@Override
		public IBSlidablePage page(final int x) {
			if( _fd[x] != null ){
				return _fd[x];
			}

            if( x < 6 ){
                _fd[x] = new HelpPage(x);
            }
            else{
                _fd[x] = new AboutPage(x);
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
		//c.drawBox(this, originalSize(), false);
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
