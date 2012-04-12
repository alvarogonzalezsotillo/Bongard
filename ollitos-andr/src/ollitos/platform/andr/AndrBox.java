package ollitos.platform.andr;

import ollitos.geom.IBRectangle;
import ollitos.geom.IBTransform;
import ollitos.gui.basic.BBox;
import ollitos.gui.basic.IBCanvas;
import ollitos.gui.basic.IBColor;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;

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
		if( filled() ){
			ac.drawRect(l, t, r, b, paint());
		}
		else{
			float pts[] = {
					l, t, r, t,
					r, t, r, b,
					r, b, l, b,
					l, b, l, t
			};
			ac.drawLines(pts, paint());
		}
		ac.setMatrix(m);
	}

}
