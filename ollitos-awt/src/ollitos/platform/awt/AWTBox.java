package ollitos.platform.awt;

import ollitos.geom.IBRectangle;
import ollitos.geom.IBTransform;
import ollitos.gui.basic.BBox;
import ollitos.platform.IBCanvas;
import ollitos.platform.IBColor;



public class AWTBox extends BBox{

	public AWTBox(IBRectangle r, IBColor c ){
		super(r,c);
	}

	@Override
	protected void draw_internal(IBCanvas c, IBTransform t) {
		c.drawBox(this, originalSize(), filled() );
		/*
		AWTScreen canvas = (AWTScreen) c;
		
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
		*/
	}

}
