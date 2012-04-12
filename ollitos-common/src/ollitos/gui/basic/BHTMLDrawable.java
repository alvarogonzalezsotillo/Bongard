package ollitos.gui.basic;

import ollitos.geom.IBRectangle;
import ollitos.platform.BFactory;

public abstract class BHTMLDrawable extends BRectangularDrawable implements IBDisposable{

	private String _html;
	
	public BHTMLDrawable() {
		super(BFactory.instance().game().defaultScreenSize());
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
