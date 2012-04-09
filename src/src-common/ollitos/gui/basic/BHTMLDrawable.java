package ollitos.gui.basic;

import ollitos.geom.IBRectangle;
import bongard.gui.game.BGameField;

public abstract class BHTMLDrawable extends BRectangularDrawable implements IBDisposable{

	private String _html;
	
	public BHTMLDrawable() {
		super(BGameField.computeOriginalSize());
	}
	
	public String html(){
		return _html;
	}
	
	public void setHtml(String html){
		_html = html;
		dispose();
	}
	
	@Override
	public void setOriginalSize(IBRectangle r) {
		super.setOriginalSize(r);
		dispose();
	}
}
