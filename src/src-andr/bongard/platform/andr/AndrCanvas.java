package bongard.platform.andr;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.View;
import bongard.geom.BRectangle;
import bongard.geom.IBRectangle;
import bongard.gui.basic.BCanvas;
import bongard.platform.BFactory;


public class AndrCanvas extends BCanvas{

  private class AndrView extends View{

    private Bitmap _bitmap;

    public AndrView(Context context) {
      super(context);
      Log.d("-", getMeasuredWidth() + "," + getMeasuredHeight() );
      setBackgroundColor( Color.BLUE );
    }
    
    @Override
    protected void onDraw(Canvas canvas) {
      _currentAndroidCanvas = canvas;
      try{
        super.onDraw(_currentAndroidCanvas);
        adjustTransformToSize();
        if( drawable() != null ){
          drawable().draw(AndrCanvas.this, transform());
        }
        Paint paint = new Paint();
        paint.setColor(Color.WHITE);
        _currentAndroidCanvas.drawLine(0, 0, getMeasuredWidth(), getMeasuredHeight(), paint);
      }
      finally{
        _currentAndroidCanvas = null;
      }
    }
    private Canvas _currentAndroidCanvas;
    public Canvas currentAndroidCanvas(){
      return _currentAndroidCanvas;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
       int width = MeasureSpec.getSize(widthMeasureSpec);
       int height = MeasureSpec.getSize(heightMeasureSpec);
       setMeasuredDimension(width, height);		
    }

  }

	
	private AndrView _view;
	
	public AndrView view(){
		if( _view == null ){
			_view = new AndrView( context());
		}
		return _view;
	}
	
	public AndrView resetView(){
		_view = null;
		return view();
	}
	
	public Context context(){
		return AndrFactory.context();
	}

	@Override
	public void refresh() {
		_view.invalidate();
	}

	@Override
	public IBRectangle originalSize() {
		View v = view();
		return new BRectangle(0, 0, v.getMeasuredWidth(), v.getMeasuredHeight() );
	}

	public Canvas androidCanvas(){
		return _view.currentAndroidCanvas();
	}
}
