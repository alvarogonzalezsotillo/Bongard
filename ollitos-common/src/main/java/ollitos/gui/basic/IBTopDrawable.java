package ollitos.gui.basic;

import ollitos.animation.transform.IBTemporaryTransformAnimable;
import ollitos.geom.IBPoint;
import ollitos.geom.IBRectangle;
import ollitos.geom.IBTransform;
import ollitos.geom.IBTransformHolder;
import ollitos.platform.IBCanvas;

public interface IBTopDrawable extends IBDrawable{
  public void setAsTopDrawable();
  public void removedAsTopDrawable();	
}
