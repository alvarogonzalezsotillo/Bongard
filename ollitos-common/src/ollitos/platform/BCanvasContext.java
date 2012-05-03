package ollitos.platform;

import ollitos.geom.IBTransform;
import ollitos.platform.IBCanvas.CanvasContext;

public class BCanvasContext implements CanvasContext{
	private float alpha;
	private IBColor color;
	private IBTransform transform;
	
	public void setAlpha(float alpha) {
		this.alpha = alpha;
	}

	public void setColor(IBColor color) {
		this.color = color;
	}

	public void setTransform(IBTransform transform) {
		this.transform = transform;
	}

	public void setAntialias(boolean antialias) {
		this.antialias = antialias;
	}

	public boolean antialias;

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
}