package ollitos.platform.andr;

import ollitos.geom.IBRectangle;
import ollitos.platform.IBCanvas;
import ollitos.platform.IBRaster;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;

public class AndrCanvas implements IBCanvas{
	
	private Canvas _androidCanvas;
	private Matrix _oldMatrix;
	
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
		AndrCanvasContext canvasContext = (AndrCanvasContext)c.canvasContext();
		Canvas ac = save(canvasContext);
		
		ac.drawText(str, x, y, canvasContext.paint());
		
		restore();
	}

	@Override
	public void drawRaster(CanvasContextHolder c, IBRaster r, float x, float y) {
		AndrCanvasContext canvasContext = (AndrCanvasContext)c.canvasContext();
		Canvas ac = save(canvasContext);
		
		Bitmap bitmap = ((AndrRaster)r).bitmap();

		ac.drawBitmap(bitmap, x, y, canvasContext.paint() );

		
		restore();
	}

	@Override
	public void drawBox(CanvasContextHolder c, IBRectangle rect, boolean filled) {
		AndrCanvasContext canvasContext = (AndrCanvasContext)c.canvasContext();
		Canvas ac = save(canvasContext);

		float l = (float) rect.x();
		float t = (float) rect.y();
		float r = (float) (l + rect.w());
		float b = (float) (t + rect.h());
		
		if( filled ){
			ac.drawRect(l, t, r, b, canvasContext.paint());
		}
		else{
			float pts[] = {
					l, t, r, t,
					r, t, r, b,
					r, b, l, b,
					l, b, l, t
			};
			ac.drawLines(pts, canvasContext.paint());
		}
		
		restore();
	}
}
