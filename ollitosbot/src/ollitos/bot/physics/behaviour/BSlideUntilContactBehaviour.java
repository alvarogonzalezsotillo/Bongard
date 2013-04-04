package ollitos.bot.physics.behaviour;

import java.util.List;

import ollitos.bot.geom.BDirection;
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
	private boolean _stopInThisStep = false;
	
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
		
		if( sliding() ){
			BDirection faceOfCollision = IBCollision.Util.computeFaceOfCollision(_item, collision);
			BDirection v = IBLocation.Util.normalize(_vector);
			if( faceOfCollision == v ){
				stopSliding();
			}
		}
		
	}

	private void stopSliding() {
		_vector = null;
		_stopInThisStep = true;
	}

	private void startSliding(IBLocation vector) {
		if( _stopInThisStep ){
			return;
		}
		_vector = IBLocation.Util.normalize(vector).vector();
	}

	public boolean sliding(){
		return _vector != null;
	}
	
	@Override
	public void stepFinished() {
		_stopInThisStep = false;
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
		
		BDirection d = IBLocation.Util.normalize(v);
		if( d == BDirection.down || d == BDirection.up ){
			return;
		}
		
		startSliding(v);
	}
	
}

