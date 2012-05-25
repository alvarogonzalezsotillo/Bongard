package bongard.gui.game;

import java.io.IOException;

import ollitos.geom.BRectangle;
import ollitos.geom.IBRectangle;
import ollitos.geom.IBTransform;
import ollitos.gui.basic.BButton;
import ollitos.gui.basic.BSprite;
import ollitos.gui.basic.IBDrawable;
import ollitos.gui.container.BDrawableContainer;
import ollitos.gui.container.IBDrawableContainer;
import ollitos.platform.BPlatform;
import ollitos.platform.BResourceLocator;
import ollitos.platform.BState;
import ollitos.platform.IBCanvas;
import ollitos.platform.IBRaster;
import ollitos.util.BException;


public class BGameHelp extends BDrawableContainer{

	private IBDrawable _html;
	
	public IBDrawable html(){
		if (_html == null) {
			BResourceLocator l = new BResourceLocator("/images/examples/help.html" );
			IBRaster r;
			IBRectangle s = originalSize();
			IBRectangle htmlRectangle = new BRectangle(0, 0, 240, 240*s.h()/s.w());
			try {
				r = platform().rasterUtil().html(htmlRectangle, l);
			} catch (IOException ex) {
				throw new BException( "Cant load:" + l, ex );
			}
			BSprite ret = new BSprite(r);
			ret.setAntialias(true);

			BButton button = new BButton(ret);
			button.setSizeTo(s, false, true);
			button.install(this);
			_html = button;
		}
		return _html;
	}
	
	@Override
	protected void draw_internal(IBCanvas c) {
		IBTransform t = canvasContext().transform();
		html().draw(c, t);
	}

	private IBRectangle _rectangle = new BRectangle(0, 0, 200, 320 );
	
	@Override
	public IBRectangle originalSize() {
		return _rectangle;
	}
	
	@SuppressWarnings("serial")
	private static class MyState extends BState{
		@Override
		public IBDrawableContainer createDrawable() {
			return new BGameHelp();
		}
	}
	
	@Override
	public BState save() {
		return new MyState();
	}
}
