package ollitos.platform;

import ollitos.geom.IBTransform;
import ollitos.platform.IBCanvas.CanvasContext;

public class BCanvasContextDelegate extends BCanvasContext{
	
	
	private BCanvasContext _original;
	private boolean _overrideAlpha;
	private boolean _overrideColor;
	private boolean _overrideTransform;
	private boolean _overrideAntialias;

	public BCanvasContextDelegate( BCanvasContext c ){
		_original = c;
	}
	
	public void setAlpha(float alpha) {
		super.setAlpha(alpha);
		_overrideAlpha = true;
	}

	public void setColor(IBColor color) {
		super.setColor(color);
		_overrideColor = true;
	}

	public void setTransform(IBTransform transform) {
		super.setTransform(transform);
		_overrideTransform = true;
	}

	public void setAntialias(boolean antialias) {
		super.setAntialias(antialias);
		_overrideAntialias = true;
	}


	@Override
	public float alpha() {
		if( _overrideAlpha ) return super.alpha();
		return _original.alpha();
	}

	@Override
	public IBColor color() {
		if( _overrideColor ) return super.color();
		return _original.color();
	}

	@Override
	public IBTransform transform() {
		if( _overrideTransform ) return super.transform();
		return _original.transform();
	}

	@Override
	public boolean antialias() {
		if( _overrideAntialias ) return super.antialias();
		return _original.antialias();
	}
}