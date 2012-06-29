package ollitos.gui.event;

import ollitos.platform.BPlatform;
import ollitos.platform.IBGame;

public class BRestartListener extends BEventAdapter{

	private static BRestartListener _instance;

	private BRestartListener() {
		super(null);
	}
	
	public static void install(){
		if( _instance != null ){
			return;
		}
		_instance = new BRestartListener();
		BPlatform.instance().game().screen().addListener(_instance);
	}
	
	@Override
	public boolean back() {
		IBGame game = BPlatform.instance().game();
		game.animator().abortAnimations();
		if( game.screen().drawable() != game.defaultDrawable() ){
			game.screen().setDrawable( game.defaultDrawable() );
			return true;
		}
		else{
			return false;
		}
	}

}
