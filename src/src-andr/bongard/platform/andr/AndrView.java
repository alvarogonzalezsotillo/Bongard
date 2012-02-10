package bongard.platform.andr;

import java.io.IOException;
import java.io.InputStream;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.View;

public class AndrView extends View{

	private Bitmap _bitmap;

	public AndrView(Context context) {
		super(context);
		Log.d("-", getMeasuredWidth() + "," + getMeasuredHeight() );
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		Paint paint = new Paint();
		paint.setColor(Color.WHITE);
		canvas.drawLine(0, 0, getMeasuredWidth(), getMeasuredHeight(), paint);
		Log.d("-", getMeasuredWidth() + "," + getMeasuredHeight() );
		Bitmap bitmap = bitmap();
		canvas.drawBitmap(bitmap, 0, 0, paint);
	}
	
	private Bitmap bitmap() {
		if (_bitmap == null) {
			try {
				_bitmap = createBitmap();
			} catch (IOException e) {
				e.printStackTrace();
				Log.d("-",e.toString());
			}
			
		}

		return _bitmap;
	}

	private Bitmap createBitmap() throws IOException {

		InputStream is = getContext().getAssets().open( "prueba.png" );
		
		return BitmapFactory.decodeStream(is);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		setMeasuredDimension(500, 500);
	}

}
