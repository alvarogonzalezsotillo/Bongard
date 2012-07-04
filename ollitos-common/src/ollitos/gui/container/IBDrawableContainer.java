package ollitos.gui.container;

import ollitos.gui.basic.IBDrawable;
import ollitos.gui.event.IBEventConsumer;
import ollitos.gui.event.IBEventSource;

public interface IBDrawableContainer extends IBDrawable, IBEventSource, IBEventConsumer{
	public void addEventConsumer(IBEventConsumer c);
	public void removeEventConsumer(IBEventConsumer c);
}
