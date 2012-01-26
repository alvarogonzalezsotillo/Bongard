package purethought.platform.awt;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.Rectangle2D;

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
		g2d.setStroke( new BasicStroke(2) );

		IBRectangle r = originalSize();
		
		Shape s = new Rectangle2D.Double( r.x(), r.y(), r.w(), r.h() );
		g2d.draw(s);
		
		g2d.dispose();

	}

}
