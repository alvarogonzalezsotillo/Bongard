package bongard.gui.game;

import java.io.IOException;

import ollitos.geom.BRectangle;
import ollitos.geom.IBRectangle;
import ollitos.gui.basic.BButton;
import ollitos.gui.basic.BSprite;
import ollitos.gui.basic.IBDrawable;
import ollitos.gui.container.BSlidableContainer;
import ollitos.gui.container.IBDrawableContainer;
import ollitos.gui.container.IBSlidableModel;
import ollitos.gui.container.IBSlidablePage;
import ollitos.platform.BPlatform;
import ollitos.platform.BResourceLocator;
import ollitos.platform.IBCanvas;
import ollitos.platform.IBRaster;
import ollitos.platform.state.BState;
import ollitos.util.BException;



public class BGameHelp extends BSlidableContainer{

	public BGameHelp(){
		super(BGameField.computeOriginalSize(),createModel());
	}
	
	private static IBSlidableModel createModel() {
		return new Model();
	}
	
	private static class Model implements IBSlidableModel{
			
		private IBSlidablePage _fd[];
		
		public Model(){
			_fd = new IBSlidablePage[width()];
		}

		@Override
		public int width(){
			return 10;
		}
		
		public static IBDrawable internal(){
			BResourceLocator l = new BResourceLocator("/examples/help.html" );
			IBRaster r;
			IBRectangle htmlRectangle = new BRectangle(0, 0, 240, 320);
			try {
				r = BPlatform.instance().rasterUtil().html(htmlRectangle, l);
			}
			catch (IOException ex) {
				throw new BException( "Cant load:" + l, ex );
			}
			BSprite sprite = new BSprite(r);
			sprite.setAntialias(true);
			BButton b = new BButton(sprite);
			return b;
		}
		
		@Override
		public IBSlidablePage page(int x) {
			if( _fd[x] != null ){
				return _fd[x];
			}
			
			_fd[x] = new IBSlidablePage() {
				
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
				public IBDrawable icon() {
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
		public BGameHelp create() {
			return new BGameHelp();
		}
	}
	
	@Override
	public BState save() {
		return new MyState();
	}
}
