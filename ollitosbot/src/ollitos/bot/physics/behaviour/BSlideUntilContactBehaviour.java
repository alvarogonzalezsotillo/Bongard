package ollitos.bot.physics.behaviour;

import java.util.List;

import ollitos.bot.geom.IBLocation;
import ollitos.bot.geom.IBRegion;
import ollitos.bot.geom.IBRegion.Vertex;
import ollitos.bot.physics.IBCollision;
import ollitos.bot.physics.IBPhysicalItem;
import ollitos.bot.physics.IBPhysicalListener;
import ollitos.bot.physics.impulse.BImpulse;
import ollitos.bot.physics.impulse.IBImpulse;
import ollitos.platform.BPlatform;
import ollitos.platform.IBLogger;

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
		
		IBLogger logger = BPlatform.instance().logger();
		logger.log( "Colision:" + collision );
		logger.log( "Causa de la colision:" + collision.cause() );
		logger.log( "Causa de la causa de la colision:" + collision.cause().rootCause() );
		
		if( BGravityBehaviour.gravityCollision(collision) ){
			return;
		}
		
		IBPhysicalItem pushed = collision.pushed();
		IBPhysicalItem pusher = collision.pusher();
		
		if( pushed == _item ){
			stopSliding();
			startSliding(collision.cause().delta().vector());
		}
		
		if( pusher == _item ){
			stopSliding();
		}
	}

	private void stopSliding() {
		IBLogger logger = BPlatform.instance().logger();
		logger.log( "Stop sliding:" + _item );
		_vector = null;
	}

	private void startSliding(IBLocation vector) {
		IBLogger logger = BPlatform.instance().logger();
		logger.log( "Start sliding:" + _item );
		_vector = IBLocation.Util.normalize(vector).vector();
	}

	public boolean sliding(){
		return _vector != null;
	}
	
	@Override
	public void stepFinished() {
	}

	@Override
	public void nextImpulses(List<IBImpulse> ret) {
		if( !sliding() ){
			return;
		}
		ret.add( new BImpulse(_item, _vector, this) );
	}

	@Override
	public void itemMoved(IBPhysicalItem i, IBRegion oldRegion) {
		if( i != _item ){
			return;
		}
		if( sliding() ){
			return;
		}
		IBLocation v = IBLocation.Util.subtract( i.region().vertex(Vertex.aVertex), oldRegion.vertex(Vertex.aVertex), null );
		startSliding(v);
	}
	
}

