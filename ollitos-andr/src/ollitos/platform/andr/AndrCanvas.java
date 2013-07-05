package ollitos.platform.andr;

import android.graphics.*;
import ollitos.geom.BRectangle;
import ollitos.geom.IBRectangle;
import ollitos.platform.IBCanvas;
import ollitos.platform.IBRaster;
import ollitos.platform.raster.IBRasterProvider;

public class AndrCanvas implements IBCanvas{
	
	private Canvas _androidCanvas;
	private Matrix _oldMatrix;
	private Paint _paint = new Paint();
	
	public AndrCanvas(Canvas androidCanvas){
		if( androidCanvas == null ){
			throw new IllegalArgumentException();
		}
		_androidCanvas = androidCanvas;
	}

	public Canvas androidCanvas(){
		return _androidCanvas;
	}
	
	
	private Canvas save( CanvasContext cc ){
		Canvas ret = androidCanvas();
		
		_oldMatrix = ret.getMatrix();
		
		ret.setMatrix( (Matrix) cc.transform() );
		
		return ret;
	}
	
	private void restore(){
		Canvas ac = androidCanvas();
		ac.setMatrix(_oldMatrix);
	}
	
	@Override
	public void drawString(CanvasContextHolder c, String str, float x, float y) {
		CanvasContext canvasContext = c.canvasContext();
		Canvas ac = save(canvasContext);
		
		ac.drawText(str, x, y, paint(canvasContext));
		
		restore();
	}

	@Override
	public void drawRaster(CanvasContextHolder c, IBRaster r, float x, float y) {
		CanvasContext canvasContext = c.canvasContext();
		Canvas ac = save(canvasContext);
		
		Bitmap bitmap = ((AndrRaster)r).bitmap();

		ac.drawBitmap(bitmap, x, y, paint(canvasContext) );

		
		restore();
	}

	private Paint paint(CanvasContext canvasContext) {
		if( canvasContext instanceof AndrCanvasContext ){
			return ((AndrCanvasContext)canvasContext).paint();
		}
		
		AndrCanvasContext.updatePaint(_paint, canvasContext);
		return _paint;
	}

	@Override
	public void drawBox(CanvasContextHolder c, IBRectangle rect, boolean filled) {
		CanvasContext canvasContext = c.canvasContext();
		Canvas ac = save(canvasContext);

		float l = (float) rect.x();
		float t = (float) rect.y();
		float r = (float) (l + rect.w());
		float b = (float) (t + rect.h());
		
		if( filled ){
			ac.drawRect(l, t, r, b, paint(canvasContext));
		}
		else{
			float pts[] = {
					l, t, r, t,
					r, t, r, b,
					r, b, l, b,
					l, b, l, t
			};
			ac.drawLines(pts, paint(canvasContext));
		}
		
		restore();
	}

	@Override
	public void drawLine(CanvasContextHolder c, float x1, float y1, float x2, float y2) {
		CanvasContext canvasContext = c.canvasContext();
		Canvas ac = save(canvasContext);

		ac.drawLine(x1, y1, x2, y2, paint(canvasContext) );
		
		restore();
		
	}

    @Override
    public IBRectangle stringBounds(CanvasContextHolder c, String s, IBRectangle dst) {
        Paint paint = paint(c.canvasContext());
        Rect bounds = new Rect();
        paint.getTextBounds(s,0,s.length(),bounds);

        if( dst == null ){
            dst = new BRectangle();
        }

        if( dst instanceof BRectangle ){
            BRectangle ret = (BRectangle) dst;
            ret.set(bounds.left,bounds.top,bounds.width(),bounds.height());
        }
        else{
            throw new IllegalArgumentException();
        }
        return dst;
    }
}
