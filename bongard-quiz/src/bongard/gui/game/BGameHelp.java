package bongard.gui.game;

import ollitos.geom.BRectangle;
import ollitos.geom.IBRectangle;
import ollitos.gui.basic.*;
import ollitos.gui.container.*;
import ollitos.platform.BPlatform;
import ollitos.platform.BResourceLocator;
import ollitos.platform.IBCanvas;
import ollitos.platform.raster.BRasterProviderCache;
import ollitos.platform.raster.IBRasterProvider;
import ollitos.platform.state.BState;
import ollitos.util.BException;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Properties;


public class BGameHelp extends BSlidableContainer implements BState.Stateful{

	public BGameHelp(){
		super(BGameField.computeOriginalSize(),createModel());
	}
	
	private static IBSlidableModel createModel() {
		return new HelpModel();
	}
	
	private static class HelpModel implements IBSlidableModel{

        private class HelpPage implements IBSlidablePage {

            private final int _x;
            private IBDrawable _d;
            private IBDrawable _icon;
            private IBRasterProvider _r;

            public HelpPage(int x) {
                _x = x;
            }

            @Override
            public void setUp() {
            }

            @Override
            public boolean disposed() {
                return _d == null;
            }

            @Override
            public void dispose() {
                _d = null;
                if( _r != null ){
                    _r.dispose();
                    _r = null;
                }
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
                String locator = String.format("/help/help%02d.html", x);
                BResourceLocator l = new BResourceLocator(locator);
                l = BResourceLocator.localizedResource(l);
                int w = 320*2;
                int h = 480*2;
                IBRectangle rect = new BRectangle(-w/2,-h/2,w,h);
                _r = BRasterProviderCache.instance().getFromHTML(l, rect);
                BSprite sprite = new BSprite(_r);
                sprite.setAntialias(true);
                sprite.setNotAvailableColor(BPlatform.COLOR_DARKGRAY);
                sprite.setNotAvailableBorderColor(BPlatform.COLOR_DARKGRAY);

                BDrawableContainer ret = new BDrawableContainer(rect);

                ret.addDrawable(new BZoomDrawable(sprite));

                if( x == width()-1 ){
                   addButtonsOfLastPage(ret);
                }

                return ret;
            }

            private void addButtonsOfLastPage( BDrawableContainer cont ){
                IBRectangle os = cont.originalSize();

                IBRectangle buttonR = new BRectangle(0,0,6*os.w()/16,os.h()/20);

                String locator = "/help/help.properties";
                BResourceLocator l = new BResourceLocator(locator);
                l = BResourceLocator.localizedResource(l);
                Properties props = new Properties();
                try {
                    props.load( BPlatform.instance().open(l) );
                }
                catch (IOException e) {
                    throw new BException( e.toString(), e );
                }

                BButton foundalisSiteButton = BHtmlButton.fromText(props.getProperty("harryFoundalisWeb"), buttonR );
                BButton authorSiteButton = BHtmlButton.fromText(props.getProperty("authorWeb"), buttonR);

                foundalisSiteButton.addClickedListener(new URLListener("http://www.foundalis.com/res/diss_research.html") );
                authorSiteButton.addClickedListener(new URLListener("http://sites.google.com/site/alvarogonzalezsotillo") );

                BDrawableContainer ret = cont;

                foundalisSiteButton.transform().translate(0,2*os.h()/8);
                foundalisSiteButton.transform().scale(2,2);

                authorSiteButton.transform().translate(0,3*os.h()/8);
                authorSiteButton.transform().scale(2,2);


                ret.addDrawable(authorSiteButton);
                ret.addDrawable(foundalisSiteButton);
            }

            private class URLListener implements BButton.ClickedListener {

                private final String _url;

                public URLListener( String url ){
                    _url = url;
                }
                @Override
                public void clicked(BButton b) {
                    URL u = null;
                    try {
                        u = new URL(_url);
                        BPlatform.instance().openInExternalApplication( new BResourceLocator(u) );
                    }
                    catch (MalformedURLException e) {
                        throw new BException( e.toString(), e );
                    }
                }

                @Override
                public void pressed(BButton b) {
                }

                @Override
                public void released(BButton b) {
                }
            }
        }

        private transient IBSlidablePage _fd[];
		
		public HelpModel(){
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
        private int _index;

        public MyState(int current){
            _index = current;
        }

		@Override
		public BGameHelp create() {
            BGameHelp ret = new BGameHelp();
            ret.setCurrent(_index);
            return ret;
		}
	}
	
	@Override
	public BState save() {
		return new MyState( currentIndex() );
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
