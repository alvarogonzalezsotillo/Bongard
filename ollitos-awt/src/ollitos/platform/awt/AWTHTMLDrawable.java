package ollitos.platform.awt;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;

import javax.swing.JLabel;
import javax.swing.JTextPane;

import ollitos.geom.IBRectangle;
import ollitos.geom.IBTransform;
import ollitos.gui.basic.BHTMLDrawable;
import ollitos.gui.basic.IBCanvas;


public class AWTHTMLDrawable extends BHTMLDrawable{

	private JLabel _textArea;
	
	private JLabel textArea(){
		if( _textArea != null ){
			IBRectangle s = originalSize();
			_textArea.setSize( (int)s.w(), (int)s.h());
			return _textArea;
		}
		
		_textArea = new JLabel();
		_textArea.setForeground(Color.white);
		_textArea.setText(html());
		IBRectangle s = originalSize();
		_textArea.setSize( (int)s.w(), (int)s.h());
		
		return _textArea;
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
