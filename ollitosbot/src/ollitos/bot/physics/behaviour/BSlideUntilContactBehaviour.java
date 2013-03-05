package ollitos.bot.physics.behaviour;

import java.util.List;

import ollitos.bot.geom.IBLocation;
import ollitos.bot.physics.IBCollision;
import ollitos.bot.physics.IBPhysicalItem;
import ollitos.bot.physics.IBPhysicalListener;
import ollitos.bot.physics.displacement.BImpulse;
import ollitos.bot.physics.displacement.IBImpulse;

public class BSlideUntilContactBehaviour implements IBMovementBehaviour, IBPhysicalListener{

	private IBPhysicalItem _item;
	private IBLocation _vector;
	
	public BSlideUntilContactBehaviour(IBPhysicalItem i){
		_item = i;
	}

	@Override
	public void itemAdded(IBPhysicalItem i) {
	}

	@Override
	public void itemRemoved(IBPhysicalItem i) {
	}

	@Override
	public void collision(IBCollision collision) {
		
		if( BGravityBehaviour.gravityCollision(collision) ){
			return;
		}
		
		IBPhysicalItem pushed = collision.pushed();
		IBPhysicalItem pusher = collision.pusher();
		
		if( pushed == _item ){
			stopSliding();
			startSliding(collision.cause().finalDelta());
		}
		
		if( pusher == _item ){
			stopSliding();
		}
	}

	private void stopSliding() {
		_vector = null;
	}

	private void startSliding(IBLocation vector) {
		_vector = IBLocation.Util.normalize(vector).vector();
		_vector = IBLocation.Util.scale(_vector, 2, _vector);
	}

	@Override
	public void stepFinished() {
	}

	@Override
	public void nextMovement(List<IBImpulse> ret) {
		if( _vector == null ){
			return;
		}
		ret.add( new BImpulse(_item, _vector, this) );
	}
	
}

