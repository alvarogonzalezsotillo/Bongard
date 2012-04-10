package ollitos.gui.event;


public interface IBEventSource {
	void addListener(IBEventListener l);
	void removeListener(IBEventListener l);
}
