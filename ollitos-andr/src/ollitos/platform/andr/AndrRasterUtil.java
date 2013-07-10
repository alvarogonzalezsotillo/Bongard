package ollitos.platform.andr;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLEncoder;

import android.util.Base64;
import android.webkit.WebSettings;
import ollitos.geom.IBRectangle;
import ollitos.platform.BPlatform;
import ollitos.platform.BResourceLocator;
import ollitos.platform.IBColor;
import ollitos.platform.IBRaster;
import ollitos.platform.raster.IBRasterUtil;
import ollitos.util.BException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Picture;
import android.view.View.MeasureSpec;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebView.PictureListener;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;

public class AndrRasterUtil implements IBRasterUtil{

	@Override
	public IBRaster extract(IBRectangle r, IBRaster i, IBColor color) {
		Bitmap src = ((AndrRaster)i).bitmap();
		Bitmap b = Bitmap.createBitmap(src, (int)r.x(), (int)r.y(), (int)r.w(), (int)r.h(), null, false );
		
		b = opaqueBitmap(b, ((AndrColor)color).color(), true );
		
		return new AndrRaster(b);
	}
	
	private Bitmap opaqueBitmap(Bitmap b, int color, boolean recycleOld) {
		
		Bitmap.Config config = b.getConfig();
		if( config == null ){
			config = defaultBitmapConfig();
		}
		Bitmap ret = b.copy(config, true);
		
		Canvas c = new Canvas(ret);
		Paint p = new Paint();
		p.setColor( Color.WHITE );
		c.drawRect(0, 0, ret.getWidth(), ret.getHeight(), p);
		c.drawBitmap(b, 0, 0, p);

		if( recycleOld ){
			b.recycle();
		}
		return ret;
	}

	@Override
	public IBRaster raster(InputStream is)	throws IOException {
		Bitmap b = BitmapFactory.decodeStream(is);
		is.close();
		if( b == null ){
			throw new BException("Cant open image", null);
		}

		return new AndrRaster(b);
	}

	private Bitmap.Config defaultBitmapConfig() {
		Bitmap.Config config;
		config = Bitmap.Config.ARGB_8888;
		return config;
	}

	@Override
	public AndrRaster raster(IBRectangle r) {
		Bitmap b = Bitmap.createBitmap((int)r.w(), (int)r.h(), defaultBitmapConfig() );
		return new AndrRaster(b);
	}

	@Override
	public AndrRaster html(IBRectangle s, BResourceLocator rl) throws BException {
        try{
            return html(s,rl,null);
        }
        catch( IOException e ){
            throw new BException(e.toString(),e);
        }

    }

    @Override
    public AndrRaster html(IBRectangle s, String str) throws BException {
        try{
            return html(s,null,str);
        }
        catch( IOException e ){
            throw new BException(e.toString(),e);
        }
    }

    private AndrRaster html(final IBRectangle s, final BResourceLocator rl, final String string) throws IOException {
		final AndrRaster ret = raster(s);
		final WebView webview = new WebView(AndrPlatform.context());
		webview.setWebViewClient( new WebViewClient(){
			@Override
			public void onPageFinished(WebView view, String url) {
				view.capturePicture();
			}
		});
		
		final Runnable runable = new Runnable(){
			private int _times = 4;
			private int DELAY = 1000;
			public void run() {
				Canvas canvas = new Canvas( ret.bitmap() );
				canvas.drawLine(0, 0, ret.bitmap().getWidth(), ret.bitmap().getHeight(), new Paint() );
				webview.draw(canvas);
				BPlatform.instance().game().screen().refresh();
				_times--;
				if( _times > 0 ){
					BPlatform.instance().game().animator().post(DELAY,this);
				}
				else{
					ViewGroup viewGroup = (ViewGroup)webview.getParent();
					if( viewGroup != null ){
						viewGroup.removeView(webview);
					}
				}
			};
		};
		
		webview.setPictureListener(new PictureListener(){
			@Override
			public void onNewPicture(WebView view, Picture picture) {
				BPlatform.instance().game().animator().post(runable);
			}
		});
		
		
		int b = (int) s.h();
		int r = (int) s.w();
		int t = 0;
		int l = 0;
		AndrScreen screen = (AndrScreen)AndrPlatform.instance().game().screen();
		FrameLayout v = screen.layout();
		v.addView(webview, new FrameLayout.LayoutParams(r, b) );
		screen.bringViewToFront();
		
//		view.setLayoutParams( new ViewGroup.LayoutParams(r, b));
//		view.measure(MeasureSpec.makeMeasureSpec(r, MeasureSpec.EXACTLY), 
//                 	 MeasureSpec.makeMeasureSpec(b, MeasureSpec.EXACTLY));
//		view.layout(l, t, r, b);

        webview.setHorizontalScrollBarEnabled(false);
        webview.setVerticalScrollBarEnabled(false);
        webview.getSettings().setDefaultZoom(WebSettings.ZoomDensity.MEDIUM);
        webview.setInitialScale(125);


        Runnable loadRunable = new Runnable(){
            @Override
            public void run() {
                if( rl != null ){
                    URL u = null;
                    if( rl.url() != null ){
                        u = rl.url();
                    }
                    if( u == null ){
                        u = BPlatform.instance().platformURL( rl );
                    }
                    String str = u.toExternalForm();
                    webview.loadUrl(str);
                }
                else if( string != null ){
                    try{
                        System.err.println( "HTML:" + string );
                        String encoded = URLEncoder.encode(string,"UTF-8");
                        System.err.println( "HTML:" + encoded );
                        encoded = encoded.replace('+',' ');
                        webview.loadData(encoded,"text/html",null);
                    }
                    catch( IOException ex ){
                        throw new BException( ex.toString(), ex );
                    }
                }
                else{
                    throw new IllegalArgumentException();
                }
            }
        };

        BPlatform.instance().game().animator().post(loadRunable);

		return ret;
	}

}
