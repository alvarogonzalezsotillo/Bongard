package purethought.gui;

import purethought.geom.IBTransform;

public interface IBEventSource {
	IBTransform transform();
	void addListener(IBEventListener l);
	void removeListener(IBEventListener l);
}
