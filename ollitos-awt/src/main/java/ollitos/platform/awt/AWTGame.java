package ollitos.platform.awt;

import java.awt.*;

import javax.swing.SwingUtilities;

import ollitos.platform.BGame;
import ollitos.platform.BPlatform;


public class AWTGame extends BGame{

	private Frame _c;
	
	private AWTScreen _canvas;

	@Override
	public AWTScreen screen(){
		if (_canvas == null) {
			_canvas = new AWTScreen();
		}

		return _canvas;
	}
	
	

	/**
	 *
	 */
	@Override
	public void restore() {
        Component canvasImpl = screen().canvasImpl();
        Window c = container();
		c.setVisible(true);
		super.restore();
        canvasImpl.requestFocusInWindow();
	}

    public Frame container() {
        Component canvasImpl = screen().canvasImpl();
        return (Frame)SwingUtilities.getWindowAncestor(canvasImpl);
    }

    private static BPlatform f() {
		return BPlatform.instance();
	}

	private AWTAnimator _animator;

	@Override
	public AWTAnimator animator() {
		if (_animator == null) {
			_animator = new AWTAnimator();
		}

		return _animator;
	}
	
}
