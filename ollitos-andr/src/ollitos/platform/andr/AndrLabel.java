package ollitos.platform.andr;

import ollitos.geom.IBTransform;
import ollitos.gui.basic.BLabel;
import ollitos.gui.basic.IBCanvas;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.Align;

public class AndrLabel extends BLabel {

	private Paint _paint = new Paint();

	public AndrLabel(String text) {
		super(text);
		_paint.setColor(Color.WHITE);
		_paint.setTextAlign(Align.LEFT);
	}

	@Override
	protected void draw_internal(IBCanvas c, IBTransform t) {
		AndrCanvas canvas = (AndrCanvas) c;
		Canvas ac = canvas.androidCanvas();
		Matrix m = ac.getMatrix();
		ac.setMatrix((Matrix) t);
		ac.drawText(text(), 0, 0, _paint);
		ac.setMatrix(m);
	}
}
