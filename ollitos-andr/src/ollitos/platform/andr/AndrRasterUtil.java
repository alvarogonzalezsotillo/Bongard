package ollitos.platform.andr;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import ollitos.geom.IBRectangle;
import ollitos.platform.BPlatform;
import ollitos.platform.BResourceLocator;
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
	public IBRaster extract(IBRectangle r, IBRaster i) {
		Bitmap src = ((AndrRaster)i).bitmap();
		Bitmap b = Bitmap.createBitmap(src, (int)r.x(), (int)r.y(), (int)r.w(), (int)r.h(), null, false );
		return new AndrRaster(b);
	}

	@Override
	public IBRaster raster(InputStream is)	throws IOException {
		Bitmap b = BitmapFactory.decodeStream(is);
		is.close();
		if( b == null ){
			throw new BException("Cant open image", null);
		}

		return new AndrRaster(b);
		/*
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

		b.recycle();
		return new AndrRaster(ret);
		*/
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
	public AndrRaster html(IBRectangle s, BResourceLocator rl) throws IOException {
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
		
		URL u = null;
		if( rl.url() != null ){
			u = rl.url();
		}
		if( u == null ){
			u = BPlatform.instance().platformURL( rl );
		}
		String str = u.toExternalForm();
		webview.loadUrl(str);
		
		BPlatform.instance().game().animator().addAnimation( new BProgressAnimation(ret) );
		
		return ret;
	}

}
