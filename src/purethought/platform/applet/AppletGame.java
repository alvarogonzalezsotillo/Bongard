package purethought.platform.applet;

import java.applet.Applet;
import java.awt.BorderLayout;

import purethought.gui.game.IBGame;
import purethought.platform.awt.AWTCanvas;
import purethought.platform.awt.AWTGame;
import purethought.util.BFactory;

@SuppressWarnings("serial")
public class AppletGame extends Applet {

	@Override
	public void init() {
		IBGame game = BFactory.instance().game();
		((AWTGame)game).setContainer(this);
		
		AWTCanvas canvas = ((AWTGame)game).canvas();
		setLayout( new BorderLayout() );
		add( canvas.canvasImpl(), BorderLayout.CENTER );
	}
	
	@Override
	public void start() {
		BFactory.instance().game().run();
	}
}
