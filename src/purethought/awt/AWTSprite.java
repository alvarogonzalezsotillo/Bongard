package purethought.awt;

import java.awt.Component;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;

import purethought.gui.BSprite;
import purethought.gui.IBCanvas;
import purethought.gui.IBRaster;
import purethought.gui.IBTransform;

public class AWTSprite extends BSprite{

	
	public AWTSprite(IBRaster raster) {
		super(raster);
	}

	@Override
	protected void draw_internal(IBCanvas c, IBTransform t) {
		AWTCanvas canvas = (AWTCanvas) c;
		Component comp = canvas.canvas();
		
		Graphics2D g2d = (Graphics2D) comp.getGraphics();
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
		g2d.setRenderingHint(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_ENABLE);
		g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
		g2d.setTransform((AffineTransform) t);
		
		Image img = raster().getImpl(Image.class);
		
		int x = (int) originalHull()[0].x();
		int y = (int) originalHull()[0].y();
		
		g2d.drawImage(img, x, y, null);
		g2d.dispose();
	}


	
}
