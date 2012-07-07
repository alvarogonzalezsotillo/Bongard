package ollitos.platform.awt;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;

import ollitos.geom.IBRectangle;
import ollitos.platform.IBCanvas;
import ollitos.platform.IBRaster;

public class AWTCanvas implements IBCanvas{
	
	public static Font FONT = Font.decode( "Arial-10");
	
	private Graphics2D _graphics;

	public AWTCanvas( Graphics2D g ){
		_graphics = g;
	}
	
	public Graphics2D graphics(){
		return _graphics;
	}

	private static Graphics2D cloneAndSet( Graphics2D g, CanvasContextHolder cp ){
		Graphics2D ret =(Graphics2D) g.create();
		ret.setFont(FONT);
		CanvasContext c = cp.canvasContext();
		
		ret.transform((AffineTransform) c.transform());
		ret.setColor((Color) c.color());
		ret.setComposite( AlphaComposite.getInstance( AlphaComposite.SRC_OVER, c.alpha() ) );
		
		if( c.antialias() ){
			ret.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
			ret.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			ret.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
		}
		else{
			ret.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_DEFAULT);
			ret.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
			ret.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
		}
		
		return ret;
	}
	
	@Override
	public void drawString(CanvasContextHolder c, String str, float x, float y) {
		Graphics2D g = cloneAndSet(_graphics, c);
		g.drawString(str, x, y);
		g.dispose();
	}

	@Override
	public void drawRaster(CanvasContextHolder c, IBRaster r, float x, float y) {
		Graphics2D g = cloneAndSet(_graphics, c);
		g.drawImage(((AWTRaster)r).image(), (int)x, (int)y, null);
		g.dispose();
	}

	@Override
	public void drawBox(CanvasContextHolder c, IBRectangle r, boolean filled) {
		Graphics2D g = cloneAndSet(_graphics, c);
		g.setStroke( new BasicStroke(2) );

		Shape s = new Rectangle2D.Double( r.x(), r.y(), r.w(), r.h() );
		if( filled ){
			g.setPaint((Paint)g.getColor());
			g.fill(s);
		}
		g.draw(s);
		
		g.dispose();
	}

}
