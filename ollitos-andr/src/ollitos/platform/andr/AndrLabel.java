package ollitos.platform.andr;

import ollitos.gui.basic.BLabel;
import ollitos.platform.IBCanvas;

public class AndrLabel extends BLabel {

	public AndrLabel(String text) {
		super(text);
	}

	@Override
	protected void draw_internal(IBCanvas c) {
		c.drawString(this, text(), 0, 0);
	}
}
