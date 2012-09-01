package ollitos.bot.physics.behaviour;

import ollitos.bot.geom.BDirection;
import ollitos.bot.physics.IBCollision;
import ollitos.bot.physics.IBPhysicalItem;
import ollitos.bot.physics.displacement.BDisplacement;
import ollitos.bot.physics.displacement.IBDisplacement;
import ollitos.bot.physics.displacement.IBDisplacementCause;

public class BGravityBehaviour implements IBMovementBehaviour, IBDisplacementCause{

	private IBPhysicalItem _item;

	public BGravityBehaviour(IBPhysicalItem item){
		_item = item;
	}
	
	@Override
	public IBDisplacement nextMovement(){
		return new GravityDisplacement();
	}
	
	private class GravityDisplacement extends BDisplacement{
		public GravityDisplacement(){
			super(_item, BDirection.down.vector(), BGravityBehaviour.this );
		}
	}
	
	public static boolean gravityCollision(IBCollision c){
		return c.cause() instanceof GravityDisplacement;
	}

}
