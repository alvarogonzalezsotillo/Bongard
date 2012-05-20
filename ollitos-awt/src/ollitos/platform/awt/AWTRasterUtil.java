package ollitos.platform.awt;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.JEditorPane;

import ollitos.animation.BFixedDurationAnimation;
import ollitos.geom.BRectangle;
import ollitos.geom.IBRectangle;
import ollitos.gui.basic.BBox;
import ollitos.platform.BPlatform;
import ollitos.platform.BResourceLocator;
import ollitos.platform.IBRaster;
import ollitos.platform.IBRasterUtil;



public class AWTRasterUtil implements IBRasterUtil{

	private static class BProgressAnimation extends BFixedDurationAnimation{

		
		private IBRaster _r;
		private BBox _b;

		public BProgressAnimation(IBRaster r) {
			super(10*1000);
			_r = r;
			_b = new BBox( new BRectangle( 10, 10, 0, 10 ), BPlatform.instance().color("ffff00") );
		}

		@Override
		public void stepAnimation(long millis) {
			stepMillis(millis);
			_b.setOriginalSize( new BRectangle(10, 10, currentMillis()*(_r.w()-10*2)/totalMillis(), 10) );
			_r.canvas().drawBox(_b, _b.originalSize(), true );
		}
		
	}
	
	
	private static final class PageLoadedListener implements PropertyChangeListener {
		
		private final class RefreshRunnable implements Runnable {
			private int _times = 4;
			@Override
			public void run(){
				Graphics2D g = _r.canvas().graphics();
				_ep.paint(g);
				BPlatform.instance().game().screen().refresh();
				_times--;
				if( _times > 0 ){
					BPlatform.instance().game().animator().post( TIME_TO_LOAD_PAGE, this );
				}
			}
		}

		private final JEditorPane _ep;
		private final AWTRaster _r;

		private PageLoadedListener(JEditorPane ep, AWTRaster r) {
			_ep = ep;
			_r = r;
		}

		@Override
		public void propertyChange(PropertyChangeEvent evt) {
			Graphics2D g = _r.canvas().graphics();
			_ep.paint(g);
			_ep.removePropertyChangeListener(this);
			BPlatform.instance().game().animator().post( TIME_TO_LOAD_PAGE, new RefreshRunnable());
		}
	}

	private static final int TIME_TO_LOAD_PAGE = 1000;

	/**
	 * Extract one tile of a Bongard problem
	 * @param r
	 * @param i
	 * @return
	 */
	@Override
	public IBRaster extract( IBRectangle r, IBRaster i ){
		BufferedImage ret = new BufferedImage((int)r.w(), (int)r.h(), BufferedImage.TYPE_INT_RGB);
		Graphics g = ret.getGraphics();
		Color bgcolor = Color.white;

		int x = (int) -r.x();
		int y = (int) -r.y();
		
		Image img = ((AWTRaster)i).image();
		g.drawImage(img, x, y, bgcolor, null);
		g.dispose();
		
		return new AWTRaster(ret);
	}
	
	@Override
	public AWTRaster raster(InputStream is, boolean transparent ) throws IOException {
		Color bgColor = Color.white;
		BufferedImage image = ImageIO.read( is );
		if( transparent ){
			return new AWTRaster(image);
		}
		BufferedImage ret = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);
		Graphics g = ret.getGraphics();
		g.drawImage(image, 0, 0, bgColor, null );
		g.dispose();
		
		return new AWTRaster(ret);
	}

	@Override
	public IBRaster html(IBRectangle s, BResourceLocator rl) throws IOException {
		final JEditorPane ep = new JEditorPane();
		ep.setBackground((AWTColor)BPlatform.COLOR_DARKGRAY);
		ep.setSize( (int)s.w(), (int)s.h());
		URL u = null;
		
		if( rl.url() != null ){
			u = rl.url();
		}
		if( u == null ){
			u = BPlatform.instance().platformURL( rl );
		}
		if( u != null ){
			ep.setPage( u );
		}
		
		final AWTRaster r = raster(s);
		Graphics2D g = r.canvas().graphics();
		ep.paint(g);
		
		ep.addPropertyChangeListener("page", new PageLoadedListener(ep, r));

		BPlatform.instance().game().animator().addAnimation( new BProgressAnimation(r) );
		
		return r;
	}

	@Override
	public AWTRaster raster(IBRectangle r) {
		AWTScreen screen = (AWTScreen) BPlatform.instance().game().screen();
		Image i = screen.canvasImpl().createImage((int)r.w(), (int)r.h());
		AWTRaster ret = new AWTRaster(i);
		
		return ret;
	}
}
