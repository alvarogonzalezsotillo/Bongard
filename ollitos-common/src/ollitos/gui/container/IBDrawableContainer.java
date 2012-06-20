package ollitos.gui.container;

import ollitos.gui.basic.IBRectangularDrawable;
import ollitos.gui.event.IBEventConsumer;
import ollitos.gui.event.IBEventSource;
import ollitos.platform.BState;

public interface IBDrawableContainer extends IBRectangularDrawable, IBEventSource, IBEventConsumer{

	public BState save();
	public void addEventConsumer(IBEventConsumer c);
	public void removeEventConsumer(IBEventConsumer c);

}
