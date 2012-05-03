package ollitos.platform.andr;

import android.graphics.Paint;
import android.graphics.Paint.Align;
import ollitos.platform.BCanvasContext;
import ollitos.platform.IBColor;

public class AndrCanvasContext extends BCanvasContext{
	private Paint _paint;
	
	@Override
	public void setColor(IBColor color) {
		super.setColor(color);
		updatePaint(); 
	}

	private void updatePaint() {
		Paint paint = paint();
		AndrColor color = (AndrColor)color();
		if( color != null ){
			paint.setColor( color.color() );
		}
		int flags = Paint.SUBPIXEL_TEXT_FLAG;
		if( antialias() ){
			flags |= Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG;
		}
		paint.setFlags(flags);
		paint().setAlpha((int) (alpha()*255));
		paint().setTextAlign(Align.LEFT);

	}
	
	@Override
	public void setAlpha(float alpha) {
		super.setAlpha(alpha);
		updatePaint();
	}
	
	@Override
	public void setAntialias(boolean antialias) {
		super.setAntialias(antialias);
		updatePaint();
	}

	public Paint paint() {
		if( _paint == null ){
			_paint = new Paint();
			updatePaint();
			
		}
		return _paint;
	}
}
