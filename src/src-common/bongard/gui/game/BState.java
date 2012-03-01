package bongard.gui.game;

import java.io.Serializable;

import bongard.gui.basic.IBDrawable;

@SuppressWarnings("serial")
public abstract class BState implements Serializable{

	public abstract IBDrawable createDrawable();
}
