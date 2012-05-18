package ollitos.platform.andr;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import ollitos.geom.IBRectangle;
import ollitos.platform.BPlatform;
import ollitos.platform.BResourceLocator;
import ollitos.platform.IBRaster;
import ollitos.platform.IBRasterUtil;
import ollitos.util.BException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Picture;
import android.webkit.WebView;
import android.webkit.WebView.PictureListener;
import android.webkit.WebViewClient;

public class AndrRasterUtil implements IBRasterUtil{

	@Override
	public IBRaster extract(IBRectangle r, IBRaster i) {
		Bitmap src = ((AndrRaster)i).bitmap();
		Bitmap b = Bitmap.createBitmap(src, (int)r.x(), (int)r.y(), (int)r.w(), (int)r.h(), null, false );
		return new AndrRaster(b);
	}

	@Override
	public IBRaster raster(InputStream is, boolean transparent)	throws IOException {
		Bitmap b = BitmapFactory.decodeStream(is);
		is.close();
		if( b == null ){
			throw new BException("Cant open image", null);
		}
		if( transparent ){
			return new AndrRaster(b);
		}
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
		return new AndrRaster(ret);
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
		WebView view = new WebView(AndrPlatform.context());
		view.setWebViewClient( new WebViewClient(){
			@Override
			public void onPageFinished(WebView view, String url) {
				view.capturePicture();
			}
		});
		
		view.setPictureListener(new PictureListener(){
			@Override
			public void onNewPicture(WebView view, Picture picture) {
				Canvas canvas = new Canvas( ret.bitmap() );
				picture.draw( canvas );
				canvas.drawLine(0, 0, ret.bitmap().getWidth(), ret.bitmap().getHeight(), new Paint() );
				view.draw(canvas);
				BPlatform.instance().game().screen().refresh();
			}
		});
		int b = (int) (s.y() + s.h());
		int r = (int) (s.x() + s.w()*0.75);
		int t = (int) s.y();
		int l = (int) s.x();
		view.layout(l, t, r, b);
		URL u = null;
		if( rl.url() != null ){
			u = rl.url();
		}
		if( u == null ){
			u = BPlatform.instance().platformURL( rl );
		}
		String str = u.toExternalForm();
		view.loadUrl(str);
		
		return ret;
	}

}
