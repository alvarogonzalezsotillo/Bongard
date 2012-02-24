package bongard.platform.andr;

import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import bongard.geom.IBRectangle;
import bongard.geom.IBTransform;
import bongard.gui.basic.BBox;
import bongard.gui.basic.IBCanvas;
import bongard.gui.basic.IBColor;

public class AndrBox extends BBox{

	public AndrBox(IBRectangle r, IBColor color) {
		super(r, color);
	}

	private Paint _paint;
	
	private Paint paint(){
		if (_paint == null) {
			_paint = new Paint();
		}
		return _paint;
	}
	
	@Override
	protected void setColor(IBColor color) {
		super.setColor(color);
		paint().setColor( ((AndrColor)color).color() );
	}
	
	@Override
	protected void draw_internal(IBCanvas c, IBTransform tr) {
		AndrCanvas canvas = (AndrCanvas) c;
		Canvas ac = canvas.androidCanvas();

		IBRectangle rect = originalSize();
		float l = (float) rect.x();
		float t = (float) rect.y();
		float r = (float) (l + rect.w());
		float b = (float) (t + rect.h());
		
		Matrix m = ac.getMatrix();
		ac.setMatrix((Matrix) tr);
		ac.drawRect(l, t, r, b, paint());
		ac.setMatrix(m);
	}

}
