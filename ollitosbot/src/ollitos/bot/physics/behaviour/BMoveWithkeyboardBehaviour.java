package ollitos.bot.physics.behaviour;

import java.util.List;

import ollitos.bot.control.IBPhysicsControl;
import ollitos.bot.physics.IBPhysicalItem;
import ollitos.bot.physics.IBPhysicalListener;
import ollitos.bot.physics.impulse.BImpulse;
import ollitos.bot.physics.impulse.IBImpulse;
import ollitos.gui.event.BEventAdapter;
import ollitos.gui.event.IBEvent;
import ollitos.gui.event.IBEventListener;

public class BMoveWithkeyboardBehaviour implements IBMovementBehaviour{
	private IBPhysicalItem _item;
	

	public BMoveWithkeyboardBehaviour( IBPhysicalItem i ){
		_item = i;
	}

	protected void jump() {
		// TODO: Implement jump state/behaviour
	}


	@Override
	public void nextImpulses(List<IBImpulse> ret) {
        IBPhysicsControl c = _item.physics().control();
        if( c.buttonPressed("right") ){
			_item.rotateTo(_item.direction().right());
		}
		if( c.buttonPressed("left") ){
			_item.rotateTo(_item.direction().left());
		}
		if( c.buttonPressed("forward") ){
			ret.add( new BImpulse(_item, _item.direction().vector(), this) );
		}
	}

    @Override
    public IBPhysicalListener physicalListener() {
        return null;
    }
}
