package bongard.gui.game;

import java.io.Serializable;

import bongard.gui.container.IBDrawableContainer;

@SuppressWarnings("serial")
public abstract class BState implements Serializable{
	public abstract IBDrawableContainer createDrawable();
}
