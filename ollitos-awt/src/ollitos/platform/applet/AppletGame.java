package ollitos.platform.applet;

import java.applet.Applet;
import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import ollitos.platform.BPlatform;
import ollitos.platform.IBGame;
import ollitos.platform.state.BStateManager;
import bongard.gui.game.BStartField;



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
				IBGame game = BPlatform.instance().game();
				game.setDefaultDrawable( new BStartField() );
				game.restore();
			}
		});
		add( button, BorderLayout.CENTER );
	}
	
	@Override
	public void start() {
	}
}
