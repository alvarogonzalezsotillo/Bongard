package ollitos.platform.awt;

import ollitos.gui.basic.BLabel;
import ollitos.platform.IBCanvas;



public class AWTLabel extends BLabel {

	public AWTLabel(String text) {
		super(text);
	}

	@Override
	protected void draw_internal(IBCanvas c) {
		c.drawString(this, text(), 0, 0);
	}
}
