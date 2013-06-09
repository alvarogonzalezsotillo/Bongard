package ollitos.bot.physics.behaviour;

import java.util.List;

import ollitos.bot.physics.BPlayerAction;
import ollitos.bot.physics.IBPhysicalItem;
import ollitos.bot.physics.IBPhysicalListener;
import ollitos.bot.physics.impulse.BImpulse;
import ollitos.bot.physics.impulse.IBImpulse;
import ollitos.gui.event.BEventAdapter;
import ollitos.gui.event.IBEvent;
import ollitos.gui.event.IBEventListener;

public class BMoveWithkeyboardBehaviour implements IBMovementBehaviour{
	private IBPhysicalItem _item;
	
	private boolean _turnRight;
	private boolean _turnLeft;
	private boolean _moveForward;

	
	public BMoveWithkeyboardBehaviour( IBPhysicalItem i ){
		_item = i;
	}

	protected void jump() {
		// TODO: Implement jump state/behaviour
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
	public void nextImpulses(List<IBImpulse> ret) {
		if( _turnRight ){
			_item.rotateTo(_item.direction().right());
		}
		if( _turnLeft ){
			_item.rotateTo(_item.direction().left());
		}
		if( _moveForward ){
			ret.add( new BImpulse(_item, _item.direction().vector(), this) );
		}
		
		_moveForward = _turnLeft = _turnRight = false;
	}

	private class Listener extends IBPhysicalListener.Default{
		@Override
		public void playerAction(BPlayerAction pa) {
			switch(pa){
				case moveForward: moveForward(); return;
				case turnLeft: turnLeft(); return;
				case turnRight: turnRight(); return;
				case jump: jump(); return;
			}
		}
	};

	private IBPhysicalListener _listener;
	@Override
	public IBPhysicalListener physicalListener() {
		if (_listener == null) {
			_listener = new Listener();
		}

		return _listener;
	}
}
