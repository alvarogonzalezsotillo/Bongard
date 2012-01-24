package purethought.platform.awt;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;

import purethought.geom.IBRectangle;
import purethought.geom.IBTransform;
import purethought.gui.basic.BBox;
import purethought.gui.basic.IBCanvas;

public class AWTBox extends BBox{

	public AWTBox(IBRectangle r) {
		super(r);
	}

	@Override
	protected void draw_internal(IBCanvas c, IBTransform t) {
		AWTCanvas canvas = (AWTCanvas) c;
		
		Graphics2D g2d = canvas.getGraphics();
		g2d.transform((AffineTransform) t);
		
		g2d.setColor( Color.white );

		IBRectangle r = originalSize();
		
		g2d.drawRect((int)r.x(), (int)r.y(), (int)r.w(), (int)r.h() );
		
		g2d.dispose();

	}

}
