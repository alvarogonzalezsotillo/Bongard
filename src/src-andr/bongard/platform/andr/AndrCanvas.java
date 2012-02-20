package bongard.platform.andr;

import android.content.Context;
import android.graphics.Canvas;
import android.view.View;
import bongard.geom.BRectangle;
import bongard.geom.IBRectangle;
import bongard.gui.basic.BCanvas;

public class AndrCanvas extends BCanvas{
	
	private AndrView _view;
	private Context _context;
	private Canvas _canvas;
	
	public AndrCanvas(Context c){
		_context = c;
	}

	public AndrView view(){
		if( _view == null ){
			_view = new AndrView( _context, this );
		}
		return _view;
	}

	@Override
	public void refresh() {
		_view.invalidate();
	}

	@Override
	public IBRectangle originalSize() {
		View v = view();
		return new BRectangle(v.getRight(), v.getTop(), v.getWidth(), v.getHeight() );
	}

	public void setAndroidCanvas(Canvas canvas) {
		_canvas = canvas;
	}
	
	public Canvas androidCanvas(){
		return _canvas;
	}
}
