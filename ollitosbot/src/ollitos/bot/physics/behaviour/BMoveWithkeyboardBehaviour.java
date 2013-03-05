package ollitos.bot.physics.behaviour;

import java.util.List;

import ollitos.bot.physics.IBPhysicalItem;
import ollitos.bot.physics.displacement.BSelfDisplacement;
import ollitos.bot.physics.displacement.IBImpulse;
import ollitos.gui.event.BEventAdapter;
import ollitos.gui.event.IBEvent;
import ollitos.gui.event.IBEventListener;

public class BMoveWithkeyboardBehaviour implements IBMovementBehaviour{
	private IBPhysicalItem _item;
	
	private IBEventListener _listener = new BEventAdapter(){
		public boolean keyPressed(IBEvent e) {
			switch(e.keyChar()){
				case 'q': moveForward(); return true;
				case 'o': turnLeft(); return true;
				case 'p': turnRight(); return true;
				case ' ': jump(); return true;
			}
			return false;
		}

	};
	private boolean _turnRight;
	private boolean _turnLeft;
	private boolean _moveForward;

	public BMoveWithkeyboardBehaviour( IBPhysicalItem i ){
		_item = i;
		installListener();
	}

	protected void jump() {
		// TODO Auto-generated method stub
	}

	private void installListener(){
		if( _item.physics().view() == null ){
			throw new IllegalStateException("View is null");
		}
		_item.physics().view().eventSource().addListener(_listener);
	}
	
	private void turnRight(){
		_turnRight = true;
	}

	private void turnLeft(){
		_turnLeft = true;
	}

	private void moveForward(){
		_moveForward = true;
	}

	@Override
	public void nextMovement(List<IBImpulse> ret) {
		if( _turnRight ){
			_item.setDirection(_item.direction().right());
		}
		if( _turnLeft ){
			_item.setDirection(_item.direction().left());
		}
		if( _moveForward ){
			ret.add( new BSelfDisplacement(_item, _item.direction(), this) );
		}
		
		_moveForward = _turnLeft = _turnRight = false;
	}
}
