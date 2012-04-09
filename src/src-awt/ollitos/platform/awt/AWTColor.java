package ollitos.platform.awt;

import java.awt.Color;

import ollitos.gui.basic.IBColor;



@SuppressWarnings("serial")
public class AWTColor extends Color implements IBColor{

	public AWTColor(Color c) {
		super(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha());
	}
	
}
