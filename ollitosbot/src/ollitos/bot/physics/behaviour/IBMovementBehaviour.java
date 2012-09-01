package ollitos.bot.physics.behaviour;

import ollitos.bot.physics.displacement.IBDisplacement;

public interface IBMovementBehaviour extends IBPhysicalBehaviour{
	IBDisplacement nextMovement();
}
