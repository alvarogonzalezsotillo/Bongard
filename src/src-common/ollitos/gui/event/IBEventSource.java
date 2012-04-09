package ollitos.gui.event;

import ollitos.geom.IBTransform;

public interface IBEventSource {
	IBTransform transform();
	void addListener(IBEventListener l);
	void removeListener(IBEventListener l);
}
