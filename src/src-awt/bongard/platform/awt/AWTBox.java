package bongard.platform.awt;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;

import bongard.geom.IBRectangle;
import bongard.geom.IBTransform;
import bongard.gui.basic.BBox;
import bongard.gui.basic.IBCanvas;
import bongard.gui.basic.IBColor;


public class AWTBox extends BBox{

	public AWTBox(IBRectangle r, IBColor c ){
		super(r,c);
	}

	@Override
	protected void draw_internal(IBCanvas c, IBTransform t) {
		AWTCanvas canvas = (AWTCanvas) c;
		
		Graphics2D g2d = canvas.getGraphics();
		g2d.transform((AffineTransform) t);
		
		g2d.setColor( (Color) color() );
		g2d.setStroke( new BasicStroke(2) );

		IBRectangle r = originalSize();
		
		Shape s = new Rectangle2D.Double( r.x(), r.y(), r.w(), r.h() );
		if( filled() ){
			g2d.setPaint((Paint) color());
			g2d.fill(s);
		}
		g2d.draw(s);
		
		g2d.dispose();

	}

}
