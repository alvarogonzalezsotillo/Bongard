package purethought.platform.awt;

import java.awt.Component;
import java.awt.Container;
import java.awt.Frame;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.SwingUtilities;

import purethought.gui.container.BFlippableContainer;
import purethought.gui.game.BGameModel;
import purethought.gui.game.IBGame;
import purethought.platform.BFactory;
import purethought.platform.BImageLocator;

public class AWTGame implements IBGame, Runnable{

	private Container _c;
	
	private AWTCanvas _canvas;

	@Override
	public AWTCanvas canvas(){
		if (_canvas == null) {
			_canvas = new AWTCanvas();
		}

		return _canvas;
	}
	
	
	private Frame createFrame(){
		final Frame f = new Frame( "Bongard" );
		f.addWindowListener( new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				f.setVisible(false);
				try{
					System.exit(0);
				}
				catch( SecurityException se ){
				}
			}
		});
		Component canvas = canvas().canvasImpl();
		f.add(canvas);
		
		f.setSize( 320, 480 );
		return f;
	}
	
	/**
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		SwingUtilities.invokeLater( f().game() );
	}

	/**
	 * 
	 */
	@Override
	public void run() {
		Container c = container();
		BImageLocator[] problems = f().cardExtractor().randomProblems(13);
		canvas().setDrawable( new BFlippableContainer( new BGameModel(problems ) ) );
		c.setVisible(true);
	}

	private Container container() {
		if( _c == null ){
			_c = createFrame();
		}
		return _c;
	}
	

	private static BFactory f() {
		return BFactory.instance();
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
