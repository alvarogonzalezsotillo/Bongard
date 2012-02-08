package bongard.gui.event;

import bongard.geom.IBTransform;

public interface IBEventSource {
	IBTransform transform();
	void addListener(IBEventListener l);
	void removeListener(IBEventListener l);
}
