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

        private class HelpPage implements IBSlidablePage {

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
                IBRectangle htmlRectangle = new BRectangle(-320/2,-480/2,320,480);
                IBRasterProvider r = BRasterProviderCache.instance().getFromHTML(l, htmlRectangle);
                BSprite sprite = new BSprite(r);
                sprite.setAntialias(true);

                BDrawableContainer ret = new BDrawableContainer(htmlRectangle);
                ret.addDrawable(sprite);

                if( x == width()-1 ){
                   addButtonsOfLastPage(ret);
                }

                return ret;
            }

            private void addButtonsOfLastPage( BDrawableContainer cont ){
                IBRectangle os = cont.originalSize();

                IBRectangle buttonR = new BRectangle(0,0,6*os.w()/8,os.h()/10);


                BButton foundalisSiteButton = BHtmlButton.fromText("Web de Harry Foundalis", buttonR );
                BButton authorSiteButton = BHtmlButton.fromText("Web del autor", buttonR);

                BDrawableContainer ret = cont;

                foundalisSiteButton.transform().translate(0,2*os.h()/8);
                authorSiteButton.transform().translate(0,3*os.h()/8);



                ret.addDrawable(authorSiteButton);
                ret.addDrawable(foundalisSiteButton);
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

            _fd[x] = new HelpPage(x);

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

    public static class BHtmlButton{

        private static String tabletize(String s){
            String style = "margin-bottom: 0px; margin-top: 0px; margin-left: 0px; margin-right: 0px;" ;
            return "<body bgcolor=888888 style=\"" + style + "\"><table width=100% bgcolor=#ffffff border=1><tr><td valign=middle><center>" + s + "</center></td></tr></table></body>";
        }

        public static BButton fromText(String s, IBRectangle r){
            return fromHtml(tabletize(s), r );
        }

        private static BButton fromHtml(String html, IBRectangle r){
            r = IBRectangle.Util.centerAtOrigin(r);
            IBRasterProvider rp = BRasterProviderCache.instance().getFromHTML(html,r);
            BSprite sp = new BSprite(rp);
            sp.setAntialias(true);
            return new BButton( sp, r );
        }
    }
}
