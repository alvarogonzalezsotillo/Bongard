package ollitos.gui.event;

import ollitos.gui.basic.IBGame;
import ollitos.platform.BFactory;

public class BRestartListener extends BEventAdapter{

	private static BRestartListener _instance;
	private static boolean _installed;

	private BRestartListener() {
		super(null);
	}
	
	public static void install(){
		if( _installed ){
			return;
		}
		_instance = new BRestartListener();
		BFactory.instance().game().canvas().addListener(_instance);
		_installed = true;
	}
	
	@Override
	public boolean back() {
		IBGame game = BFactory.instance().game();
		game.animator().abortAnimations();
		if( game.canvas().drawable() != game.defaultDrawable() ){
			game.canvas().setDrawable( game.defaultDrawable() );
		}
		return true;
	}

}
