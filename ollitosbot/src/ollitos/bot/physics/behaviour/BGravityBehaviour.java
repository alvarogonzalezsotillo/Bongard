package ollitos.bot.physics.behaviour;

import java.util.List;

import ollitos.bot.geom.BDirection;
import ollitos.bot.geom.IBLocation;
import ollitos.bot.physics.IBCollision;
import ollitos.bot.physics.IBPhysicalItem;
import ollitos.bot.physics.displacement.IBDisplacement;
import ollitos.bot.physics.impulse.BImpulse;
import ollitos.bot.physics.impulse.IBImpulse;
import ollitos.bot.physics.impulse.IBImpulseCause;

public class BGravityBehaviour implements IBMovementBehaviour{

	private IBPhysicalItem _item;

	public BGravityBehaviour(IBPhysicalItem item){
		_item = item;
	}
	
	@Override
	public void nextImpulses(List<IBImpulse> ret){
		ret.add( new GravityImpulse() );
	}
	
	private class GravityImpulse extends BImpulse{
		public GravityImpulse(){
			super(_item, BDirection.down.vector(), BGravityBehaviour.this );
		}
	}
	
	public static boolean gravityCollision(IBCollision c){
		return c.cause().rootCause() instanceof GravityImpulse;
	}

}
