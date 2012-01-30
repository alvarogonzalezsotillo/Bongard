package purethought.platform.awt;

import java.awt.AlphaComposite;
import java.awt.Composite;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.util.Formatter;

import purethought.geom.IBTransform;
import purethought.gui.basic.BSprite;
import purethought.gui.basic.IBCanvas;
import purethought.gui.basic.IBRaster;

public class AWTSprite extends BSprite{
	
	private static final boolean SHOW_POSITION = true;

	public AWTSprite(IBRaster raster) {
		super(raster);
	}

	@Override
	protected void draw_internal(IBCanvas c, IBTransform t) {
		AWTCanvas canvas = (AWTCanvas) c;
		
		
		Graphics2D g2d = canvas.getGraphics();
		g2d.transform((AffineTransform) t);
		g2d.setComposite( AlphaComposite.getInstance( AlphaComposite.SRC_OVER, (float)alpha() ) );
		
		
		Image img = raster().getImpl(Image.class);
		
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
