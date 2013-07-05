package ollitos.gui.basic;

import ollitos.geom.IBRectangle;
import ollitos.platform.BPlatform;
import ollitos.platform.BResourceLocator;
import ollitos.platform.IBDisposable;

public abstract class BHTMLDrawable extends BRectangularDrawable implements IBDisposable{

	private String _html;
	private BResourceLocator _url;

    public BHTMLDrawable(IBRectangle r) {
        super(r);
    }

    public BHTMLDrawable() {
		this(BPlatform.instance().game().defaultScreenSize());
	}
	
	public String html(){
		return _html;
	}
	
	public void setHtml(String html){
		_html = html;
		_url = null;
		dispose();
	}
	
	public void load(BResourceLocator url){
		_url = url;
		_html = null;
		dispose();
	}
	
	@Override
	public void setOriginalSize(IBRectangle r) {
		super.setOriginalSize(r);
		dispose();
	}

	public BResourceLocator url() {
		return _url;
	}

}
