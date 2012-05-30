package ollitos.gui.basic;

import ollitos.geom.IBPoint;
import ollitos.geom.IBRectangle;
import ollitos.geom.IBTransform;
import ollitos.platform.BPlatform;
import ollitos.platform.IBCanvas;
import ollitos.platform.IBColor;

public class BLabel extends BDrawable{
	
	private String _text;

	public BLabel(String text){
		setText(text);
		setColor(BPlatform.COLOR_WHITE);
	}
	
	public IBColor color(){
		return canvasContext().color();
	}
	
	public void setColor(IBColor c){
		canvasContext().setColor(c);
	}

	@Override
	public boolean inside(IBPoint p, IBTransform aditionalTransform) {
		return false;
	}

	public String text() {
		return _text;
	}

	public void setText(String text) {
		_text = text;
	}

	@Override
	protected void draw_internal(IBCanvas c) {
		c.drawString(this, text(), 0, 0);
	}

	@Override
	public IBRectangle originalSize() {
		throw new IllegalStateException();
	}

}
