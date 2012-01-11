package purethought.awt;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.geom.AffineTransform;

import purethought.gui.BCanvas;
import purethought.gui.BRectangle;
import purethought.gui.IBRectangle;
import purethought.util.BFactory;

public class AWTCanvas extends BCanvas{
	
	@SuppressWarnings("serial")
	public static class Canvas extends java.awt.Canvas{
		{
			addComponentListener( new ComponentAdapter() {
				@Override
				public void componentResized(ComponentEvent e) {
					BFactory f = BFactory.instance();
					AWTCanvas c = (AWTCanvas) f.canvas();
					c.adjustTransformToSize();
				}
			});
		}
		
		public void paint(Graphics g) {
			BFactory f = BFactory.instance();
			AWTCanvas canvas = (AWTCanvas)f.canvas();
			canvas.eraseBackground();
			Image i = canvas.getOffscreenImage();
			f.field().draw();
			g.drawImage(i,0,0,null);
		}
		public void update(Graphics g){
			paint(g);
		}
	};

	private Canvas _impl;
	private Image _image;
	
	/**
	 * 
	 * @param obj
	 */
	public AWTCanvas(Component impl) {
		_impl = (Canvas) impl;
	}
	
	private Component canvas(){
		return _impl;
	}
	
	public Graphics2D getGraphics(){
		Image i = getOffscreenImage();
		Graphics2D g2d = (Graphics2D) i.getGraphics();
		
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
		g2d.setRenderingHint(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_ENABLE);
		g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

		AffineTransform transform = (AffineTransform)transform();
		g2d.setTransform( transform );
		
		return g2d;
	}
	
	@Override
	public void refresh() {
		canvas().repaint();
	}
	

	/**
	 * 
	 */
	public void eraseBackground(){
		Image i = getOffscreenImage();
		Graphics graphics = i.getGraphics();
		graphics.fillRect(0, 0, i.getWidth(null), i.getHeight(null));
	}
	
	public void adjustTransformToSize(){
		IBRectangle origin = BFactory.instance().field().size();
		IBRectangle destination = size();
		
		transform().setTo(origin, destination);
	}

	/**
	 * 
	 */
	public Image getOffscreenImage() {
		Component c = canvas();
		Dimension d = c.getSize();
		if (_image == null || _image.getWidth(null) != d.width
				|| _image.getHeight(null) != d.height) {
			_image = c.createImage(d.width, d.height);
		}
		return _image;
	}

	@Override
	public IBRectangle size() {
		Component c = canvas();
		return new BRectangle( 0, 0, c.getWidth(), c.getHeight() );
	}

}
