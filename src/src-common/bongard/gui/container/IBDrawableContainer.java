package bongard.gui.container;

import bongard.geom.IBRectangle;
import bongard.gui.basic.IBDrawable;
import bongard.gui.event.IBEventListener;
import bongard.gui.event.IBEventSource;

public interface IBDrawableContainer extends IBDrawable, IBEventSource{
	IBEventListener listener();
	IBRectangle originalSize(); // TODO: maybe move to IBDrawable
}
