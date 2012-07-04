package ollitos.gui.container;

import ollitos.gui.basic.IBDrawable;
import ollitos.platform.BState;
import ollitos.gui.event.IBEventConsumer;
import ollitos.gui.event.IBEventSource;

public interface IBDrawableContainer extends IBDrawable, IBEventSource, IBEventConsumer, BState.Stateful{
	public void addEventConsumer(IBEventConsumer c);
	public void removeEventConsumer(IBEventConsumer c);
}
