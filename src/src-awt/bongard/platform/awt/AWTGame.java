package bongard.platform.awt;

import java.awt.Component;
import java.awt.Container;
import java.awt.Frame;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.ByteArrayInputStream;
import java.util.prefs.Preferences;
import java.util.prefs.PreferencesFactory;

import javax.swing.SwingUtilities;

import bongard.gui.container.IBDrawableContainer;
import bongard.gui.game.BGame;
import bongard.gui.game.BState;
import bongard.platform.BFactory;
import bongard.util.BException;


public class AWTGame extends BGame{

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
					save( state() );
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
		Component canvas = canvas().canvasImpl();
		f.add(canvas);
		
		f.setSize( 320, 480 );
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
		return BState.fromBytes(ba);
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
				BState state = null;
				try{
					state = load();
				}
				catch( Throwable e ){
					e.printStackTrace();
				}
				f().game().restore(state);
			}
		} );
	}

	/**
	 * 
	 */
	@Override
	public void restore(BState state) {
		super.restore(state);
		Container c = container();
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
