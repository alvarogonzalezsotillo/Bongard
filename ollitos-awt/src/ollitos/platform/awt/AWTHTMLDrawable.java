package ollitos.platform.awt;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.net.URL;

import javax.swing.JEditorPane;

import ollitos.geom.IBRectangle;
import ollitos.geom.IBTransform;
import ollitos.gui.basic.BHTMLDrawable;
import ollitos.platform.BPlatform;
import ollitos.platform.IBCanvas;
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
		textArea.addPropertyChangeListener("page", new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				BPlatform.instance().game().screen().refresh();
			}
		});
		if( html() != null ){
			textArea.setText(html());
		}
		
		URL u = null;
		
		if( url() != null && url().url() != null ){
			u = url().url();
		}
		if( u == null ){
			u = BPlatform.instance().platformURL( url() );
		}
		
		if( u != null ){
			try {
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
		
		Graphics2D g2d = (Graphics2D) canvas.graphics().create();
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
