package ollitos.platform.andr;

import java.net.URL;

import ollitos.geom.IBRectangle;
import ollitos.geom.IBTransform;
import ollitos.gui.basic.BHTMLDrawable;
import ollitos.gui.basic.IBCanvas;
import ollitos.platform.BFactory;
import android.graphics.Canvas;
import android.graphics.Picture;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebView.PictureListener;
import android.webkit.WebViewClient;

public class AndrHTMLDrawable extends BHTMLDrawable{

	private WebView _view;
	private boolean _ready;
	
	
	private WebView view(){
		if( _view == null ){
			initView();
		}
		
		return _view;
	}

	private void initView() {
		_view = new WebView(AndrFactory.context());
		_view.setWebViewClient( new WebViewClient(){
			@Override
			public void onPageFinished(WebView view, String url) {
				view.capturePicture();
			}
		});
		
		_view.setPictureListener(new PictureListener(){
			@Override
			public void onNewPicture(WebView view, Picture picture) {
				_ready = true;
				BFactory.instance().game().canvas().refresh();
			}
		});
		IBRectangle s = originalSize();
		int b = (int) (s.y() + s.h());
		int r = (int) (s.x() + s.w());
		int t = (int) s.y();
		int l = (int) s.x();
		_view.layout(l, t, r, b);
		if( html() != null ){
			_view.loadData( html(), "text/html", "UTF-8" );
		}
		else if( url() != null ){
			String u = BFactory.instance().platformURL(url()).toExternalForm();
			_view.loadUrl(u);
		}
	}
	
	@Override
	public void setUp() {
		
	}

	@Override
	public void dispose() {
		if( disposed() ){
			return;
		}
		_view.destroy();
		_view = null;
		_ready = false;
	}

	@Override
	public boolean disposed() {
		return _view == null;
	}

	@Override
	protected void draw_internal(IBCanvas c, IBTransform t) {
		View v = view();
		BFactory.instance().logger().log( this, "ready:" + ready( ) );
		AndrCanvas canvas = (AndrCanvas) c;
		Canvas ac = canvas.androidCanvas();
		ac.save();
		ac.setMatrix((AndrTransform)t);
		v.draw(ac);
		ac.restore();
	}

	public boolean ready() {
		return _ready;
	}

}
