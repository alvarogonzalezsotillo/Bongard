package ollitos.platform.andr;

import ollitos.geom.BRectangle;
import ollitos.geom.IBPoint;
import ollitos.geom.IBRectangle;
import ollitos.gui.basic.BSprite;
import ollitos.gui.event.IBEvent;
import ollitos.platform.BPlatform;
import ollitos.platform.BResourceLocator;
import ollitos.platform.BScreen;
import ollitos.platform.IBCanvas;
import ollitos.platform.IBRaster;
import ollitos.util.BTransformUtil;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;

public class AndrScreen extends BScreen implements IBCanvas{

	
	private class AndrView extends View {

		private class AndrListener implements OnTouchListener{

			private static final int CLICK_THRESHOLD = 20;
			private IBPoint _lastPointerDown;

			@Override
			public boolean onTouch(View v, MotionEvent e){
				
				BPlatform.instance().logger().log( e.toString() );
				final IBEvent ev = event( e );
				
				if( ev != null && ev.type() == IBEvent.Type.pointerDown ){
					_lastPointerDown = ev.point();
				}
				
				if( ev != null && ev.type() == IBEvent.Type.pointerUp ){
					double distance = BTransformUtil.distance(ev.point(), _lastPointerDown );
					BPlatform.instance().logger().log( this, "distance:" + distance );
					if( distance < CLICK_THRESHOLD ){
						BPlatform.instance().logger().log( this, "CLICK" );
						new Handler().post( new Runnable(){
							public void run() {
								IBEvent click = new IBEvent( IBEvent.Type.pointerClick, ev.point() );
								listeners().handle(click);
							}
						});
					}
					_lastPointerDown = null;
				}

				if( ev != null ){
					return listeners().handle(ev);
				}

				return false;
			}

			private IBEvent event(MotionEvent e) {
				int action = e.getAction();
				
				float x = e.getX();
				float y = e.getY();
				AndrPoint op = new AndrPoint(x, y);

				IBEvent.Type t = null;
				switch( action ){
					case MotionEvent.ACTION_DOWN: 
						t = IBEvent.Type.pointerDown;
						break;
					case MotionEvent.ACTION_UP:	
						t = IBEvent.Type.pointerUp;
						break;
					case MotionEvent.ACTION_MOVE: 
						t = IBEvent.Type.pointerDragged; 
						break;
				}
				if( t == null ){
					return null;
				}
				IBRectangle r = null;
				IBPoint p = transform().inverse().transform(op);
				return new IBEvent(t, p, r);
			}

		}

		private Object _lastOriginalSize;
		
		public AndrView(Context context) {
			super(context);
			setBackgroundColor(Color.BLUE);
			AndrListener al = new AndrListener();
			setOnTouchListener(al);
			setBackgroundColor( ((AndrColor)backgroundColor()).color() );
		}

		@Override
		protected void onDraw(Canvas canvas) {
			_currentAndroidCanvas = canvas;
			adjustTransformIfNecesary();
			try {
				super.onDraw(_currentAndroidCanvas);
				if (drawable() != null) {
					drawable().draw(canvas(), transform());
				}
				//drawTest();
			}
			finally {
				_currentAndroidCanvas = null;
			}
		}

		private void adjustTransformIfNecesary() {
			IBRectangle os = originalSize();
			if( !os.equals( _lastOriginalSize) ){
				adjustTransformToSize();
			}
		}

		@SuppressWarnings("unused")
		private void drawTest() {
			Paint paint = new Paint();
			paint.setColor(Color.WHITE);
			_currentAndroidCanvas.drawLine(0, 0, getMeasuredWidth(),getMeasuredHeight(), paint);
			_currentAndroidCanvas.drawCircle(getMeasuredWidth(), getMeasuredHeight(), 20, paint);

			BResourceLocator l = new BResourceLocator(
					"/images/backgrounds/arrecibo.png");
			IBRaster r = BPlatform.instance().raster(l, false);
			BSprite s = BPlatform.instance().sprite(r);

			s.draw(canvas(), null);
		}

		private Canvas _currentAndroidCanvas;

		public Canvas currentAndroidCanvas() {
			return _currentAndroidCanvas;
		}

		@Override
		protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
			int width = MeasureSpec.getSize(widthMeasureSpec);
			int height = MeasureSpec.getSize(heightMeasureSpec);
			setMeasuredDimension(width, height);
		}

	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			IBEvent e = new IBEvent( IBEvent.Type.back );
			return listeners().handle(e);
		}

		return false;
	}
	
	private AndrView _view;

	public AndrView view() {
		if (_view == null) {
			_view = new AndrView(context());
		}
		return _view;
	}

	public AndrView resetView() {
		_view = null;
		return view();
	}

	public Context context() {
		return AndrPlatform.context();
	}

	@Override
	public void refresh() {
		_view.invalidate();
	}

	@Override
	public IBRectangle originalSize() {
		View v = view();
		return new BRectangle(0, 0, v.getMeasuredWidth(), v.getMeasuredHeight());
	}

	public Canvas androidCanvas() {
		return _view.currentAndroidCanvas();
	}

	@Override
	public IBCanvas canvas() {
		return this;
	}

	@Override
	public void drawString(CanvasContextProvider c, String str, float x, float y) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void drawRaster(CanvasContextProvider c, IBRaster r, float x, float y) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void drawBox(CanvasContextProvider c, IBRectangle r, boolean filled) {
		// TODO Auto-generated method stub
		
	}
}
