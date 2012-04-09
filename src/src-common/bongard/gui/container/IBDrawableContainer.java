package bongard.gui.container;

import ollitos.geom.IBRectangle;
import ollitos.gui.basic.IBDrawable;
import ollitos.gui.event.IBEventListener;
import ollitos.gui.event.IBEventSource;
import bongard.gui.game.BState;

public interface IBDrawableContainer extends IBDrawable, IBEventSource{
	IBEventListener listener();
	IBRectangle originalSize(); // TODO: maybe move to IBDrawable
	
	public BState save();

}
