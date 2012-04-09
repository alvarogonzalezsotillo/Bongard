package ollitos.platform.awt;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

import ollitos.geom.BRectangle;
import ollitos.geom.IBRectangle;
import ollitos.gui.basic.BCanvas;
import ollitos.gui.event.IBEvent;
import ollitos.platform.BFactory;



public class AWTCanvas extends BCanvas{
	
	private class KeyListenerImpl extends KeyAdapter{
		@Override
		public void keyPressed(KeyEvent e) {
			int c = e.getKeyCode();
			BFactory.instance().logger().log( this, "KeyTyped:" + c );
			if( c == KeyEvent.VK_ESCAPE || c == KeyEvent.VK_BACK_SPACE ){
				listeners().handle( new IBEvent(IBEvent.Type.back) );
			}
		}
	}
	
	private class MouseListenerImpl extends MouseAdapter {
		
		
		@Override
		public void mouseClicked(MouseEvent e) {
			listeners().handle( event( IBEvent.Type.pointerClick, e ) );
		}
		
		@Override
		public void mouseDragged(MouseEvent e) {
			listeners().handle( event( IBEvent.Type.pointerDragged, e ) );
		}
		
		@Override
		public void mouseWheelMoved(MouseWheelEvent e) {
			if( e.getWheelRotation() < 0 ){
				listeners().handle( event( IBEvent.Type.zoomIn, e ) );
			}
			else{
				listeners().handle( event( IBEvent.Type.zoomOut, e ) );
			}
		}
		
		@Override
		public void mousePressed(MouseEvent e) {
			listeners().handle( event( IBEvent.Type.pointerDown, e ) );
		}
		
		@Override
		public void mouseReleased(MouseEvent e) {
			listeners().handle( event( IBEvent.Type.pointerUp, e ) );
		}
	}
	
	private AWTPoint pointInOriginalCoords(MouseEvent e){
		if( e == null ){
			return null;
		}
		
		Point p = e.getPoint();
		AWTPoint point = new AWTPoint(0, 0);
		inverseTransform().transform(p, point);
		return point;
	}
	

	private IBEvent event( IBEvent.Type t, MouseEvent e ){
		return new IBEvent( t, pointInOriginalCoords(e), originalSize() );
	}

	
	@SuppressWarnings("serial")
	private class CanvasImpl extends Canvas{
		{
			addComponentListener( new ComponentAdapter() {
				@Override
				public void componentResized(ComponentEvent e) {
					listeners().handle( event( IBEvent.Type.containerResized, null ) );
					AWTCanvas c = AWTCanvas.this;
					c.adjustTransformToSize();
				}
			});
			
			MouseListenerImpl l = new MouseListenerImpl();
			addMouseListener( l ); 
			addMouseMotionListener(l);
			addMouseWheelListener(l);
			addKeyListener( new KeyListenerImpl() );
		}
		
		public void paint(Graphics g) {
			BFactory f = BFactory.instance();
			eraseBackground();
			if( drawable() != null ){
				drawable().draw(AWTCanvas.this, transform());
			}
			Image i = getOffscreenImage();
			g.drawImage(i,0,0,null);
			AWTAnimator a = (AWTAnimator)f.game().animator();
			g.drawString("Millis:" + a.lastStep(), 0, getHeight());
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
		
		//g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
		
		IBRectangle os = drawable().originalSize();
		double pts[] = { os.x(), os.y(), os.x()+os.w(), os.y()+os.h() };
		((AWTTransform)transform()).transform(pts, 0, pts, 0, 2);
		
		g2d.setClip((int)pts[0], (int)pts[1], (int)(pts[2]-pts[0]), (int)(pts[3]-pts[1]));
		
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
		graphics.setColor( (Color) backgroundColor() );
		graphics.fillRect(0, 0, i.getWidth(null), i.getHeight(null));
		graphics.dispose();
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
	public IBRectangle originalSize() {
		Component c = canvasImpl();
		return new BRectangle( 0, 0, c.getWidth(), c.getHeight() );
	}

	public AWTTransform inverseTransform(){
		return (AWTTransform) transform().inverse();
	}

}
