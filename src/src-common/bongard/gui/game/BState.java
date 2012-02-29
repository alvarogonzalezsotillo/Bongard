package bongard.gui.game;

import java.io.Serializable;

import bongard.gui.basic.IBDrawable;

public abstract class BState implements Serializable{

	public abstract IBDrawable createDrawable();
}
