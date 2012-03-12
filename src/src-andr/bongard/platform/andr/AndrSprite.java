package bongard.platform.andr;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import bongard.geom.IBTransform;
import bongard.gui.basic.BSprite;
import bongard.gui.basic.IBCanvas;
import bongard.gui.basic.IBRaster;

public class AndrSprite extends BSprite {

	
	private Paint _paintA;
	private Paint _paint;

	public AndrSprite(IBRaster raster) {
		super(raster);
	}
	
	private Paint paint(){
		if (_paintA == null) {
			_paintA = new Paint(Paint.FILTER_BITMAP_FLAG);
		}
		if (_paint == null) {
			_paint = new Paint();
		}
		return antialias() ? _paintA : _paint;
	}

	@Override
	protected void draw_internal(IBCanvas c, IBTransform t) {
		AndrCanvas canvas = (AndrCanvas) c;
		Canvas ac = canvas.androidCanvas();
		Bitmap bitmap = ((AndrRaster)raster()).bitmap();

		int x = -bitmap.getWidth()/2;
		int y = -bitmap.getHeight()/2;

		Matrix m = new Matrix((Matrix) t);
		m.preTranslate(x, y);
		
		paint().setAlpha((int) (alpha()*255));
		ac.drawBitmap(bitmap, m, paint());
	}

}
