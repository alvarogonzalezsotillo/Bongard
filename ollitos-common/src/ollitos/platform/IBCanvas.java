package ollitos.platform;

import ollitos.geom.IBRectangle;
import ollitos.geom.IBTransform;

public interface IBCanvas{
	public interface CanvasContextProvider{
		CanvasContext canvasContext();
	}
	
	public interface CanvasContext{
		public float alpha();
		public IBColor color();
		public IBTransform transform();
		public boolean antialias();
	}
	
	public static class DefaultCanvasContext implements CanvasContext{
		public float alpha;
		public IBColor color;
		public IBTransform transform;
		public boolean antialias;

		public static final DefaultCanvasContext create(){
			DefaultCanvasContext ret = new DefaultCanvasContext();
			ret.alpha = 1;
			ret.color = BPlatform.COLOR_BLACK;
			ret.transform = BPlatform.instance().identityTransform();
			ret.antialias = false;
			return ret;
		}

		@Override
		public float alpha() {
			return alpha;
		}

		@Override
		public IBColor color() {
			return color;
		}

		@Override
		public IBTransform transform() {
			return transform;
		}

		@Override
		public boolean antialias() {
			return antialias;
		}
	};
	
	void drawString(CanvasContextProvider c, String str, float x, float y );
	void drawRaster(CanvasContextProvider c, IBRaster r, float x, float y );
	void drawBox( CanvasContextProvider c,  IBRectangle r, boolean filled );
}