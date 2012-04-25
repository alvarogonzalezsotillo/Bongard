package ollitos.gui.event;

import ollitos.platform.BPlatform;
import ollitos.platform.IBGame;

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
		BPlatform.instance().game().screen().addListener(_instance);
		_installed = true;
	}
	
	@Override
	public boolean back() {
		IBGame game = BPlatform.instance().game();
		game.animator().abortAnimations();
		if( game.screen().drawable() != game.defaultDrawable() ){
			game.screen().setDrawable( game.defaultDrawable() );
		}
		return true;
	}

}
