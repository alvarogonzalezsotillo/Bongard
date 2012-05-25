package ollitos.gui.container;

import ollitos.geom.IBRectangle;
import ollitos.gui.basic.IBDrawable;
import ollitos.gui.event.IBEventListener;
import ollitos.gui.event.IBEventSource;
import ollitos.platform.BState;

public interface IBDrawableContainer extends IBDrawable, IBEventSource, IBEventConsumer{

	IBRectangle originalSize(); // TODO: maybe move to IBDrawable
	
	public BState save();

}
