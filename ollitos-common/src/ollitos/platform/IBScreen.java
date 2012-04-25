package ollitos.platform;

import ollitos.geom.IBRectangle;
import ollitos.gui.container.IBDrawableContainer;
import ollitos.gui.event.IBEventSource;

public interface IBScreen extends IBEventSource{
	void refresh();
	IBRectangle originalSize();
	void setDrawable( IBDrawableContainer d );
	IBDrawableContainer drawable();
	IBCanvas canvas();
}
