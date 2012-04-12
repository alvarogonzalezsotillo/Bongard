package ollitos.gui.basic;

import ollitos.geom.IBRectangle;
import ollitos.geom.IBTransform;
import ollitos.geom.IBTransformHolder;
import ollitos.gui.container.IBDrawableContainer;


public interface IBCanvas extends IBTransformHolder{
	void setTransform(IBTransform t);
	void refresh();
	IBRectangle originalSize();
	void setDrawable( IBDrawableContainer d );
	IBDrawableContainer drawable();
}