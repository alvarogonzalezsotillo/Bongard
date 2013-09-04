package ollitos.platform.awt;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.prefs.Preferences;

import javax.swing.SwingUtilities;

import ollitos.bongard.all.BStartField;
import ollitos.platform.BGame;
import ollitos.platform.BPlatform;
import ollitos.platform.state.BState;


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
	
	
	private Frame createFrame(){
		final Frame f = new Frame( "Bongard" );
		f.addWindowListener( new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				f.setVisible(false);
				try{
					saveState();
				}
				catch( Throwable se ){
					se.printStackTrace();
				}
				try{
					System.exit(0);
				}
				catch( SecurityException se){
					se.printStackTrace();
				}
			}
		});
		Component canvas = screen().canvasImpl();
		f.add(canvas);
		
		f.setPreferredSize(new Dimension(320, 480));
        f.pack();
		return f;
	}
	
	private static void save(BState s){
		if( s == null ){
			return;
		}
		Preferences ur = preferences();
		ur.putByteArray("state", s.bytes() );
	}

	private static BState load(){
		Preferences ur = preferences();
		byte[] ba = ur.getByteArray("state", null);
		return (BState) BState.fromBytes(ba);
	}


	private static Preferences preferences() {
		Preferences ur = Preferences.userRoot();
		ur = ur.node(AWTGame.class.getName().toLowerCase());
		return ur;
	}

	/**
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		SwingUtilities.invokeLater( new Runnable() {
			@Override
			public void run() {
				f().game().setDefaultDrawable( new BStartField() );
				f().game().restore();
			}
		} );
	}

	/**
	 * 
	 */
	@Override
	public void restore() {
		Frame c = container();
		c.setVisible(true);
		super.restore();
		screen().canvasImpl().requestFocusInWindow();
	}

	public Frame container() {
		if( _c == null ){
			_c = createFrame();
		}
		return _c;
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
