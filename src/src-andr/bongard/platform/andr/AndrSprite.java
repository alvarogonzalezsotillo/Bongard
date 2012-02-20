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

	private Paint _paint;

	public AndrSprite(IBRaster raster) {
		super(raster);
		_paint = new Paint();
	}

	@Override
	protected void draw_internal(IBCanvas c, IBTransform t) {
		AndrCanvas canvas = (AndrCanvas) c;
		Canvas ac = canvas.androidCanvas();
		Bitmap bitmap = ((AndrRaster)raster()).bitmap();
		ac.drawBitmap(bitmap, (Matrix)t, _paint);
	}

}
