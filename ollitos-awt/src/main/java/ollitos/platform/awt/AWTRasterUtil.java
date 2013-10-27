package ollitos.platform.awt;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.JEditorPane;

import ollitos.geom.IBRectangle;
import ollitos.platform.BPlatform;
import ollitos.platform.BResourceLocator;
import ollitos.platform.IBColor;
import ollitos.platform.IBRaster;
import ollitos.platform.raster.IBRasterUtil;
import ollitos.util.BException;


public class AWTRasterUtil implements IBRasterUtil{

	
	
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
	public IBRaster extract( IBRectangle r, IBRaster i, IBColor background ){
		BufferedImage ret = new BufferedImage((int)r.w(), (int)r.h(), BufferedImage.TYPE_INT_RGB);
		Graphics g = ret.getGraphics();
		Color bgcolor = (AWTColor)background;

		int x = (int) -r.x();
		int y = (int) -r.y();
		
		Image img = ((AWTRaster)i).image();
		g.drawImage(img, x, y, bgcolor, null);
		g.dispose();
		
		return new AWTRaster(ret);
	}
	
	@Override
	public AWTRaster raster(InputStream is ) throws IOException {
		BufferedImage image = ImageIO.read( is );

//		try {
//			Thread.sleep( (long) (Math.random()*20000) );
//		}
//		catch (InterruptedException ex) {
//		}
		

		return new AWTRaster(image);
	}

	@Override
	public IBRaster html(IBRectangle s, BResourceLocator rl) throws BException {
        try{
            return html(s,rl,null);
        }
        catch( IOException e ){
            throw new BException(e.toString(),e);
        }
    }

    private IBRaster html(IBRectangle r, BResourceLocator rl, String str) throws IOException{
		final JEditorPane ep = new JEditorPane();
		ep.setBackground((AWTColor)BPlatform.COLOR_DARKGRAY);
        Dimension d = new Dimension((int)r.w(), (int)r.h());
		ep.setSize(d);
        ep.setPreferredSize(d);
        ep.setMaximumSize(d);
        ep.setMinimumSize(d);

        if( rl !=null ){
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
        }
        else if( str != null ){
            ep.setContentType("text/html");
            ep.setText(str);
        }
        else{
            throw new IllegalArgumentException();
        }
		
		final AWTRaster ret = raster(r);
		Graphics2D g = ret.canvas().graphics();
		ep.paint(g);
		
		ep.addPropertyChangeListener("page", new PageLoadedListener(ep, ret));

		BPlatform.instance().game().animator().addAnimation( new BProgressAnimation(ret) );
		
		return ret;
	}

    @Override
    public IBRaster html(IBRectangle r, String html) throws BException{
        try{
            return html(r,null,html);
        }
        catch( IOException e ){
            throw new BException(e.toString(),e);
        }
    }

    @Override
	public AWTRaster raster(IBRectangle r) {
		AWTScreen screen = (AWTScreen) BPlatform.instance().game().screen();
		Image i = screen.canvasImpl().createImage((int)r.w(), (int)r.h());
		AWTRaster ret = new AWTRaster(i);
		
		return ret;
	}
}
