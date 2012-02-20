package bongard.platform.andr;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import bongard.geom.IBTransform;
import bongard.gui.basic.BLabel;
import bongard.gui.basic.IBCanvas;

public class AndrLabel extends BLabel {

	private Paint _paint = new Paint();

	public AndrLabel(String text) {
		super(text);
		_paint.setColor(Color.BLUE);
		_paint.setTextAlign(Align.LEFT);
	}

	@Override
	protected void draw_internal(IBCanvas c, IBTransform t) {
		AndrCanvas canvas = (AndrCanvas) c;
		Canvas ac = canvas.androidCanvas();
		ac.setMatrix((Matrix) t);
		ac.drawText(text(), 0, 0, _paint);
	}
}
