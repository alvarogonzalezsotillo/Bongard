package ollitos.bot.physics.behaviour;

import java.util.ArrayList;
import java.util.List;

import ollitos.bot.geom.BDirection;
import ollitos.bot.geom.IBRegion;
import ollitos.bot.physics.IBCollision;
import ollitos.bot.physics.IBPhysicalItem;
import ollitos.bot.physics.IBPhysicalListener;
import ollitos.bot.physics.behaviour.BSlideUntilContactBehaviour.Listener;
import ollitos.bot.physics.impulse.BImpulse;
import ollitos.bot.physics.impulse.IBImpulse;
import ollitos.bot.physics.impulse.IBImpulseCause;

public class BStaticPusherBehaviour implements IBMovementBehaviour, IBImpulseCause{

	private static class TemporarySlideUntilContactBehaviour extends BSlideUntilContactBehaviour{

		public TemporarySlideUntilContactBehaviour(IBPhysicalItem i) {
			super(i);
		}
		
		@Override
		protected void stopSliding() {
			super.stopSliding();
			autoDestroy();
		}

		private void autoDestroy() {
			item().removeBehaviour(this);
		}
		
	}
	
	private List<IBCollision> _collisions = new ArrayList<IBCollision>(); 
	private IBPhysicalItem _item;
	
	public BStaticPusherBehaviour(IBPhysicalItem item){
		_item = item;
	}
	
	@Override
	public void nextImpulses(List<IBImpulse> ret){
		for( IBCollision c: _collisions ){
			IBPhysicalItem toPush = c.pushed() == _item ? c.pusher() : c.pushed();
			BDirection faceOfCollision = IBCollision.Util.computeFaceOfCollision( _item, c );
			IBImpulse i = new BImpulse( toPush, faceOfCollision.vector(), this );
			ret.add(i);
			
			if( toPush.behaviour(BSlideUntilContactBehaviour.class) == null ){
				toPush.addBehaviour( new TemporarySlideUntilContactBehaviour(toPush) );
			}
			
		}
		_collisions.clear();
	}


	class Listener extends IBPhysicalListener.Default{
		@Override
		public void collision(IBCollision collision){
			if( collision.pushed() == _item || collision.pusher() == _item ){
				_collisions.add(collision);
			}
		}
	}

	private IBPhysicalListener _listener;
	@Override
	public IBPhysicalListener physicalListener() {
		if (_listener == null) {
			_listener = new Listener();
			
		}
		return _listener;
	}

}
