package bongard.platform.andr;

import android.content.Context;
import bongard.geom.IBRectangle;
import bongard.gui.basic.BCanvas;

public class AndrCanvas extends BCanvas{
	
	private AndrView _view;
	private Context _context;
	
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
		// TODO Auto-generated method stub
		
	}

	@Override
	public IBRectangle originalSize() {
		// TODO Auto-generated method stub
		return null;
	}

}
