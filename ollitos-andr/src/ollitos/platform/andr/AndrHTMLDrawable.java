package ollitos.platform.andr;

import ollitos.geom.IBRectangle;
import ollitos.geom.IBTransform;
import ollitos.gui.basic.BHTMLDrawable;
import ollitos.gui.basic.IBCanvas;
import ollitos.platform.BFactory;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class AndrHTMLDrawable extends BHTMLDrawable{

	private WebView _view;
	
	private WebView view(){
		if( _view == null ){
			_view = new WebView(AndrFactory.context());
		}
		_view.setWebViewClient( new WebViewClient(){
			@Override
			public void onPageFinished(WebView view, String url) {
				BFactory.instance().game().canvas().refresh();
			}
		});
		IBRectangle s = originalSize();
		int b = (int) (s.y() + s.h());
		int r = (int) (s.x() + s.w());
		int t = (int) s.y();
		int l = (int) s.x();
		_view.layout(l, t, r, b);
		_view.loadData( html(), "text/html", "UTF-8" );
		return _view;
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
	}

	@Override
	public boolean disposed() {
		return _view == null;
	}

	@Override
	protected void draw_internal(IBCanvas c, IBTransform t) {
		View v = view();
		AndrCanvas canvas = (AndrCanvas) c;
		Canvas ac = canvas.androidCanvas();
		ac.save();
		ac.setMatrix((AndrTransform)t);
		v.draw(ac);
		ac.restore();
	}

}
