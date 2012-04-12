package ollitos.platform.awt;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.util.Formatter;

import ollitos.geom.IBTransform;
import ollitos.gui.basic.BSprite;
import ollitos.gui.basic.IBCanvas;
import ollitos.gui.basic.IBRaster;



public class AWTSprite extends BSprite{
	
	private static final boolean SHOW_POSITION = false;

	public AWTSprite(IBRaster raster) {
		super(raster);
	}

	@Override
	protected void draw_internal(IBCanvas c, IBTransform t) {
		AWTCanvas canvas = (AWTCanvas) c;
		
		
		Graphics2D g2d = canvas.getGraphics();
		
		if( antialias() ){
			g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
			g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
		}
		
		g2d.transform((AffineTransform) t);
		g2d.setComposite( AlphaComposite.getInstance( AlphaComposite.SRC_OVER, (float)alpha() ) );
		
		
		Image img = ((AWTRaster)raster()).image();
		
		int x = -img.getWidth(null)/2;
		int y = -img.getHeight(null)/2;
		
		g2d.drawImage(img, x, y, null);
		
		if( SHOW_POSITION ){
			AffineTransform at = (AffineTransform) transform();
			String s = new Formatter().format("%2.2f, %2.2f", at.getTranslateX(), at.getTranslateY() ).toString();
			g2d.drawString(s, 0, 0);
		}
		
		g2d.dispose();
	}
}
