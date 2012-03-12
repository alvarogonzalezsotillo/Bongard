package bongard.gui.container;

import bongard.geom.IBRectangle;
import bongard.gui.basic.IBDrawable;
import bongard.gui.event.IBEventListener;
import bongard.gui.event.IBEventSource;
import bongard.gui.game.BState;

public interface IBDrawableContainer extends IBDrawable, IBEventSource{
	IBEventListener listener();
	IBRectangle originalSize(); // TODO: maybe move to IBDrawable
	
	public BState save();

}
