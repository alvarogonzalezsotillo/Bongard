package bongard.gui.game;

import java.io.IOException;

import ollitos.geom.BRectangle;
import ollitos.geom.IBRectangle;
import ollitos.gui.basic.BButton;
import ollitos.gui.basic.BSprite;
import ollitos.gui.container.BFlippableContainer;
import ollitos.gui.container.IBDrawableContainer;
import ollitos.gui.container.IBFlippableDrawable;
import ollitos.gui.container.IBFlippableModel;
import ollitos.platform.BResourceLocator;
import ollitos.platform.BState;
import ollitos.platform.IBRaster;
import ollitos.util.BException;


public class BGameHelp extends BFlippableContainer{

	
	
	public BGameHelp(){
		super();
	}
	
	private IBFlippableModel createModel() {
		return new IBFlippableModel() {
			
			private IBFlippableDrawable _html;
			
			public IBFlippableDrawable html(){
				if (_html == null) {
					BResourceLocator l = new BResourceLocator("/examples/help.html" );
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
					button.install(BGameHelp.this);
					_html = ( button );
				}
				return _html;
			}
			
			@Override
			public int width() {
				return 3;
			}
			
			@Override
			public IBFlippableDrawable drawable(int x) {
				return html();
			}
			
			@Override
			public void dispose(int x) {
			}
			
			@Override
			public BResourceLocator background() {
				return null;
			}
		};
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
