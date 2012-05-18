package bongard.gui.game;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import ollitos.geom.BRectangle;
import ollitos.geom.IBRectangle;
import ollitos.geom.IBTransform;
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
import ollitos.util.BTransformUtil;


public class BGameHelp extends BDrawableContainer{

	private IBDrawable _html;
	
	public static String read( InputStream i ) throws IOException{
		InputStreamReader r = new InputStreamReader(i);
		StringBuffer ret = new StringBuffer();
		int c = r.read();
		while( c != -1 ){
			ret.append((char)c);
			c = r.read();
		}
		r.close();
		return ret.toString();
	}
	
	public IBDrawable html(){
		if (_html == null) {
			BResourceLocator l = new BResourceLocator("/images/examples/help.html" );
//			_html = BPlatform.instance().html();
//			_html.load(l);
//			_html.setOriginalSize(originalSize());
			IBRaster r;
			IBRectangle htmlRectangle = new BRectangle(0, 0, 320, 480);
			IBRectangle s = originalSize();
			BPlatform platform = BPlatform.instance();
			try {
				r = platform.rasterUtil().html(htmlRectangle, l);
			} catch (IOException ex) {
				throw new BException( "Cant load:" + l, ex );
			}
			BSprite ret = new BSprite(r);
			ret.setAntialias(true);

			platform.logger().log( this, "Original " + s + ": " + ret.originalSize() );
			IBTransform t = ret.transform();
			BTransformUtil.setTo(t, ret.originalSize(), s, true, true);
			platform.logger().log( this, "final " + s + ": " + BTransformUtil.transform(t, ret.originalSize() ) );
			_html = ret;
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
