package purethought.platform.applet;

import java.applet.Applet;
import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import purethought.gui.game.IBGame;
import purethought.platform.awt.AWTCanvas;
import purethought.platform.awt.AWTGame;
import purethought.util.BFactory;

@SuppressWarnings("serial")
public class AppletGame extends Applet {

	@Override
	public void init() {
		setLayout( new BorderLayout() );
		Button button = new Button("Start");
		button.addActionListener( new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				System.out.println( "Starting game frame..." );
				BFactory.instance().game().run();
			}
		});
		add( button, BorderLayout.CENTER );
	}
	
	@Override
	public void start() {
	}
}
