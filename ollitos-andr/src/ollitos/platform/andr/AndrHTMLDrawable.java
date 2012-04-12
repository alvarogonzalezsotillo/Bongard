package ollitos.platform.andr;

import ollitos.geom.IBRectangle;
import ollitos.geom.IBTransform;
import ollitos.gui.basic.BHTMLDrawable;
import ollitos.gui.basic.IBCanvas;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.view.View;
import android.webkit.WebView;

public class AndrHTMLDrawable extends BHTMLDrawable{

	private WebView _view;
	
	private WebView view(){
		if( _view != null ){
			IBRectangle s = originalSize();
			int b = (int) (s.y() + s.h());
			int r = (int) (s.x() + s.w());
			int t = (int) s.y();
			int l = (int) s.x();
			_view.layout(l, t, r, b);
			return _view;
		}
		
		_view = new WebView(AndrFactory.context());
		
		return _view;

	}
	
	@Override
	public void setUp() {
		
	}

	@Override
	public void dispose() {
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
