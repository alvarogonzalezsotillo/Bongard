package ollitos.platform;

import ollitos.geom.IBRectangle;
import ollitos.geom.IBTransform;

public interface IBCanvas{
	public interface CanvasContextProvider{
		CanvasContext canvasContext();
	}
	
	public interface CanvasContext extends CanvasContextProvider{
		public float alpha();
		public IBColor color();
		public IBTransform transform();
		public boolean antialias();
	}
	
	void drawString(CanvasContextProvider c, String str, float x, float y );
	void drawRaster(CanvasContextProvider c, IBRaster r, float x, float y );
	void drawBox( CanvasContextProvider c,  IBRectangle r, boolean filled );
}