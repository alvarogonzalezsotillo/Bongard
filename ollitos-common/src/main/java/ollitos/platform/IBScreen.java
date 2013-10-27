package ollitos.platform;

import ollitos.geom.IBRectangle;
import ollitos.geom.IBTransformHolder;
import ollitos.gui.basic.IBDrawable;
import ollitos.gui.event.IBEventSource;

public interface IBScreen extends IBEventSource, IBTransformHolder{
	void refresh();
	IBRectangle originalSize();
	void setDrawable( IBDrawable d );
	IBDrawable drawable();
	IBCanvas canvas();
}
