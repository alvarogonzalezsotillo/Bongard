package bongard.platform.andr;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import bongard.geom.BRectangle;
import bongard.geom.IBPoint;
import bongard.geom.IBRectangle;
import bongard.gui.basic.BCanvas;
import bongard.gui.basic.BSprite;
import bongard.gui.basic.IBRaster;
import bongard.gui.event.IBEvent;
import bongard.gui.event.IBEvent.Type;
import bongard.platform.BFactory;
import bongard.platform.BResourceLocator;

public class AndrCanvas extends BCanvas {

	private class AndrView extends View {

		private class AndrListener implements OnTouchListener{

			@Override
			public boolean onTouch(View v, MotionEvent e){
				
				IBEvent ev = event( e );
				
				if( ev != null ){
					return listeners().handle(ev);
				}
				return false;
			}

			private IBEvent event(MotionEvent e) {
				int id = e.getPointerId(0);
				int action = e.getAction();
				IBEvent.Type t = null;
				switch( action ){
					case MotionEvent.ACTION_DOWN: t = IBEvent.Type.pointerDown;
					case MotionEvent.ACTION_UP:	t = IBEvent.Type.pointerUp;
					case MotionEvent.ACTION_MOVE: t = IBEvent.Type.pointerDragged;
				}
				if( t == null ){
					return null;
				}
				IBRectangle r = null;
				float x = e.getX(id);
				float y = e.getY(id);
				AndrPoint op = new AndrPoint(x, y);
				IBPoint p = transform().inverse().transform(op);
				return new IBEvent(t, p, r);
			}
		}
		
		public AndrView(Context context) {
			super(context);
			Log.d("-", getMeasuredWidth() + "," + getMeasuredHeight());
			setBackgroundColor(Color.BLUE);
		}

		@Override
		protected void onDraw(Canvas canvas) {
			_currentAndroidCanvas = canvas;
			try {
				super.onDraw(_currentAndroidCanvas);
				adjustTransformToSize();
				if (drawable() != null) {
					drawable().draw(AndrCanvas.this, transform());
				}
				drawTest();
			}
			finally {
				_currentAndroidCanvas = null;
			}
		}

		private void drawTest() {
			Paint paint = new Paint();
			paint.setColor(Color.WHITE);
			_currentAndroidCanvas.drawLine(0, 0, getMeasuredWidth(),
					getMeasuredHeight(), paint);
			_currentAndroidCanvas.drawCircle(getMeasuredWidth(),
					getMeasuredHeight(), 20, paint);

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
