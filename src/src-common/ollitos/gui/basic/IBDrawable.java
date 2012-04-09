package ollitos.gui.basic;

import ollitos.animation.IBTransformAnimable;
import ollitos.geom.IBPoint;
import ollitos.geom.IBTransform;
import ollitos.geom.IBTransformable;
import bongard.gui.game.BState;

public interface IBDrawable extends IBTransformable, IBTransformAnimable{
	public IBTransform transform();
	public void setTransform(IBTransform t);
	public void draw(IBCanvas c,IBTransform aditionalTransform);
	public IBPoint position();
	
	
	/**
	 * Decides where the point is inside de drawable
	 * @param p in drawable coordinates (after aplying its transform)
	 * @param aditionalTransform another transform to concatenate to the drawable
	 * @return
	 */
	public boolean inside(IBPoint p, IBTransform aditionalTransform);
}
