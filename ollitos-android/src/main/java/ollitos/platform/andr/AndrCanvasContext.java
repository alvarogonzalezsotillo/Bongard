package ollitos.platform.andr;

import ollitos.platform.BCanvasContext;
import ollitos.platform.IBCanvas;
import ollitos.platform.IBColor;
import android.graphics.Paint;
import android.graphics.Paint.Align;

public class AndrCanvasContext extends BCanvasContext{
	private Paint _paint;
	
	@Override
	public void setColor(IBColor color) {
		super.setColor(color);
		updatePaint(); 
	}

	public static void updatePaint( Paint paint, IBCanvas.CanvasContext cc ){
		AndrColor color = (AndrColor)cc.color();
		if( color != null ){
			paint.setColor( color.color() );
		}
		int flags = Paint.SUBPIXEL_TEXT_FLAG;
		if( cc.antialias() ){
			flags |= Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG;
		}
		paint.setFlags(flags);
		paint.setAlpha((int) (cc.alpha()*255));
		paint.setTextAlign(Align.LEFT);
	}
	
	private void updatePaint() {
		Paint paint = paint();
		
		updatePaint( paint, this );
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
