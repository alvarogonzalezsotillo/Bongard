package ollitos.bot.physics.behaviour;

import ollitos.bot.geom.IBMovableRegion;
import ollitos.bot.view.BPhysicsView;
import ollitos.gui.event.BEventAdapter;
import ollitos.gui.event.IBEvent;
import ollitos.gui.event.IBEventListener;

public class BMoveWithkeyboardBehaviour implements IBPhysicalBehaviour{
	private BPhysicsView _view;
	private IBMovableRegion _item;
	private IBEventListener _listener = new BEventAdapter(){
		public boolean keyPressed(IBEvent e) {
			switch(e.keyChar()){
				case 'q': moveForward(); return true;
				case 'o': turnLeft(); return true;
				case 'p': turnRight(); return true;
			}
			return false;
		}

	};

	public BMoveWithkeyboardBehaviour( BPhysicsView view, IBMovableRegion i ){
		_item = i;
		_view = view;
		installListener();
	}

	private void installListener(){
		_view.eventSource().addListener(_listener);
	}
	
	private void turnRight(){
	}

	private void turnLeft(){
		// TODO Auto-generated method stub
	}

	private void moveForward(){
		// TODO Auto-generated method stub
	};
	
}
