package ollitos.platform.awt;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.io.IOException;
import java.net.URL;

import javax.swing.JEditorPane;

import ollitos.geom.IBRectangle;
import ollitos.geom.IBTransform;
import ollitos.gui.basic.BHTMLDrawable;
import ollitos.gui.basic.IBCanvas;
import ollitos.platform.BFactory;
import ollitos.util.BException;


public class AWTHTMLDrawable extends BHTMLDrawable{

	private JEditorPane _textArea;
	
	private JEditorPane textArea(){
		if( _textArea != null ){
			IBRectangle s = originalSize();
			_textArea.setSize( (int)s.w(), (int)s.h());
			
			return _textArea;
		}
		
		_textArea = init();
		
		return _textArea;
	}

	private JEditorPane init() {
		JEditorPane textArea = new JEditorPane();
		if( html() != null ){
			textArea.setText(html());
		}
		
		if( url() != null ){
			try {
				URL u = BFactory.instance().url(url());
				textArea.setPage( u );
			} 
			catch (IOException ex) {
				throw new BException("Cant load:" + url(), ex);
			}
		}
		
		IBRectangle s = originalSize();
		textArea.setSize( (int)s.w(), (int)s.h());
		
		return textArea;
	}
	
	@Override
	protected void draw_internal(IBCanvas c, IBTransform t) {
		AWTCanvas canvas = (AWTCanvas) c;
		
		Graphics2D g2d = canvas.getGraphics();
		g2d.transform((AffineTransform) t);

		textArea().paint(g2d);
		
		g2d.dispose();
	}

	@Override
	public void setUp() {
	}

	@Override
	public void dispose() {
		_textArea = null;
	}

	@Override
	public boolean disposed() {
		return _textArea == null;
	}

}
