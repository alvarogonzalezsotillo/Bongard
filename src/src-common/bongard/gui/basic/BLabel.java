package bongard.gui.basic;

import bongard.geom.IBPoint;
import bongard.geom.IBTransform;

public abstract class BLabel extends BDrawable{
	
	private String _text;

	public BLabel(String text){
		setText(text);
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
}
