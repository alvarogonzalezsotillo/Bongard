package ollitos.platform;

import ollitos.geom.IBRectangle;
import ollitos.geom.IBTransform;

public interface IBCanvas{
	public interface CanvasContextHolder{
		CanvasContext canvasContext();
	}
	
	public interface CanvasContext extends CanvasContextHolder{
		public float alpha();
		public IBColor color();
		public IBTransform transform();
		public boolean antialias();
	}
	
	void drawString(CanvasContextHolder c, String str, float x, float y );
	void drawRaster(CanvasContextHolder c, IBRaster r, float x, float y );
	void drawBox( CanvasContextHolder c,  IBRectangle r, boolean filled );
	void drawLine( CanvasContextHolder c, float x1, float y1, float x2, float y2 );
}