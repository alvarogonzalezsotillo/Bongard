package ollitos.gui.event;

import ollitos.geom.Holder;


public interface IBEventSource extends Holder{
	void addListener(IBEventListener l);
	void removeListener(IBEventListener l);
	boolean preHandleEvent(IBEvent e);
	boolean postHandleEvent(IBEvent e);
}
