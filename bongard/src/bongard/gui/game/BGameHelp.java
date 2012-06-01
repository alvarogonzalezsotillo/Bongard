package bongard.gui.game;

import java.io.IOException;

import ollitos.geom.BRectangle;
import ollitos.geom.IBRectangle;
import ollitos.gui.basic.BButton;
import ollitos.gui.basic.BSprite;
import ollitos.gui.basic.IBDrawable;
import ollitos.gui.basic.IBRectangularDrawable;
import ollitos.gui.container.BFlippableContainer;
import ollitos.gui.container.IBDrawableContainer;
import ollitos.gui.container.IBFlippableDrawable;
import ollitos.gui.container.IBFlippableModel;
import ollitos.platform.BPlatform;
import ollitos.platform.BResourceLocator;
import ollitos.platform.BState;
import ollitos.platform.IBCanvas;
import ollitos.platform.IBRaster;
import ollitos.util.BException;



public class BGameHelp extends BFlippableContainer{

	public BGameHelp(){
		super(BGameField.computeOriginalSize(),createModel());
	}
	
	private static IBFlippableModel createModel() {
		return new Model();
		//return new BBongardGameModel();
	}
	
	private static class Model implements IBFlippableModel{
			
		private IBFlippableDrawable _fd[];
		
		public Model(){
			_fd = new IBFlippableDrawable[width()];
		}

		@Override
		public int width(){
			return 10;
		}
		
		
		public static IBDrawable internal(){
			BResourceLocator l = new BResourceLocator("/examples/help.html" );
			IBRaster r;
			IBRectangle htmlRectangle = new BRectangle(0, 0, 240, 480);
			try {
				r = BPlatform.instance().rasterUtil().html(htmlRectangle, l);
			}
			catch (IOException ex) {
				throw new BException( "Cant load:" + l, ex );
			}
			BSprite sprite = new BSprite(r);
			sprite.setAntialias(true);
//			return sprite;

			final BButton button = new BButton(sprite);
			button.setSizeTo(htmlRectangle, true, true);
			BButton ret = button;
			return ret;
		}
		
		@Override
		public IBFlippableDrawable drawable(int x) {
			if( _fd[x] != null ){
				return _fd[x];
			}
			
			_fd[x] = new IBFlippableDrawable() {
				
				private IBDrawable _d;

				@Override
				public void setUp() {
				}
				
				@Override
				public boolean disposed() {
					return false;
				}
				
				@Override
				public void dispose() {
				}
				
				@Override
				public IBDrawable drawable() {
					if( _d == null ){
						_d = internal();
					}
					return _d;
				}
				
				@Override
				public IBRectangularDrawable icon() {
					return null;
				}
			};
			
			return _fd[x];
		}
		
		@Override
		public void dispose(int x) {
		}
		
		@Override
		public BResourceLocator background() {
			return null;
		}
	};
			
	@Override
	protected void draw_internal(IBCanvas c) {
		super.draw_internal(c);
		c.drawBox(this, originalSize(), false);
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
