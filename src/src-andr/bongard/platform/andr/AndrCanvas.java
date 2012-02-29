package bongard.platform.andr;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import bongard.geom.BRectangle;
import bongard.geom.IBPoint;
import bongard.geom.IBRectangle;
import bongard.gui.basic.BCanvas;
import bongard.gui.basic.BSprite;
import bongard.gui.basic.IBRaster;
import bongard.gui.event.IBEvent;
import bongard.platform.BFactory;
import bongard.platform.BResourceLocator;
import bongard.util.BTransformUtil;

public class AndrCanvas extends BCanvas {

	private class AndrView extends View {

		private class AndrListener implements OnTouchListener{

			private static final int CLICK_THRESHOLD = 20;
			private IBPoint _lastPointerDown;

			@Override
			public boolean onTouch(View v, MotionEvent e){
				
				BFactory.instance().logger().log( e.toString() );
				final IBEvent ev = event( e );
				
				if( ev != null && ev.type() == IBEvent.Type.pointerDown ){
					_lastPointerDown = ev.point();
				}
				
				if( ev != null && ev.type() == IBEvent.Type.pointerUp ){
					double distance = BTransformUtil.distance(ev.point(), _lastPointerDown );
					BFactory.instance().logger().log( this, "distance:" + distance );
					if( distance < CLICK_THRESHOLD ){
						BFactory.instance().logger().log( this, "CLICK" );
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
				int id = e.getPointerId(0);
				int action = e.getAction();
				
				float x = e.getX(id);
				float y = e.getY(id);
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
					drawable().draw(AndrCanvas.this, transform());
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
			IBRaster r = BFactory.instance().raster(l, false);
			BSprite s = BFactory.instance().sprite(r);

			s.draw(AndrCanvas.this, null);
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
		return AndrFactory.context();
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
}
