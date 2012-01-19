package purethought.platform.applet;

import java.applet.Applet;

import purethought.gui.IBGame;
import purethought.platform.awt.AWTGame;
import purethought.util.BFactory;

@SuppressWarnings("serial")
public class AppletGame extends Applet {

	@Override
	public void init() {
		IBGame game = BFactory.instance().game();
		((AWTGame)game).setContainer(this);
	}
	
	@Override
	public void start() {
		BFactory.instance().game().run();
	}
}
