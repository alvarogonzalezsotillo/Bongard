package purethought.awt;

import java.awt.Canvas;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.geom.AffineTransform;

import purethought.geom.BRectangle;
import purethought.geom.IBRectangle;
import purethought.gui.BCanvas;
import purethought.gui.BGameField;
import purethought.problem.BProblemLocator;
import purethought.util.BFactory;

public class AWTCanvas extends BCanvas{
	
	private class MouseListenerImpl extends MouseAdapter{
		@Override
		public void mouseClicked(MouseEvent e) {
			listeners().pointerClick(null);
		}
		
		@Override
		public void mouseDragged(MouseEvent e) {
			listeners().pointerDrag(null);
		}
		
		@Override
		public void mouseWheelMoved(MouseWheelEvent e) {
			listeners().zoomIn(null);
		}
		
		@Override
		public void mousePressed(MouseEvent e) {
			listeners().pointerDown(null);
		}
		
		@Override
		public void mouseReleased(MouseEvent e) {
			listeners().pointerUp(null);
		}
	}
	
	
	@SuppressWarnings("serial")
	private class CanvasImpl extends Canvas{
		{
			addComponentListener( new ComponentAdapter() {
				@Override
				public void componentResized(ComponentEvent e) {
					BFactory f = BFactory.instance();
					AWTCanvas c = (AWTCanvas) f.canvas();
					c.adjustTransformToSize();
				}
			});
			
			addMouseListener( new MouseListenerImpl() ); 
		}
		
		public void paint(Graphics g) {
			BFactory f = BFactory.instance();
			AWTCanvas canvas = (AWTCanvas)f.canvas();
			canvas.eraseBackground();
			Image i = canvas.getOffscreenImage();
			canvas.drawable().draw(canvas, transform());
			g.drawImage(i,0,0,null);
		}
		public void update(Graphics g){
			paint(g);
		}
	};

	private CanvasImpl _impl;
	private Image _image;
	
	/**
	 * 
	 * @param obj
	 */
	public AWTCanvas() {
		_impl = new CanvasImpl();
	}
	
	public Component canvasImpl(){
		return _impl;
	}
	
	public Graphics2D getGraphics(){
		Image i = getOffscreenImage();
		Graphics2D g2d = (Graphics2D) i.getGraphics();
		
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
		g2d.setRenderingHint(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_ENABLE);
		g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
		
		return g2d;
	}
	
	@Override
	public void refresh() {
		canvasImpl().repaint();
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
		IBRectangle origin = drawable().size();
		IBRectangle destination = size();
		
		transform().setTo(origin, destination);
	}

	/**
	 * 
	 */
	public Image getOffscreenImage() {
		Component c = canvasImpl();
		Dimension d = c.getSize();
		if (_image == null || _image.getWidth(null) != d.width
				|| _image.getHeight(null) != d.height) {
			_image = c.createImage(d.width, d.height);
		}
		return _image;
	}

	@Override
	public IBRectangle size() {
		Component c = canvasImpl();
		return new BRectangle( 0, 0, c.getWidth(), c.getHeight() );
	}

}
