package purethought.gui.basic;

import purethought.geom.IBPoint;
import purethought.geom.IBTransform;
import purethought.geom.IBTransformable;

public interface IBDrawable extends IBTransformable{
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
