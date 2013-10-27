package ollitos.gui.basic;

import ollitos.animation.transform.IBTemporaryTransformAnimable;
import ollitos.geom.IBPoint;
import ollitos.geom.IBRectangle;
import ollitos.geom.IBTransform;
import ollitos.geom.IBTransformHolder;
import ollitos.platform.IBCanvas;

public interface IBDrawable extends IBTemporaryTransformAnimable, IBTransformHolder, IBCanvas.CanvasContextHolder{
	
	public interface DrawableHolder {
		IBDrawable drawable();
	}
	
	public void setTransform(IBTransform t);
	public void draw(IBCanvas c,IBTransform aditionalTransform);
	public IBPoint position();
	IBRectangle originalSize();
	

	
	
	/**
	 * Decides where the point is inside de drawable
	 * @param p in drawable coordinates (after aplying its transform)
	 * @param aditionalTransform another transform to concatenate to the drawable
	 * @return
	 */
	public boolean inside(IBPoint p, IBTransform aditionalTransform);
}
